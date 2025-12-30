/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.feature.home.selectAddress

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.feature.home.selectAddress.domain.model.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class SelectAddressViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appEventBus: AppEventBus,
    savedStateHandle: SavedStateHandle,
) : CoreViewModel<SelectAddressScreenContract.State, SelectAddressScreenContract.SideEffect, SelectAddressScreenContract.Event>(
    initialState = SelectAddressScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),
    ),
) {
    private val placesClient by lazy { Places.createClient(context) }

    override fun setEvent(event: SelectAddressScreenContract.Event) {
        when (event) {
            is SelectAddressScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is SelectAddressScreenContract.Event.NavigateUp -> setSideEffect(SelectAddressScreenContract.SideEffect.NavigateUp)
            is SelectAddressScreenContract.Event.PopBackStack -> setSideEffect(SelectAddressScreenContract.SideEffect.PopBackStack)
            is SelectAddressScreenContract.Event.DismissDialogs -> dismissDialogs()
            is SelectAddressScreenContract.Event.SetTextSearch -> setTextSearch(event.text)
            is SelectAddressScreenContract.Event.LoadNearbyLocations -> loadNearbyLocations()
            is SelectAddressScreenContract.Event.SelectedAddress -> setSelectedAddress(
                title = event.title,
                address = event.address,
                city = event.city,
                country = event.country,
            )
            is SelectAddressScreenContract.Event.OnMapClick -> {
                updateState { state ->
                    state.copy(
                        userLocation = event.location,
                    )
                }
            }
            is SelectAddressScreenContract.Event.FindSelectedAddress -> findSelectedAddress()
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        searchListen()
        if (!Places.isInitialized()) {
            Places.initialize(context, appBuildConfigStatePack.googleMapsKey)
        }
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun searchListen() {
        state.distinctUntilChangedBy { it.textSearch.value }
            .debounce(SelectAddressScreenContract.Static.SEARCH_DEBOUNCE)
            .map { it.textSearch.value }
            .filter { it.length >= SelectAddressScreenContract.Static.MIN_SEARCH_LENGTH }
            .onEach(::searchPlacesWithGoogleMaps)
            .launchIn(viewModelScope)
    }

    private fun setTextSearch(text: String) {
        val textFieldValue = viewState.textSearch.copy(
            value = text,
        )
        updateState { state ->
            state.copy(
                textSearch = textFieldValue,
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun loadNearbyLocations() {
        updateState { state ->
            state.copy(
                isLoading = true,
            )
        }

        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { resp ->
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        nearbyLocations = resp.placeLikelihoods.map { it.place }.map {
                            toUIModel(
                                primaryText = it.displayName ?: "-",
                                fullText = it.formattedAddress ?: "-",
                                latitude = it.location?.latitude,
                                longitude = it.location?.longitude,
                            )
                        },
                    )
                }

                val locationProvider = LocationServices.getFusedLocationProviderClient(context)
                locationProvider.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val latitude = location?.latitude
                        val longitude = location?.longitude
                        if (latitude == null) return@addOnSuccessListener
                        if (longitude == null) return@addOnSuccessListener
                        updateState { state ->
                            state.copy(
                                userLocation = LatLng(latitude, longitude),
                            )
                        }
                    }
            }
            .addOnFailureListener { error ->
                updateState { state ->
                    state.copy(
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
    }

    var searchPlacesJob: Job? = null
    fun searchPlacesWithGoogleMaps(query: String) {
        searchPlacesJob?.cancel()
        searchPlacesJob = viewModelScope.launch {
            val token = AutocompleteSessionToken.newInstance()
            val req = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build()

            val result = placesClient.findAutocompletePredictions(req).asDeferred().await()
            val items = result.autocompletePredictions.map {
                toUIModel(
                    primaryText = it.getPrimaryText(null).toString(),
                    fullText = it.getFullText(null).toString(),
                    latitude = null,
                    longitude = null,
                )
            }
            updateState { state ->
                state.copy(
                    isLoading = false,
                    filteredNearbyLocations = items,
                )
            }
        }
    }

    private fun setSelectedAddress(
        title: String,
        address: String,
        city: String?,
        country: String?,
        latitude: Double? = null,
        longitude: Double? = null,
    ) = viewModelScope.launch {
        appEventBus.updateSelectedAddress(
            title = title,
            address = address,
            city = city,
            country = country,
            latitude = latitude,
            longitude = longitude,
        )
        setSideEffect(SelectAddressScreenContract.SideEffect.NavigateUp)
    }

    @Suppress("MissingPermission", "TooGenericExceptionCaught")
    private fun findSelectedAddress() {
        val userLocation = viewState.userLocation ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val title = address.featureName ?: address.thoroughfare ?: "-"
                    val fullAddress = address.getAddressLine(0) ?: "-"
                    val city = address.locality
                    val country = address.countryName

                    withContext(Dispatchers.Main) {
                        setSelectedAddress(
                            title = title,
                            address = fullAddress,
                            city = city,
                            country = country,
                            latitude = userLocation.latitude,
                            longitude = userLocation.longitude,
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    updateState {
                        it.copy(alertDialogModel = e.toAlertDialog)
                    }
                }
            }
        }
    }
}

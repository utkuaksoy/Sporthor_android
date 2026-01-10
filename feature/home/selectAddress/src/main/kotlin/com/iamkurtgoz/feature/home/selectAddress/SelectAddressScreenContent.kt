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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSelectAddressRoute
import com.iamkurtgoz.core.resources.R as resourcesR

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SelectAddressScreenContent(
    state: SelectAddressScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectAddressScreenContract.Event) -> Unit,
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
        onPermissionsResult = {},
    )

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (locationPermissionState.allPermissionsGranted) {
            setEvent.invoke(SelectAddressScreenContract.Event.LoadNearbyLocations)
        } else {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    if (state.route.isMapActive && state.textSearch.value.isEmpty() && state.userLocation != null) {
        Column {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(state.userLocation, 15f)
            }

            val uiSettings by remember {
                mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
            }
            val properties by remember {
                mutableStateOf(MapProperties(mapType = MapType.NORMAL))
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapClick = {
                    setEvent.invoke(SelectAddressScreenContract.Event.OnMapClick(it))
                },
            ) {
                MarkerComposable(
                    state = MarkerState(position = state.userLocation),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = AppTheme.colors.generalColors.primaryGreen,
                                shape = AppTheme.shapes.radiusCircle,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = resourcesR.drawable.img_location_pin),
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
        ) {
            when {
                state.filteredNearbyLocations.isNotEmpty() -> {
                    this.itemsIndexed(
                        items = state.filteredNearbyLocations,
                        key = { index, place ->
                            "place_$index$place"
                        },
                        itemContent = { index, place ->
                            AddressView(
                                title = place.name,
                                address = place.address,
                                city = place.city,
                                country = place.country,
                                setEvent = setEvent,
                            )
                        },
                    )
                }
                else -> {
                    this.itemsIndexed(
                        items = state.nearbyLocations,
                        key = { index, place ->
                            "place_$index$place"
                        },
                        itemContent = { index, place ->
                            AddressView(
                                title = place.name,
                                address = place.address,
                                city = place.city,
                                country = place.country,
                                setEvent = setEvent,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressView(
    title: String,
    address: String,
    city: String?,
    country: String?,
    modifier: Modifier = Modifier,
    setEvent: (SelectAddressScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val event = SelectAddressScreenContract.Event.SelectedAddress(
                    title = title,
                    address = address,
                    city = city,
                    country = country,
                )
                setEvent.invoke(event)
            }
            .padding(all = AppTheme.spacing.spacingMedium),
    ) {
        Text(
            text = title,
            style = AppTheme.typography.heading06,
        )

        Text(
            text = address,
            style = AppTheme.typography.bodyMediumCompact,
        )
    }
    HorizontalDivider()
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectAddressScreenContent(
                state = SelectAddressScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSelectAddressRoute(
                        isMapActive = true,
                        isBlackBackground = false,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}

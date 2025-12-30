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
package com.iamkurtgoz.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.iamkurtgoz.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationRepository {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val geocoder = Geocoder(context, Locale.getDefault())

    override suspend fun getCurrentLocation(): Location? {
        return withContext(Dispatchers.IO) {
            var lastKnownLocation: Location? = null
            try {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastKnownLocation == null) {
                        lastKnownLocation =
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                } else if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    lastKnownLocation =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
            } catch (e: SecurityException) {
                println("Konum izni reddedildi: ${e.message}")
                return@withContext null
            }
            lastKnownLocation
        }
    }

    override suspend fun getAddressFromLocation(location: Location): LocationRepository.AddressResult = withContext(Dispatchers.IO) {
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val address = addresses[0]

                val city = address.adminArea ?: ""
                val district = address.subAdminArea ?: ""
                val country = address.countryName ?: ""

                LocationRepository.AddressResult(
                    city = city,
                    district = district,
                    country = country,
                )
            } else {
                LocationRepository.AddressResult("", "", "")
            }
        } catch (_: Exception) {
            LocationRepository.AddressResult("", "", "")
        }
    }
}

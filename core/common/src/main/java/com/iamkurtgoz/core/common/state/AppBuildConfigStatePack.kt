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
package com.iamkurtgoz.core.common.state

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBuildConfigStatePack @Inject constructor() {
    companion object {
        private const val SEPARATOR = '/'
        private const val QUESTION_MARK = "?"
        private const val ACCESS_TOKEN = "accessToken"
        private const val EQUAL_MARK = "="
    }

    var packageName: String = ""
    var buildType: String = "debug"
    var flavor: String = "beta"
    var isDebug: Boolean = false
    var websiteAddress: String = ""
    var termsAddress: String = ""
    var privacyPolicyAddress: String = ""
    var contactAddress: String = ""
    var googlePlayAddress: String = ""
    var googlePlayAccountProfileAddress: String = ""
    var apiUrl: String = ""
    var socketUrl: String = ""
    var mediaFilePrefixUrl: String = ""
    var googleServiceClientId: String = ""
    var googleMapsKey: String = ""
    var versionCode: Int = 0
    var versionName: String = ""

    fun getMediaFileDownloadableAddress(fileKey: String?, accessToken: String?): String? {
        if (fileKey == null) return null
        if (accessToken == null) return null
        return mediaFilePrefixUrl
            .plus(fileKey)
            .plus(SEPARATOR)
            .plus(QUESTION_MARK)
            .plus(ACCESS_TOKEN)
            .plus(EQUAL_MARK)
            .plus(accessToken)
    }

    fun getMediaFileDownloadableAddress(fileAddress: String?): String? {
        if (fileAddress == null) return null
        val isSeparatorExist = mediaFilePrefixUrl.lastOrNull() == SEPARATOR || fileAddress.firstOrNull() == SEPARATOR
        return mediaFilePrefixUrl
            .plus(if (isSeparatorExist) "" else SEPARATOR)
            .plus(fileAddress)
    }
}

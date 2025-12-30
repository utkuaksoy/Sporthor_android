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
package com.iamkurtgoz.data.crypto

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import com.iamkurtgoz.domain.crypto.CryptoHelper
import timber.log.Timber
import java.security.KeyStore
import java.util.Base64
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import kotlin.concurrent.withLock

internal class CryptoHelperImpl @Inject constructor() : CryptoHelper {

    private val charset = Charsets.UTF_8
    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val secretKey: SecretKey = getOrCreateSecretKey()
    private val lock = ReentrantLock(FAIR_ORDERING_POLICY)

    companion object {
        private const val PROVIDER = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_ALIAS = "CryptoAlias"
        private const val IV_SEPARATOR = "]"
        private const val IV_PART_INDEX = 0
        private const val ENCRYPTED_DATA_PART_INDEX = 1
        private const val ENCRYPTED_DATA_PARTS_COUNT = 2
        private const val FAIR_ORDERING_POLICY = true
    }

    override fun encryptData(rawData: String): String = lock.withLock {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val ivString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encode(cipher.iv).toString(charset)
        } else {
            android.util.Base64.encodeToString(cipher.iv, android.util.Base64.DEFAULT)
        }
        val encryptedDataByteArray = cipher.doFinal(rawData.toByteArray(charset))
        val encryptedDataString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encode(encryptedDataByteArray).toString(charset)
        } else {
            android.util.Base64.encodeToString(encryptedDataByteArray, android.util.Base64.DEFAULT)
        }
        ivString + IV_SEPARATOR + encryptedDataString
    }

    @Suppress("TooGenericExceptionCaught")
    override fun decryptData(encryptedData: String): Any = lock.withLock {
        val split = encryptedData.split(IV_SEPARATOR.toRegex())

        if (split.size != ENCRYPTED_DATA_PARTS_COUNT) {
            Timber.d("EncryptionHelper", "Passed data is incorrect.")
            throw IllegalArgumentException("Passed data is incorrect.", Throwable("Passed data is incorrect"))
        }

        val ivByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(split[IV_PART_INDEX])
        } else {
            android.util.Base64.decode(split[IV_PART_INDEX], android.util.Base64.DEFAULT)
        }
        val ivSpec = IvParameterSpec(ivByteArray)

        val encryptedDataByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(split[ENCRYPTED_DATA_PART_INDEX])
        } else {
            android.util.Base64.decode(split[ENCRYPTED_DATA_PART_INDEX], android.util.Base64.DEFAULT)
        }

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        try {
            val decryptedData = cipher.doFinal(encryptedDataByteArray)
            val decryptedString = decryptedData.toString(charset)
            return@withLock decryptedString
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to decrypt data", e)
        }
    }

    private fun getOrCreateSecretKey(): SecretKey {
        return getSecretKey() ?: createSecretKey()
    }

    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM_AES, PROVIDER)
        val builder = KeyGenParameterSpec.Builder(KEY_ALIAS, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
            .setBlockModes(BLOCK_MODE_CBC)
            .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey? {
        val keyStore = KeyStore.getInstance(PROVIDER).apply { load(null) }
        return (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.secretKey
    }
}

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
package com.iamkurtgoz.core.designsystem.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object PhoneNumberVisualTransformation : VisualTransformation {

    private const val ZERO = 0
    private const val ONE = 1
    private const val TWO = 2
    private const val THREE = 3
    private const val FIRST_SPACE_POSITION = 2 // after XXX
    private const val SECOND_SPACE_POSITION = 5 // after XXX XXX
    private const val THIRD_SPACE_POSITION = 7 // after XXX XXX XX
    private const val LAST_DIGIT_INDEX = 9 // 10th digit index = 9

    override fun filter(text: AnnotatedString): TransformedText {
        // 1. Limit the input to a maximum of 10 digits
        val trimmed = if (text.text.length > LAST_DIGIT_INDEX) {
            text.text.substring(ZERO..LAST_DIGIT_INDEX)
        } else {
            text.text
        }

        // 2. Build the output in the format XXX XXX XX XX
        val transformedText = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                when (i) {
                    FIRST_SPACE_POSITION,
                    SECOND_SPACE_POSITION,
                    THIRD_SPACE_POSITION,
                    -> append(' ')
                }
            }
        }

        // 3. OffsetMapping to correctly handle cursor positioning
        //    based on how many spaces have been inserted so far.
        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return when {
                    // 0..2 (first 3 digits)
                    offset <= FIRST_SPACE_POSITION -> offset
                    // 3..5 (next 3 digits)
                    offset <= SECOND_SPACE_POSITION -> offset + ONE
                    // 6..7 (next 2 digits)
                    offset <= THIRD_SPACE_POSITION -> offset + TWO
                    // 8..9 (final 2 digits)
                    offset <= LAST_DIGIT_INDEX -> offset + THREE
                    else -> transformedText.length // if beyond, just move to end
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= FIRST_SPACE_POSITION -> offset
                    offset <= (SECOND_SPACE_POSITION + ONE) -> offset - ONE
                    offset <= (THIRD_SPACE_POSITION + TWO) -> offset - TWO
                    offset <= (LAST_DIGIT_INDEX + THREE) -> offset - THREE
                    else -> LAST_DIGIT_INDEX + ONE // maximum original index + 1
                }
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}

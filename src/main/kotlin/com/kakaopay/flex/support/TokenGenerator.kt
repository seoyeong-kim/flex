package com.kakaopay.flex.support

import org.springframework.data.domain.Range
import java.lang.StringBuilder
import java.security.SecureRandom

class TokenGenerator {
    companion object {
        private const val TOKEN_LENGTH = 3
        private val RANDOM = SecureRandom()
        private val CHARS62 = arrayOf(
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'
        )

        fun generate(): String {
            val stringBuilder = StringBuilder()
            repeat(TOKEN_LENGTH) {
                stringBuilder.append(CHARS62[RANDOM.nextInt(CHARS62.size)])
            }

            return stringBuilder.toString()
        }
    }
}
package com.kakaopay.flex.support

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenGeneratorTest {

    @Test
    fun `토큰은 3글자다`() {
        // given, when
        val token = TokenGenerator.generate()

        // then
        assertEquals(token.length, 3)
    }
}
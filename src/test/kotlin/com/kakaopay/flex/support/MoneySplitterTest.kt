package com.kakaopay.flex.support

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MoneySplitterTest {
    @Test
    fun `입력한돈을 사이즈만큼 나눈다`() {
        // given
        val totalAmount = BigDecimal(100000)

        // when
        val splitMoneys = MoneySplitter.split(totalAmount, 5)

        // then
        assertEquals(splitMoneys.size, 5)
        assertEquals(splitMoneys.reduce(BigDecimal::add), totalAmount)
    }
}
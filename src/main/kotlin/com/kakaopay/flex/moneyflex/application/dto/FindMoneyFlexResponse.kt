package com.kakaopay.flex.moneyflex.application.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class FindMoneyFlexResponse(
        val createdAt: LocalDateTime,
        val amount: BigDecimal,
        val receivedAmount: BigDecimal,
        val receivedMoneys: List<ReceivedMoneyResponse>
) {
    data class ReceivedMoneyResponse (
            val amount: BigDecimal,
            val userId: Long
    )
}


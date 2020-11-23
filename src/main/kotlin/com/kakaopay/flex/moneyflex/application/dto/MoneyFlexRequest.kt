package com.kakaopay.flex.moneyflex.application.dto

import java.math.BigDecimal

data class MoneyFlexRequest (
        val amount: BigDecimal,
        val userCount: Int
)

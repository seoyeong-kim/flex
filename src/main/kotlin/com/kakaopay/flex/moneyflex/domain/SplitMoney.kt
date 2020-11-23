package com.kakaopay.flex.moneyflex.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
class SplitMoney(
        val token: String,
        val amount: BigDecimal,
        @Version
        val version: Int = 0
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    private var used: Boolean = false

    fun markUsed() {
        used = true
    }

    fun isNotUsed(): Boolean {
        return !used
    }
}
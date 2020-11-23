package com.kakaopay.flex.support

import java.math.BigDecimal
import java.util.*

class MoneySplitter {
    companion object {
        private val RANDOM = Random()

        fun split(amount: BigDecimal, size: Int): List<BigDecimal> {
            val splitMoneys = mutableListOf<BigDecimal>()
            var leftMoney = amount
            (0..size - 2).forEach {
                val maxMoney = leftMoney - BigDecimal(size - it -1)
                val pickMoney = BigDecimal(RANDOM.nextInt(maxMoney.intValueExact()) + 1)
                splitMoneys.add(pickMoney)
                leftMoney -= pickMoney
            }

            splitMoneys.add(leftMoney)

            return splitMoneys
        }
    }

}

package com.kakaopay.flex.moneyflex.domain

import com.kakaopay.flex.exception.*
import com.kakaopay.flex.support.MoneySplitter
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("token"))])
class MoneyFlex(
        @Id
        val token: String,
        val userId: Long,
        val amount: BigDecimal,
        val userCount: Int,
        val roomId: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(name = "token")
        var splitMoneys: MutableList<SplitMoney> = mutableListOf()
) {

    companion object {
        private const val READABLE_DAYS: Long = 7L
        private const val RECEIVABLE_MINUTES: Long = 10L
        private const val MIN_USER_COUNT: Int = 3
    }

    init {
        if (userCount < MIN_USER_COUNT) {
            throw UserCountException(MIN_USER_COUNT)
        }
        if (BigDecimal(userCount) >= amount) {
            throw MinMoneyException()
        }

        this.splitMoneys = MoneySplitter.split(amount, userCount)
                .map {
                    SplitMoney(
                            token = token,
                            amount = it,
                            version = 0
                    )
                }
                .toMutableList()
    }

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "token")
    val receivedMoneys: MutableList<ReceivedMoney> = mutableListOf()

    fun receiveMoney(userId: Long, amount: BigDecimal, compareDateTime: LocalDateTime): ReceivedMoney {
        if (isNotReceivable(compareDateTime)) {
            throw MoneyFlexExpiredException()
        }
        if (isOwner(userId)) {
            throw OwnerCanNotParticipateException()
        }
        if (isParticipated(userId)) {
            throw AlreadyParticipatedException()
        }
        if (isFull()) {
            throw MoneyFlexIsFullException()
        }
        val receivedMoney = ReceivedMoney(
                token = this.token,
                userId = userId,
                amount = amount
        )
        receivedMoneys.add(receivedMoney)
        return receivedMoney
    }

    fun selectSplitMoney(token: String, roomId: String, userId: Long): SplitMoney {
        return splitMoneys.firstOrNull { it.isNotUsed() }
                .also { it?.markUsed() }
                ?: throw MoneyFlexIsFullException()
    }

    fun isNotReadable(compareDateTime: LocalDateTime): Boolean {
        val expiredDate = createdAt.plusDays(READABLE_DAYS)
        if (compareDateTime.isAfter(expiredDate)) {
            return true
        }
        return false
    }

    fun isNotOwner(userId: Long): Boolean {
        return !isOwner(userId)
    }

    private fun isNotReceivable(compareDateTime: LocalDateTime): Boolean {
        val expiredDate = createdAt.plusMinutes(RECEIVABLE_MINUTES)
        if (compareDateTime.isAfter(expiredDate)) {
            return true
        }
        return false
    }

    private fun isOwner(userId: Long): Boolean {
        if (this.userId == userId) {
            return true
        }
        return false
    }

    private fun isParticipated(userId: Long): Boolean {
        return receivedMoneys.any { it.userId == userId }
    }

    private fun isFull(): Boolean {
        return receivedMoneys.size >= userCount
    }
}
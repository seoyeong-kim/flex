package com.kakaopay.flex.moneyflex.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceivedMoneyRepository : JpaRepository<ReceivedMoney, Long> {
}
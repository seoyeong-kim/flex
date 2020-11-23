package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class MinMoneyException : BaseException(HttpStatus.FORBIDDEN, "참여금액은 참가인원 당 1원보다 커야합니다.")
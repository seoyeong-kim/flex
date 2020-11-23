package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class MoneyFlexExpiredException : BaseException(HttpStatus.FORBIDDEN, "해당 돈뿌리기는 기간이 만료되었습니다.")

package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class MoneyFlexIsFullException : BaseException(HttpStatus.FORBIDDEN, "돈뿌리기 인원이 마감되었습니다.")
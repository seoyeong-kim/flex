package com.kakaopay.flex.exception

import org.springframework.http.HttpStatus

class MoneyFlexNotFoundException : BaseException(HttpStatus.NOT_FOUND, "대화방에서 돈뿌리기 정보를 찾을 수 없습니다.")
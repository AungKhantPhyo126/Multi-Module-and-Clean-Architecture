package com.critx.shwemiAdmin.dateControllers

import org.threeten.bp.LocalDateTime

fun parseRefreshTokenExpire(expireTime:Long):LocalDateTime{
    val todayTime = LocalDateTime.now()
    return todayTime.plusSeconds(expireTime)
}
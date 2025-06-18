package kr.wooco.woocobe.core.notification.application.buffer

import kr.wooco.woocobe.core.notification.domain.entity.DeviceToken
import kr.wooco.woocobe.core.notification.domain.entity.Notification

data class NotificationJob(
    val notification: Notification,
    val token: DeviceToken,
    var retryCount: Int = 0,
) {
    fun tokenValue(): String = token.token.value
}

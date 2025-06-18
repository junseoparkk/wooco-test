package kr.wooco.woocobe.core.notification.application.port.out

import kr.wooco.woocobe.core.notification.application.buffer.NotificationJob
import kr.wooco.woocobe.core.notification.application.buffer.NotificationSendResult
import kr.wooco.woocobe.core.notification.domain.entity.Notification
import kr.wooco.woocobe.core.notification.domain.vo.Token

interface NotificationSenderPort {
    suspend fun sendNotification(notificationJob: NotificationJob): NotificationSendResult
}

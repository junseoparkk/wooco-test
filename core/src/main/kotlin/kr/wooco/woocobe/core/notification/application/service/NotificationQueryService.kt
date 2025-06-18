package kr.wooco.woocobe.core.notification.application.service

import kr.wooco.woocobe.core.notification.application.port.`in`.ReadAllNotificationUseCase
import kr.wooco.woocobe.core.notification.application.port.`in`.results.NotificationResult
import kr.wooco.woocobe.core.notification.application.port.out.NotificationQueryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationQueryService(
    private val notificationQueryPort: NotificationQueryPort,
) : ReadAllNotificationUseCase {
    @Transactional(readOnly = true)
    override fun readAllNotification(query: ReadAllNotificationUseCase.Query): List<NotificationResult> {
        val notifications = notificationQueryPort.getAllByUserIdWithActive(query.userId)
        return notifications.map { NotificationResult.from(it) }
    }
}

package kr.wooco.woocobe.core.notification.application.handler

import kr.wooco.woocobe.core.coursecomment.domain.event.CourseCommentCreateEvent
import kr.wooco.woocobe.core.notification.application.port.`in`.CreateNotificationUseCase
import kr.wooco.woocobe.core.notification.application.port.`in`.SendNotificationUseCase
import kr.wooco.woocobe.core.notification.domain.vo.NotificationType
import kr.wooco.woocobe.core.plan.domain.event.PlanShareRequestEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class NotificationEventHandler(
    private val createNotificationUseCase: CreateNotificationUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase,
) {
    @TransactionalEventListener
    fun handleCourseCommentCreateEvent(event: CourseCommentCreateEvent) {
        val command = CreateNotificationUseCase.Command(
            userId = event.courseWriterId,
            targetId = event.courseId,
            targetName = event.courseTitle,
            type = NotificationType.COURSE_COMMENT_CREATED.name,
        )
        val notificationId = createNotificationUseCase.createNotification(command)
        val query = SendNotificationUseCase.Query(notificationId)
        sendNotificationUseCase.sendNotification(query)
    }

    @TransactionalEventListener
    fun handlePlanShareRequestEvent(event: PlanShareRequestEvent) {
        val command = CreateNotificationUseCase.Command(
            userId = event.userId,
            targetId = event.planId,
            targetName = event.planTitle,
            type = NotificationType.PLAN_SHARE_REQUEST.name,
        )
        val notificationId = createNotificationUseCase.createNotification(command)
        val query = SendNotificationUseCase.Query(notificationId)
        sendNotificationUseCase.sendNotification(query)
    }
}

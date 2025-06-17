package kr.wooco.woocobe.api.notification

import kr.wooco.woocobe.core.coursecomment.domain.event.CourseCommentCreateEvent
import kr.wooco.woocobe.core.notification.application.port.`in`.CreateNotificationUseCase
import kr.wooco.woocobe.core.notification.application.port.`in`.SendNotificationUseCase
import kr.wooco.woocobe.core.notification.domain.vo.NotificationType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications/test/")
class MockNotificationController(
    private val publisher: ApplicationEventPublisher,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val createNotificationUseCase: CreateNotificationUseCase,
) {
    data class CourseCommentReq(
        val courseId: Long = 12345L,
        val courseWriterId: Long = 12345L,
        val courseTitle: String = "Dummy course",
        val commentWriterId: Long = 1234567890L,
    )

    @PostMapping("/course-comment")
    fun testCourseComment(@RequestBody body: CourseCommentReq): ResponseEntity<Void> {
        publisher.publishEvent(
            CourseCommentCreateEvent(
                courseId = body.courseId,
                courseTitle = body.courseTitle,
                courseWriterId = body.courseWriterId,
                commentWriterId = body.commentWriterId,
            )
        )
        return ResponseEntity.ok().build()
    }

    @GetMapping("/direct-send")
    fun testDirectSend(): ResponseEntity<Void> {
        val notificationId = createNotificationUseCase.createNotification(
            CreateNotificationUseCase.Command(
                userId = 1234567890L,
                targetId = 12345L,
                targetName = "Test course",
                type = NotificationType.COURSE_COMMENT_CREATED.name
            )
        )

        sendNotificationUseCase.sendNotification(
            SendNotificationUseCase.Query(notificationId)
        )
        return ResponseEntity.ok().build()
    }
}

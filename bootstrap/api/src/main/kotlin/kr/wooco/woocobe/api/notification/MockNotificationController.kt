package kr.wooco.woocobe.api.notification

import kr.wooco.woocobe.core.coursecomment.domain.event.CourseCommentCreateEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications/test/")
class MockNotificationController(
    private val publisher: ApplicationEventPublisher,
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
}

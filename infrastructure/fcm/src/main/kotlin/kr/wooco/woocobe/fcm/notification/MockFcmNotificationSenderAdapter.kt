package kr.wooco.woocobe.fcm.notification

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.wooco.woocobe.core.notification.application.buffer.NotificationJob
import kr.wooco.woocobe.core.notification.application.buffer.NotificationSendResult
import kr.wooco.woocobe.core.notification.application.port.out.NotificationSenderPort
import org.springframework.stereotype.Component
import com.google.firebase.messaging.Message
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Component
class MockFcmNotificationSenderAdapter(
    private val webClient: WebClient = WebClient.builder().build()
) : NotificationSenderPort {
    override suspend fun sendNotification(notificationJob: NotificationJob): NotificationSendResult =
        runCatching {
            val body = mapOf(
                "message" to mapOf(
                    "notification" to mapOf(
                        "title" to notificationJob.notification.type.name,
                        "body" to notificationJob.notification.targetName
                    ),
                    "token" to notificationJob.token.token.value
                ),
                "data" to mapOf(
                    "notification_id" to notificationJob.notification.id.toString(),
                    "user_id"         to notificationJob.notification.userId.toString(),
                    "target_id"       to notificationJob.notification.targetId.toString(),
                    "type"            to notificationJob.notification.type.name,
                )
            )
            val response = webClient.post()
                .uri("http://localhost:8081/mock-fcm/send")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block()

            logger.info { "Success to send FCM notification: $response" }
        }.fold(
            onSuccess = { NotificationSendResult.SUCCESS },
            onFailure = {
                logger.error { "Failed to send FCM notification: ${it.message}" }
                NotificationSendResult.FAILED
            },
        )

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}


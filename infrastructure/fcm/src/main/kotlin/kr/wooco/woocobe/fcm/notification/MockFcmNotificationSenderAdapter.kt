package kr.wooco.woocobe.fcm.notification

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.wooco.woocobe.core.notification.application.port.out.NotificationSenderPort
import kr.wooco.woocobe.core.notification.domain.entity.Notification
import kr.wooco.woocobe.core.notification.domain.vo.Token
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class MockFcmNotificationSenderAdapter(
    private val webClient: WebClient = WebClient.builder().build()
) : NotificationSenderPort {

    private val log = KotlinLogging.logger {}

    override fun sendNotification(notification: Notification, tokens: List<Token>) {
        val body = mapOf(
            "message" to mapOf(
                "tokens" to tokens.map { it.value },
                "notification" to mapOf(
                    "title" to notification.type.name,
                    "body"  to notification.targetName
                ),
                "data" to mapOf(
                    "notification_id" to notification.id.toString(),
                    "user_id"         to notification.userId.toString(),
                    "target_id"       to notification.targetId.toString(),
                    "type"            to notification.type.name,
                )
            )
        )

        val resp = webClient.post()
            .uri("http://localhost:8081/mock-fcm/send")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .block()

        if (resp?.statusCode?.is2xxSuccessful != true) {
            log.warn { "FCM-MOCK push failed: ${resp?.statusCode}" }
        }
    }
}

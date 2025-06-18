package kr.wooco.woocobe.core.notification.application.buffer

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kr.wooco.woocobe.core.notification.application.port.out.NotificationSenderPort
import org.springframework.stereotype.Component

@Component
class NotificationJobBuffer(
    private val notificationSenderPort: NotificationSenderPort,
) : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.IO) {
    private val channel = Channel<NotificationJob>(capacity = 1000)

    fun enqueue(job: NotificationJob) = channel.trySend(job)

    init {
        launch {
            val bucket = mutableListOf<NotificationJob>()
            for (job in channel) {
                bucket += job
                logger.info { "[buffer] add notification to bucket: $job" }
                if (bucket.size >= BATCH_SIZE) flush(bucket)
            }
        }
    }

    private suspend fun flush(bucket: MutableList<NotificationJob>) {
        coroutineScope {
            bucket
                .map { async { trySend(job = it) } }
                .awaitAll()
        }
        logger.info { "[buffer] count of notification to send: ${bucket.size}" }
        logger.info { "[buffer] flush all notification job bucket" }
        bucket.clear()
    }

    private suspend fun trySend(job: NotificationJob) {
        val result = notificationSenderPort.sendNotification(job)

        if (result == NotificationSendResult.SUCCESS) return

        job.retryCount++

        if (job.retryCount >= MAX_RETRY_COUNT) {
            // TODO: 실패한 알림 DB 저장
            logger.warn { "[buffer] save to failed notification: $job" }
            return
        }
        enqueue(job)
        logger.info { "[buffer] enqueue notification job: $job" }
    }

    companion object {
        private const val BATCH_SIZE = 2
        private const val MAX_RETRY_COUNT = 3
        private val logger = KotlinLogging.logger {}
    }
}

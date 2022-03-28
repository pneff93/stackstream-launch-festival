import org.apache.logging.log4j.kotlin.logger

fun main() {

    val logger = logger("Kafka Streams")

    val streamsProperties = StreamProperties()
    val streamProcessor = StreamProcessor(streamsProperties)

    streamProcessor.streams.start()

}
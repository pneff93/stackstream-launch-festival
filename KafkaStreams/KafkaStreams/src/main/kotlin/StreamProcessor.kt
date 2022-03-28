import com.kafkaStreamsExample.*
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.*
import org.apache.logging.log4j.kotlin.logger
import java.time.Duration
import kotlin.math.roundToInt

class StreamProcessor(properties: StreamProperties) {

    val streams: KafkaStreams

    private val serdeDataPerValue: SpecificAvroSerde<SensorDataPerValue>
    private val serdePreAggregatedData: SpecificAvroSerde<SensorDataPreAggregation>
    private val serdeAggregatedData: SpecificAvroSerde<SensorDataAggregation>
    private val serdeAggregatedKey: SpecificAvroSerde<SensorDataAggregationKey>

    init {


        serdeDataPerValue = SpecificAvroSerde<SensorDataPerValue>()
        serdeDataPerValue.configure(registryConfig, false)

        serdePreAggregatedData = SpecificAvroSerde<SensorDataPreAggregation>()
        serdePreAggregatedData.configure(registryConfig, false)

        serdeAggregatedData = SpecificAvroSerde<SensorDataAggregation>()
        serdeAggregatedData.configure(registryConfig, false)

        serdeAggregatedKey = SpecificAvroSerde<SensorDataAggregationKey>()
        serdeAggregatedKey.configure(registryConfig, true) // true because it's a key


        logger("Kafka Streams").info(createTopology().describe())
    }

    private fun createTopology(): Topology {

        val processor = StreamsBuilder()


        return processor.build()
    }



    // Fahrenheit -> Celsius
    private fun convertTemperature(event: SensorData): SensorData {
        val convertedValue: List<value_record> = event.getValue().map {
            if (it.getType() == "temperature" && it.getUnit() == "Fahrenheit") {
                it.setValue(((it.getValue() - 32) / 1.8))
                it.setUnit("Celsius")
            }
            it
        }

        event.setValue(convertedValue)
        return event
    }

    // One data point -> list of data points
    private fun splitDataPoints(event: SensorData): List<SensorDataPerValue> {
        return event.getValue().map {
            SensorDataPerValue(
                event.getSensorId(),
                it.getType(),
                it.getValue(),
                it.getUnit(),
                event.getTimestamp()
            )
        }
    }

    // Aggregation (sum & count)
    private fun aggregateEvents(
        value: SensorDataPerValue,
        aggregate: SensorDataPreAggregation
    ): SensorDataPreAggregation {
        return SensorDataPreAggregation(
            aggregate.getSum() + value.getValue(),
            aggregate.getCount() + 1,
            value.getUnit(),
            value.getTimestamp()
        )
    }

    // Calculate average out of sum and count
    private fun calculateAverage(value: SensorDataPreAggregation): SensorDataAggregation {
        return SensorDataAggregation(
            ((value.getSum() / value.getCount()) * 10.0).roundToInt() / 10.0,
            value.getCount(),
            value.getUnit(),
            value.getTimestamp()
        )
    }
}
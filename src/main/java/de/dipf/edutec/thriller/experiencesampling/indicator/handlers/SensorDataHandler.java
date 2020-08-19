package de.dipf.edutec.thriller.experiencesampling.indicator.handlers;

import de.dipf.edutec.thriller.experiencesampling.SensorRecord;
import de.dipf.edutec.thriller.experiencesampling.Stats;
import de.dipf.edutec.thriller.experiencesampling.UserIds;
import de.dipf.edutec.thriller.experiencesampling.indicator.bindings.Bindings;
import de.dipf.edutec.thriller.experiencesampling.indicator.controller.UserIdsDto;
import de.dipf.edutec.thriller.experiencesampling.indicator.model.AccelerometerRecordDto;
import de.dipf.edutec.thriller.experiencesampling.indicator.model.CountSumTimeAverageDto;
import io.confluent.kafka.streams.serdes.avro.PrimitiveAvroSerde;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

@EnableBinding(Bindings.class)
@Component
public class SensorDataHandler {

    private static final Logger log = LogManager.getLogger(SensorDataHandler.class);
    private static final String SCHEMA_REGISTRY_URL_KEY = "schema.registry.url";

    private final SimpMessagingTemplate messagingTemplate;
    public static final String DESTINATION_LINEAR_ACC = "/topic/linear-acceleration";
    public static final String DESTINATION_ACCELEROMETER = "/topic/accelerometer";
    public static final String DESTINATION_GYROSCOPE = "/topic/gyroscope";
    public static final String DESTINATION_LIGHT = "/topic/light";
    public static final String DESTINATION_STATS = "/topic/stats";
    public static final String DESTINATION_USER_IDS = "/topic/user-ids";

    private final Map<String, String> avroSerdeConfig;
    private final PrimitiveAvroSerde<String> stringKeyPrimitiveSerde;
    private String userIdsInDestination;

    public SensorDataHandler(
            SimpMessagingTemplate messagingTemplate,
            @Value("${spring.cloud.stream.kafka.streams.binder.configuration.schema.registry.url}")
                    String schemaRegistryUrl,
            @Value("${spring.cloud.stream.bindings.linear-acceleration.destination}")
                    String userIdsInDestination) {

        avroSerdeConfig = Collections.singletonMap(SCHEMA_REGISTRY_URL_KEY, schemaRegistryUrl);

        stringKeyPrimitiveSerde = new PrimitiveAvroSerde<>();
        stringKeyPrimitiveSerde.configure(avroSerdeConfig, true);

        this.userIdsInDestination = userIdsInDestination;
        this.messagingTemplate = messagingTemplate;
    }

    @StreamListener(Bindings.LINEAR_ACCELERATION)
    public void processLinearAcceleration(KStream<String, SensorRecord> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            String destination = getDestination(DESTINATION_LINEAR_ACC, deserialize(s));
            log.debug("Retrieved message from input binding '" + Bindings.ACCELEROMETER +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });
    }

    @StreamListener(Bindings.ACCELEROMETER)
    public void processAccelerometer(KStream<String, SensorRecord> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            String destination = getDestination(DESTINATION_ACCELEROMETER, deserialize(s));
            log.debug("Retrieved message from input binding '" + Bindings.ACCELEROMETER +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });
    }

    @StreamListener(Bindings.GYROSCOPE)
    public void processGyroscope(KStream<String, SensorRecord> sensorDataStream) {

        sensorDataStream.foreach((s, value) -> {
            String destination = getDestination(DESTINATION_GYROSCOPE, deserialize(s));
            log.debug("Retrieved message from input binding '" + Bindings.GYROSCOPE +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });
    }

    @StreamListener(Bindings.LIGHT)
    public void processLight(KStream<String, SensorRecord> sensorDataStream) {

        sensorDataStream.foreach((s, value) -> {
            String destination = getDestination(DESTINATION_LIGHT, deserialize(s));
            log.debug("Retrieved message from input binding '" + Bindings.LIGHT +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });
    }

    @StreamListener(Bindings.STATS)
    public void processStats(KStream<String, Stats> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            String destination = getDestination(DESTINATION_STATS, deserialize(s));
            log.debug("Retrieved message from input binding '" + Bindings.LIGHT +
                    "', forwarding to destination '" + destination + "'.");

            CountSumTimeAverageDto dto = CountSumTimeAverageDto.builder()
                .count(value.getCount())
                .timeSumSec(value.getTimeSumSec())
                .countPerSecond(value.getCountPerSecond())
                .time(value.getTime())
                .build();
        
            messagingTemplate.convertAndSend(destination, dto);
        });
    }

    @StreamListener(Bindings.USER_IDS_IN)
    public void userIds(KStream<Long, UserIds> sensorDataStream) {

        sensorDataStream
                .groupByKey()
                .reduce((value1, value2) -> value2, Materialized.as("indicator-user-ids"))
                .toStream()
                .foreach((key, value) -> {
                    log.debug("Retrieved message from input binding '" + Bindings.USER_IDS_IN +
                            "', forwarding to destination '" + DESTINATION_USER_IDS + "'.");
                    messagingTemplate.convertAndSend(DESTINATION_USER_IDS,
                            new UserIdsDto(value.getIds(), value.getTime()));
                });
    }

    private void convertAndSend(SensorRecord value, String destination) {
        Float[] values = value.getValues().toArray(new Float[0]);
        Float[] valuesFull = IntStream.range(0, 4).mapToObj(value1 -> {
            if (values.length > value1)
                return values[value1];
            else return 0F;
        }).toArray(Float[]::new);
        AccelerometerRecordDto dto = AccelerometerRecordDto.builder()
                .time(value.getTime())
                .x(valuesFull[0])
                .y(valuesFull[1])
                .z(valuesFull[2])
                .build();

        messagingTemplate.convertAndSend(destination, dto);
    }

    private String deserialize(String s) {
        return stringKeyPrimitiveSerde.deserializer().deserialize(userIdsInDestination, s.getBytes());
    }

    private String getDestination(String type, String userId) {
        return String.format(type + "/%s", userId);
    }
}

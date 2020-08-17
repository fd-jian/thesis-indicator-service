package de.dipf.edutec.thriller.experiencesampling.indicator.handlers;

import de.dipf.edutec.thriller.experiencesampling.indicator.bindings.Bindings;
import de.dipf.edutec.thriller.experiencesampling.indicator.model.AccelerometerRecordDto;
import de.dipf.edutec.thriller.experiencesampling.indicator.model.CountSumTimeAverageDto;
import de.dipf.edutec.thriller.experiencesampling.SensorRecord;
import de.dipf.edutec.thriller.experiencesampling.Stats;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@EnableBinding(Bindings.class)
@Component
@RequiredArgsConstructor
public class SensorDataHandler {

    Logger log = LogManager.getLogger();
    
    private final SimpMessagingTemplate messagingTemplate;

    @StreamListener(Bindings.LINEAR_ACCELERATION)
    public void processLinearAcceleration(KStream<String, SensorRecord> sensorDataStream) {

        // TODO: implement activity recognition logic

        String destination = "/topic/linear-acceleration";
        sensorDataStream.foreach((s, value) -> {
            log.debug("Retrieved message from input binding '" + Bindings.LINEAR_ACCELERATION +
                    "', forwarding to output binding '" + Bindings.STATS + "'.");
            convertAndSend(value, destination);
        });

    }

    @StreamListener(Bindings.ACCELEROMETER)
    public void processAccelerometer(KStream<String, SensorRecord> sensorDataStream) {

        // TODO: implement activity recognition logic

        String destination = "/topic/accelerometer";
        sensorDataStream.foreach((s, value) -> {
            log.debug("Retrieved message from input binding '" + Bindings.ACCELEROMETER +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });

    }

    @StreamListener(Bindings.GYROSCOPE)
    public void processGyroscope(KStream<String, SensorRecord> sensorDataStream) {

        String destination = "/topic/gyroscope";
        sensorDataStream.foreach((s, value) -> {
            log.debug("Retrieved message from input binding '" + Bindings.GYROSCOPE +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
        });

    }

    @StreamListener(Bindings.LIGHT)
    public void processLight(KStream<String, SensorRecord> sensorDataStream) {

        String destination = "/topic/light";
        sensorDataStream.foreach((s, value) -> {
            log.debug("Retrieved message from input binding '" + Bindings.LIGHT +
                    "', forwarding to destination '" + destination + "'.");
            convertAndSend(value, destination);
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

    @StreamListener(Bindings.STATS)
    public void processActivities(KStream<String, Stats> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            log.debug("Retrieved message from input binding '" + Bindings.LINEAR_ACCELERATION +
                    "', forwarding to output binding '" + Bindings.STATS + "'.");

            CountSumTimeAverageDto dto = CountSumTimeAverageDto.builder()
                .count(value.getCount())
                .timeSumSec(value.getTimeSumSec())
                .countPerSecond(value.getCountPerSecond())
                .time(value.getTime())
                .build();
        
            messagingTemplate.convertAndSend("/topic/stats", dto);

        });

    }

}

package com.edutec.indicatorservice.handlers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.edutec.indicatorservice.bindings.Bindings;
import com.edutec.activitydetector.countsum.CountSumTimeAverage;
import com.edutec.activitydetector.model.AccelerometerRecord;
import com.edutec.indicatorservice.model.AccelerometerRecordDto;
import com.edutec.indicatorservice.model.CountSumTimeAverageDto;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@EnableBinding(Bindings.class)
@Component
@RequiredArgsConstructor
public class SensorDataHandler {

    Logger log = LogManager.getLogger();
    
    private final SimpMessagingTemplate messagingTemplate;

    @StreamListener(Bindings.SENSOR_DATA)
    public void processSensordata(KStream<String, AccelerometerRecord> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            log.info("Retrieved message from input binding '" + Bindings.SENSOR_DATA +
                    "', forwarding to output binding '" + Bindings.ACTIVITIES + "'.");
           AccelerometerRecordDto dto = AccelerometerRecordDto.builder()
                    .time(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .format(ZonedDateTime.ofInstant(
                            Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault())
                        ))
                .x(value.getX())
                .y(value.getY())
                .z(value.getZ())
                .build();

            messagingTemplate.convertAndSend("/topic/accelerometer", dto);
        });

    }

    @StreamListener(Bindings.ACTIVITIES)
    public void processActivities(KStream<String, CountSumTimeAverage> sensorDataStream) {

        // TODO: implement activity recognition logic

        sensorDataStream.foreach((s, value) -> {
            log.info("Retrieved message from input binding '" + Bindings.SENSOR_DATA +
                    "', forwarding to output binding '" + Bindings.ACTIVITIES + "'.");

            CountSumTimeAverageDto dto = CountSumTimeAverageDto.builder()
                .count(value.getCount())
                .timeSumSec(value.getTimeSumSec())
                .countPerSecond(value.getCountPerSecond())
                .build();
        
            messagingTemplate.convertAndSend("/topic/stats", dto);

        });

    }

}

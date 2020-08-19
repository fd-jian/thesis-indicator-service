package de.dipf.edutec.thriller.experiencesampling.indicator.bindings;

import de.dipf.edutec.thriller.experiencesampling.SensorRecord;
import de.dipf.edutec.thriller.experiencesampling.Stats;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface Bindings {

    String LINEAR_ACCELERATION = "linear-acceleration";
    String ACCELEROMETER = "accelerometer";
    String GYROSCOPE = "gyroscope";
    String LIGHT = "light";
    String STATS = "activities";
    String USER_IDS_IN = "user-ids-in";

    @Input(LINEAR_ACCELERATION)
    KStream<String, SensorRecord> linearAcceleration();


    @Input(ACCELEROMETER)
    KStream<String, SensorRecord> accelerometer();


    @Input(GYROSCOPE)
    KStream<String, SensorRecord> gyroscope();


    @Input(LIGHT)
    KStream<String, SensorRecord> light();

    @Input(STATS)
    KStream<String, Stats> stats();

    @Input(USER_IDS_IN)
    KStream<String, SensorRecord> userIdsIn();
}

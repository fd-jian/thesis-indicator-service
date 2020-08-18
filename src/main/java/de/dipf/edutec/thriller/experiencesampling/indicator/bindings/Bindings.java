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

    @Input(LINEAR_ACCELERATION)
    // TODO: use user id key
    KStream<String, SensorRecord> linearAcceleration();


    @Input(ACCELEROMETER)
        // TODO: use user id key
    KStream<String, SensorRecord> accelerometer();


    @Input(GYROSCOPE)
        // TODO: use user id key
    KStream<String, SensorRecord> gyroscope();


    @Input(LIGHT)
        // TODO: use user id key
    KStream<String, SensorRecord> light();

    @Input(STATS)
        // TODO: use user id key
    KStream<String, Stats> stats();

}

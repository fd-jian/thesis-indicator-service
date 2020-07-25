package com.edutec.indicatorservice.bindings;

import com.edutec.activitydetector.countsum.CountSumTimeAverage;
import com.edutec.activitydetector.model.AccelerometerRecord;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface Bindings {

    String LINEAR_ACCELERATION = "linear-acceleration";
    String ACCELEROMETER = "accelerometer";
    String GYROSCOPE = "gyroscope";
    String LIGHT = "light";
    String ACTIVITIES = "activities";

    @Input(LINEAR_ACCELERATION)
    // TODO: use user id key
    KStream<String, AccelerometerRecord> linearAccelerometer();


    @Input(ACCELEROMETER)
        // TODO: use user id key
    KStream<String, AccelerometerRecord> accelerometer();


    @Input(GYROSCOPE)
        // TODO: use user id key
    KStream<String, AccelerometerRecord> gyroscope();


    @Input(LIGHT)
        // TODO: use user id key
    KStream<String, AccelerometerRecord> light();

    @Input(ACTIVITIES)
        // TODO: use user id key
    KStream<String, CountSumTimeAverage> activities();

}

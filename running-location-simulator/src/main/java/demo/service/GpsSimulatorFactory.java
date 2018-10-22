package demo.service;

import demo.model.GpsSimulatorRequest;
import demo.model.Point;
import demo.task.LocationSimulator;

import java.util.List;

public interface GpsSimulatorFactory {

    LocationSimulator prepareGpsSimulator(GpsSimulatorRequest gpsSimulatorRequest);

    LocationSimulator prepareGpsSimulator(LocationSimulator gpsSimulator, List<Point> points);
}

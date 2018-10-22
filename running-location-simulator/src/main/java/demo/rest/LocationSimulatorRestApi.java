package demo.rest;

import demo.service.GpsSimulatorFactory;
import demo.service.PathService;
import demo.task.LocationSimulatorInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LocationSimulatorRestApi {

    @Autowired
    private GpsSimulatorFactory gpsSimulatorFactory;

    @Autowired
    //here we need to create a PathService
    private PathService pathService;

    @Autowired
    //here we need to add a Spring Bean in SimulationServiceApplication class for AsyncTaskExecutor.
    private AsyncTaskExecutor taskExecutor;

    @RequestMapping("/simulation")
    public List<LocationSimulatorInstance> simulation() {

    }

    @RequestMapping("/cancel")
    public int cancel() {

    }
}

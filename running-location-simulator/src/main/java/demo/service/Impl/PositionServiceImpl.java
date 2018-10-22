package demo.service.Impl;

import demo.model.CurrentPosition;
import demo.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PositionServiceImpl implements PositionService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${com.bo.running.location.distribution}")
    String runningLocationDistribution;

    // private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);
    @Override
    public void processPositionInfo(long id, CurrentPosition currentPosition,
                                    boolean sendPositionToDistributionService) {


        if (sendPositionToDistributionService) {
            log.info(String.format("Thread %d Simulator is calling distribution REST API",
                    Thread.currentThread().getId()));
            this.restTemplate.postForLocation(runningLocationDistribution +"/", currentPosition);
        }
    }
}

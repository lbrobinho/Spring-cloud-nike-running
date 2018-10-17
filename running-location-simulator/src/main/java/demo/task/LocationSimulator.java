package demo.task;

import demo.model.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationSimulator implements Runnable{

    private long id;

    // flag for cancelling the simulation
    private AtomicBoolean cancel = new AtomicBoolean();

    private double speedInMps;

    // determine whether the runner should continue to move or not.
    // Say, if the runner has already got to his destination, shoudMove = false.

    private boolean shouldMove;
    private boolean exportPositionsToMessaging = true;
    private Integer reportInterval=500; // in ms

    private PositionInfo currentPosition = null;

    private List<Leg> legs;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String runningId;
    private Point startPoint;
    private Date executionStartTime;
    private MedicalInfo medicalInfo;

    public LocationSimulator(GpsSimulatorRequest gpsSimulatorRequest) {
        this.shouldMove = gpsSimulatorRequest.isMove();
        this.exportPositionsToMessaging = gpsSimulatorRequest.isExportPositionsToMessaging();
        this.setSpeed(gpsSimulatorRequest.getSpeed());
        this.reportInterval = gpsSimulatorRequest.getReportInterval();
        this.runningId = gpsSimulatorRequest.getRunningId();
        this.runnerStatus = gpsSimulatorRequest.getRunnerStatus();
        this.medicalInfo = gpsSimulatorRequest.getMedicalInfo();
    }

    public void setSpeed(double speed) {
        this.speedInMps = speed;
    }

    @Override
    public void run() {

        try {

            executionStartTime = new Date();
            if (cancel.get()) {
                destroy();
                return;
            }

            while (!Thread.interrupted()) {
                long startTime = new Date().getTime();

                if (currentPosition != null) {
                    if (shouldMove) {
                        moveRunningLocation();
                        currentPosition.setSpeed(speedInMps);
                    } else {
                        currentPosition.setSpeed(0.0);
                    }

                    currentPosition.setRunnerStatus(this.runnerStatus);

                    final MedicalInfo medicalInfo;

                    switch (this.runnerStatus) {
                        case SUPPLY_NOW:
                        case SUPPLY_SOON:
                        case STOP_NOW:
                            medicalInfo = this.medicalInfo;
                            break;
                        default:
                            medicalInfo = null;
                            break;
                    }

                    final CurrentPosition currentPosition = new CurrentPosition(
                            this.currentPosition.getRunningId(),
                            new Point(this.currentPosition.getPosition().getLatitude(),
                                    this.currentPosition.getPosition().getLongitude()),
                            this.currentPosition.getRunnerStatus(),
                            this.currentPosition.getSpeed(),
                            this.currentPosition.getLeg().getHeading(),
                            medicalInfo
                    );
                }

                // wait until next Position report
                sleep(startTime);
            }



        } catch (InterruptedException ie) {
            destroy();
            return;
        }
    }

    void destroy(){
        currentPosition = null;
    }

    private void moveRunningLocation() {

    }


    private void sleep(long startTime) throws InterruptedException{
        long endTime = new Date().getTime();
        long elaspedTime = endTime - startTime;
        long sleepTime = reportInterval - elaspedTime > 0 ? reportInterval : elaspedTime;
        Thread.sleep(sleepTime);
    }
}

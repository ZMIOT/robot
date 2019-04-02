package ldu.guofeng.imdemo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/3/31.
 */

public class TrainInfo {
    private String deptDate;
    private String trainCode;
    private String deptTime;
    private String arrTime;
    private String deptStationName;
    private String arrStationName;
    private String arrDate;
    private String runTime;
    private List<TrainTicket> trainTickets;

    public TrainInfo(String deptDate, String trainCode, String deptTime, String arrTime, String deptStationName, String arrStationName, String arrDate, String runTime, List<TrainTicket> trainTickets) {
        this.deptDate = deptDate;
        this.trainCode = trainCode;
        this.deptTime = deptTime;
        this.arrTime = arrTime;
        this.deptStationName = deptStationName;
        this.arrStationName = arrStationName;
        this.arrDate = arrDate;
        this.runTime = runTime;
        this.trainTickets = trainTickets;
    }

    public String getDeptDate() {
        return deptDate;
    }

    public void setDeptDate(String deptDate) {
        this.deptDate = deptDate;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getDeptTime() {
        return deptTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDeptStationName() {
        return deptStationName;
    }

    public void setDeptStationName(String deptStationName) {
        this.deptStationName = deptStationName;
    }

    public String getArrStationName() {
        return arrStationName;
    }

    public void setArrStationName(String arrStationName) {
        this.arrStationName = arrStationName;
    }

    public String getArrDate() {
        return arrDate;
    }

    public void setArrDate(String arrDate) {
        this.arrDate = arrDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public List<TrainTicket> getTrainTickets() {
        return trainTickets;
    }

    public void setTrainTickets(List<TrainTicket> trainTickets) {
        this.trainTickets = trainTickets;
    }
}

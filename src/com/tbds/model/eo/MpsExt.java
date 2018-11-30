/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.model.eo;

import java.io.Serializable;

/**
 *
 * @author totan
 */
public class MpsExt implements Serializable {
    private String trainName = null;
    private String aPointIP = null;
    private String bPointIP = null;
    private String aPointPort = null;
    private String bPointPort = null;
    private int aPointStatus = 0;
    private int bPointStatus = 0;
    private long aPointDuration = 0;
    private long bPointDuration = 0;
    private String aPointCheckTime = null;
    private String bPointCheckTime = null; 

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getAPointIP() {
        return aPointIP;
    }

    public void setAPointIP(String aPointIP) {
        this.aPointIP = aPointIP;
    }

    public String getBPointIP() {
        return bPointIP;
    }

    public void setBPointIP(String bPointIP) {
        this.bPointIP = bPointIP;
    }

    public String getAPointPort() {
        return aPointPort;
    }

    public void setAPointPort(String aPointPort) {
        this.aPointPort = aPointPort;
    }

    public String getBPointPort() {
        return bPointPort;
    }

    public void setBPointPort(String bPointPort) {
        this.bPointPort = bPointPort;
    }


    public int getAPointStatus() {
        return aPointStatus;
    }

    public void setAPointStatus(int aPointStatus) {
        this.aPointStatus = aPointStatus;
    }

    public int getBPointStatus() {
        return bPointStatus;
    }

    public void setBPointStatus(int bPointStatus) {
        this.bPointStatus = bPointStatus;
    }

    public long getAPointDuration() {
        return aPointDuration;
    }

    public void setAPointDuration(long aPointDuration) {
        this.aPointDuration = aPointDuration;
    }

    public long getBPointDuration() {
        return bPointDuration;
    }

    public void setBPointDuration(long bPointDuration) {
        this.bPointDuration = bPointDuration;
    }

    public String getAPointCheckTime() {
        return aPointCheckTime;
    }

    public void setAPointCheckTime(String aPointCheckTime) {
        this.aPointCheckTime = aPointCheckTime;
    }

    public String getBPointCheckTime() {
        return bPointCheckTime;
    }

    public void setBPointCheckTime(String bPointCheckTime) {
        this.bPointCheckTime = bPointCheckTime;
    }

    
    
    
}

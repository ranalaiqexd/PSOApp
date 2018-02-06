package com.newinspections.pso.dto;

/**
 * Created by mobiweb on 28/9/16.
 */
public class SurveyDTO {

    //status -1=select, 1=yes, 2=no, 3=N/A

    private String message="",status="",key="";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

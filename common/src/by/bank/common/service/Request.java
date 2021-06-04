package by.bank.common.service;

import java.io.Serializable;

public abstract class Request implements Serializable {

    protected RequestType requestType;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}

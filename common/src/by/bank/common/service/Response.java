package by.bank.common.service;

import java.io.Serializable;

public abstract class Response implements Serializable {

    protected ResponseType responseType;

    public Response(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}

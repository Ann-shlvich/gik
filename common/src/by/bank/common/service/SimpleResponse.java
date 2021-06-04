package by.bank.common.service;

public class SimpleResponse extends Response {

    protected String message;

    public SimpleResponse(ResponseType responseType) {
        super(responseType);
    }

    public SimpleResponse(ResponseType responseType, String message) {
        super(responseType);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

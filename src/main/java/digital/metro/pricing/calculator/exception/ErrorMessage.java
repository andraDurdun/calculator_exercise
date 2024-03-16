package digital.metro.pricing.calculator.exception;

import java.util.Date;

public class ErrorMessage {

    private final Date timestamp;
    private final int status;
    private final String error;

    public ErrorMessage(Date timestamp, int status, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}

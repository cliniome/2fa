package sa.com.nadec.milk.utils;

import java.io.Serializable;

/**
 * Created by snouto on 27/11/15.
 */
public class VerificationResult implements Serializable {

    private boolean result;
    private String reason;

    public VerificationResult(){}
    public VerificationResult(boolean result , String reason){
        this.setResult(result);
        this.setReason(reason);
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}


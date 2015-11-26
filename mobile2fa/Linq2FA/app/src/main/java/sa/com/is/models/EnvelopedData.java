package sa.com.is.models;

import java.io.Serializable;

/**
 * Created by snouto on 26/11/15.
 */
public class EnvelopedData implements Serializable {

    private String seed;
    private String signature;
    private int seconds;
    private String key;
    private String binPassword;



    public EnvelopedData(){}
    public EnvelopedData(String seed , String signature , int seconds,String key)
    {
        this.setSeed(seed);
        this.setSignature(signature);
        this.setSeconds(seconds);
        this.setKey(key);
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBinPassword() {
        return binPassword;
    }

    public void setBinPassword(String binPassword) {
        this.binPassword = binPassword;
    }
}

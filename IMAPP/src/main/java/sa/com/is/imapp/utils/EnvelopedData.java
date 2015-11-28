package sa.com.is.imapp.utils;

import sa.com.is.imapp.db.models.Account;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by snouto on 25/11/15.
 */
public class EnvelopedData implements Serializable {

    private String seed;
    private int seconds;
    private String key;
    private int numDigits;
    private String accountName;

    public EnvelopedData(){}
    public EnvelopedData(String seed  , int seconds,String key,int numDigits)
    {
        this.setSeed(seed);

        this.setSeconds(seconds);
        this.setKey(key);
        this.setNumDigits(numDigits);
    }



    public Account toAccount(SystemConfiguration configuration){

        Account dbAccount = new Account();
        dbAccount.setDeleted(false);
        dbAccount.setId(UUID.randomUUID().toString());
        dbAccount.setNumOfDigits(this.getNumDigits());
        dbAccount.setNumOfSeconds(this.getSeconds());
        dbAccount.setSeedValue(this.getSeed());
        dbAccount.setSymmetricAlgorithmName(configuration.getSymmetricKeyAlgorithm());
        dbAccount.setSymmetricKey(this.getKey());
        return dbAccount;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
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

    public int getNumDigits() {
        return numDigits;
    }

    public void setNumDigits(int numDigits) {
        this.numDigits = numDigits;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

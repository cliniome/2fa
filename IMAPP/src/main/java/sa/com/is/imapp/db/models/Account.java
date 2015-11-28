package sa.com.is.imapp.db.models;

import sa.com.is.imapp.utils.EnvelopedData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by snouto on 28/11/15.
 */
@Entity(name="Account")
@Table(name = "Accounts")
public class Account implements Serializable {


    @Id
    @Column(name="ID")
    private String id;
    @Column(name="SymmetricKey",nullable = false)
    private String symmetricKey;
    @Column(name="NumOfSeconds",nullable = false)
    private int numOfSeconds;
    @Column(name="NumOfDigits",nullable = false)
    private int numOfDigits;
    @Column(name="SymmetricAlgorithmName",nullable = false)
    private String symmetricAlgorithmName;
    @Column(name="SeedValue",nullable = false)
    private String seedValue;
    @Column(name="Deleted",nullable = false)
    private boolean deleted;
    @Column(name="AccountName",nullable = false)
    private String accountName;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    public int getNumOfSeconds() {
        return numOfSeconds;
    }

    public void setNumOfSeconds(int numOfSeconds) {
        this.numOfSeconds = numOfSeconds;
    }

    public int getNumOfDigits() {
        return numOfDigits;
    }

    public void setNumOfDigits(int numOfDigits) {
        this.numOfDigits = numOfDigits;
    }

    public String getSymmetricAlgorithmName() {
        return symmetricAlgorithmName;
    }

    public void setSymmetricAlgorithmName(String symmetricAlgorithmName) {
        this.symmetricAlgorithmName = symmetricAlgorithmName;
    }

    public String getSeedValue() {
        return seedValue;
    }

    public void setSeedValue(String seedValue) {
        this.seedValue = seedValue;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public EnvelopedData toEnvelopedData(){

        EnvelopedData data = new EnvelopedData();
        data.setKey(this.getSymmetricKey());
        data.setNumDigits(this.getNumOfDigits());
        data.setSeconds(this.getNumOfSeconds());
        data.setSeed(this.getSeedValue());
        data.setAccountName(this.getAccountName());
        return data;


    }


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

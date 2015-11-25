package sa.com.is.imapp.utils;

/**
 * Created by snouto on 25/11/15.
 */
public class SystemConfiguration {

    private String privateKeyLocation;
    private String publicKeyLocation;
    private int numberOfSeconds;
    private String initialSeed;
    private String encryptionModeType;
    private String SymmetricKeyAlgorithm;

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }

    public void setPrivateKeyLocation(String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
    }

    public String getPublicKeyLocation() {
        return publicKeyLocation;
    }

    public void setPublicKeyLocation(String publicKeyLocation) {
        this.publicKeyLocation = publicKeyLocation;
    }

    public int getNumberOfSeconds() {
        return numberOfSeconds;
    }

    public void setNumberOfSeconds(int numberOfSeconds) {
        this.numberOfSeconds = numberOfSeconds;
    }

    public String getInitialSeed() {
        return initialSeed;
    }

    public void setInitialSeed(String initialSeed) {
        this.initialSeed = initialSeed;
    }

    public String getEncryptionModeType() {
        return encryptionModeType;
    }

    public void setEncryptionModeType(String encryptionModeType) {
        this.encryptionModeType = encryptionModeType;
    }

    public String getSymmetricKeyAlgorithm() {
        return SymmetricKeyAlgorithm;
    }

    public void setSymmetricKeyAlgorithm(String symmetricKeyAlgorithm) {
        SymmetricKeyAlgorithm = symmetricKeyAlgorithm;
    }
}

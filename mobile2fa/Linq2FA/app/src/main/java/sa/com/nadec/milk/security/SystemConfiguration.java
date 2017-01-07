package sa.com.nadec.milk.security;

import android.content.Context;

/**
 * Created by snouto on 26/11/15.
 */
public class SystemConfiguration {


    private Context context;
    private String privateKeyName;
    private String publicKeyName;
    private String symmetricAlgorithm;
    private String encdecryptMode;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPrivateKeyName() {
        return privateKeyName;
    }

    public void setPrivateKeyName(String privateKeyName) {
        this.privateKeyName = privateKeyName;
    }

    public String getPublicKeyName() {
        return publicKeyName;
    }

    public void setPublicKeyName(String publicKeyName) {
        this.publicKeyName = publicKeyName;
    }

    public String getSymmetricAlgorithm() {
        return symmetricAlgorithm;
    }

    public void setSymmetricAlgorithm(String symmetricAlgorithm) {
        this.symmetricAlgorithm = symmetricAlgorithm;
    }

    public String getEncdecryptMode() {
        return encdecryptMode;
    }

    public void setEncdecryptMode(String encdecryptMode) {
        this.encdecryptMode = encdecryptMode;
    }
}


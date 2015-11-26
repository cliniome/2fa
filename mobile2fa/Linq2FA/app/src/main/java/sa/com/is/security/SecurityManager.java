package sa.com.is.security;

/**
 * Created by snouto on 26/11/15.
 */

public interface SecurityManager {

    public byte[] encryptEnvelope(byte[] envelope);
    public byte[] generateSignature(byte[] data) throws Exception;
    public byte[] generateKey() throws Exception;
    public byte[] encryptSymmetricKey(byte[] key) throws Exception;


}



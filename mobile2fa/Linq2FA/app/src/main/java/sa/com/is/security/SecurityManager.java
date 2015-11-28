package sa.com.is.security;

/**
 * Created by snouto on 26/11/15.
 */

public interface SecurityManager {


    public boolean verifySignature(byte[] signatureData , byte[] dataToVerify) throws Exception;
    public byte[] decryptSymmetricKey(byte[] key) throws Exception;
    public byte[] decryptEnvelope(byte[] data , byte[] symmetricKey) throws Exception;


}



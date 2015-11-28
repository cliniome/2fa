package sa.com.is.imapp.utils;

import java.security.PublicKey;

/**
 * Created by snouto on 25/11/15.
 */
public interface SecurityManager {

    public byte[] encryptEnvelope(byte[] envelope,byte[] key);
    public byte[] generateSignature(byte[] data) throws Exception;
    public byte[] generateKey() throws Exception;
    public byte[] encryptSymmetricKey(byte[] key) throws Exception;


}

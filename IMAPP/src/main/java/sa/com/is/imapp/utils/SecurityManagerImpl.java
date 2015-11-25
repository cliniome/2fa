package sa.com.is.imapp.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by snouto on 25/11/15.
 */
public class SecurityManagerImpl implements SecurityManager {

    private  byte[] public_Key = null;

    private SystemConfiguration configuration;

    public SecurityManagerImpl(){

        configuration = (SystemConfiguration)SpringUtils.getSpringBean("systemConfiguration");
        loadPublicKey();
    }

    private void loadPublicKey() {

        try
        {
            InputStream publicKeyStream = new FileInputStream(new File(configuration.getPublicKeyLocation()));

            if(publicKeyStream != null)
            {
                public_Key = new byte[publicKeyStream.available()];
                publicKeyStream.read(public_Key);
                publicKeyStream.close();
            }

        }catch (Exception s)
        {
            s.printStackTrace();
        }

    }

    private PublicKey readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(this.public_Key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    private PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readPrivateKeyBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private byte[] readPrivateKeyBytes() throws IOException {


        InputStream privateInputStream = new FileInputStream(new File(configuration.getPrivateKeyLocation()));
        byte[] privateKey = new byte[privateInputStream.available()];
        privateInputStream.read(privateKey);
        privateInputStream.close();

        return privateKey;

    }


    public byte[] encryptEnvelope(byte[] envelope) {
        try
        {
            Cipher cipher = Cipher.getInstance(configuration.getSymmetricKeyAlgorithm());
            byte[] secretKey = this.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(secretKey,configuration.getSymmetricKeyAlgorithm()));
            return cipher.doFinal(envelope);


        }catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }
    }

    public byte[] generateSignature(byte[] data) throws Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        if(rsa == null) throw new Exception("No Security Provider is available for RSA Algorithm");
        rsa.initSign(readPrivateKey());
        rsa.update(data);
        return rsa.sign();

    }

    public byte[] generateKey() throws Exception {

        KeyGenerator generator = KeyGenerator.getInstance(configuration.getSymmetricKeyAlgorithm());

        SecretKey secretKey = generator.generateKey();
        byte[] encodedBytes = secretKey.getEncoded();
        //decrypt the key
        return encodedBytes;
    }

    public byte[] encryptSymmetricKey(byte[] key) throws Exception {

        Cipher cipher = Cipher.getInstance(configuration.getEncryptionModeType());
        PublicKey pubkey = this.readPublicKey();
        cipher.init(Cipher.ENCRYPT_MODE,pubkey);
        return cipher.doFinal(key);
    }
}

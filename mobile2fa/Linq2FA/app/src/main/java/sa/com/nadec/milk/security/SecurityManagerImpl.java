package sa.com.nadec.milk.security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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

    public SecurityManagerImpl(SystemConfiguration configuration){

        this.configuration = configuration;
        loadPublicKey();
    }

    private void loadPublicKey() {

        try
        {

            InputStream publicKeyStream = configuration.getContext().getAssets().open(
                    configuration.getPublicKeyName());

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



        InputStream privateInputStream = configuration.getContext().getAssets()
                .open(configuration.getPrivateKeyName());
        byte[] privateKey = new byte[privateInputStream.available()];
        privateInputStream.read(privateKey);
        privateInputStream.close();

        return privateKey;

    }

    @Override
    public boolean verifySignature(byte[] signatureData , byte[] dataToVerify) throws Exception {

        try
        {
            Signature signature = Signature.getInstance("SHA1withRSA","BC");
            signature.initVerify(readPublicKey());
            signature.update(dataToVerify);
            return signature.verify(signatureData);


        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }


    }

    @Override
    public byte[] decryptSymmetricKey(byte[] key) throws Exception {
        try
        {

            Cipher cipher = Cipher.getInstance(configuration.getEncdecryptMode());
            cipher.init(Cipher.DECRYPT_MODE, readPrivateKey());
            cipher.update(key);
            return cipher.doFinal();

        }catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] decryptEnvelope(byte[] data , byte[] key) throws Exception {
        try
        {
            Cipher cipher = Cipher.getInstance(configuration.getSymmetricAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(key,configuration.getSymmetricAlgorithm()));
            return cipher.doFinal(data);

        }catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }
    }


    static {

        Security.insertProviderAt(new BouncyCastleProvider(),1);
    }
}


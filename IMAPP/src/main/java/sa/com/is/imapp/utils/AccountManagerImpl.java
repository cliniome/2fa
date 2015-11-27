package sa.com.is.imapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.primefaces.util.Base64;
import sa.com.is.imapp.models.Account;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by snouto on 25/11/15.
 */
public class AccountManagerImpl implements AccountManager {

    private static final String CHARSET_ENCODING ="UTF-8";


    private SecurityManager securityManager;

    private SystemConfiguration configuration;


    public AccountManagerImpl(){
        securityManager = new SecurityManagerImpl();
        configuration = (SystemConfiguration) SpringUtils.getSpringBean("systemConfiguration");
    }


    public String generateQR(Account account) throws Exception {

        if(securityManager == null) throw new Exception("There was a problem initializing the security manager");

        /*
          We will generate the QR barcode Based on the encrypted data
          The steps are as follow :
          1.Generate the seed for that user
          1.Create an Json Envelope based on the account details
          2.Convert Json String into bytes array.
          3. Encrypt the bytes array
         */
        String seed = getSeed(account);
        byte[] payload = generatePayload(seed, configuration.getNumberOfSeconds(),configuration.getNumDigits());
        //Sign the seed value
        byte[] signature = securityManager.generateSignature(payload);
        //Base64 Encode the signature
        String encodedSignature = Base64.encodeToString(signature,false);
        byte[] encryptedSymmetricKey = securityManager.generateKey();
        String encodedSymmetricKey = Base64.encodeToString(encryptedSymmetricKey, false);
        //Create an enveloped Data
        EnvelopedData envelopedData = new EnvelopedData();
        envelopedData.setKey(Base64.encodeToString(securityManager.encryptSymmetricKey(encryptedSymmetricKey),false));
        envelopedData.setSeconds(configuration.getNumberOfSeconds());
        envelopedData.setSeed(Base64.encodeToString(securityManager.encryptEnvelope(seed.getBytes(CHARSET_ENCODING)),false));
        envelopedData.setSignature(Base64.encodeToString(signature,false));
        envelopedData.setNumDigits(configuration.getNumDigits());
        //Convert that into Json
        Gson gson = new GsonBuilder().create();
        //The Json String representation of the enveloped Data
        String jsonString = gson.toJson(envelopedData);
        //Encrypt that
        return jsonString;

    }

    public String compress(String QRPayload) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        byte[] payload = QRPayload.getBytes(CHARSET_ENCODING);
        gzipOutputStream.write(payload);
        gzipOutputStream.flush();
        gzipOutputStream.close();

        return Base64.encodeToString(outputStream.toByteArray(),false);

    }

    private byte[] generatePayload(String seed, int numberOfSeconds , int numDigits) throws Exception {
        //get the bytes for the seed
        byte[] seedBytes = seed.getBytes(CHARSET_ENCODING);
        byte[] newBytes = new byte[seedBytes.length];

        if(seedBytes != null && seedBytes.length > 0){


            for(int i=0;i<seedBytes.length;i++){

                newBytes[i] = new Integer((seedBytes[i] ^ numberOfSeconds) ^ numDigits).byteValue();
            }
        }else throw new Exception("There was a problem generating the payload for the seed , the seed is empty");

        return newBytes;
    }

    private String getSeed(Account account){

        //Get the temp Hashcode by adding the absolute value of the Fixed OTP Seed Value + The absolute value
        // of the account UserName
        int hashcode = hashCode(configuration.getInitialSeed()) + hashCode(account.getUserName());

        //Append the OTP_seed with the String representation of the generated hashcode
        String finalSeed = configuration.getInitialSeed() + String.valueOf(hashcode);

        return finalSeed;
    }

    private int hashCode(String value)
    {
        int hashcode = value.hashCode();
        return Math.abs(hashcode);
    }
}

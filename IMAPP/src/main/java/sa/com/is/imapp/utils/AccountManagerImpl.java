package sa.com.is.imapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.primefaces.util.Base64;
import sa.com.is.imapp.db.beans.AccountsDAO;
import sa.com.is.imapp.models.Account;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.zip.GZIPOutputStream;

/**
 * Created by snouto on 25/11/15.
 */
public class AccountManagerImpl implements AccountManager {

    private static final String CHARSET_ENCODING ="UTF-8";


    private SecurityManager securityManager;

    private SystemConfiguration configuration;

    private AccountsDAO accountsDAO;


    public AccountManagerImpl(AccountsDAO accountsDAO){
        securityManager = new SecurityManagerImpl();
        configuration = (SystemConfiguration) SpringUtils.getSpringBean("systemConfiguration");
        this.accountsDAO = accountsDAO;
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
        //Generate the payload that will be signed
        byte[] payload = generatePayload(seed, configuration.getNumberOfSeconds(), configuration.getNumDigits());
        //Generate the symmetric key
        byte[] symmetricKey = securityManager.generateKey();
        //byte[] encryptedSymmetricKey = securityManager.encryptSymmetricKey(symmetricKey);
        //Create the enveloped Data
        EnvelopedData envelopedData = new EnvelopedData();
        envelopedData.setKey(Base64.encodeToString(symmetricKey,false));
        envelopedData.setSeconds(configuration.getNumberOfSeconds());
        byte[] encryptedSeed = securityManager.encryptEnvelope(seed.getBytes(CHARSET_ENCODING),symmetricKey);
        System.out.println("seed Value is : " + seed);
        envelopedData.setSeed(Base64.encodeToString(seed.getBytes(CHARSET_ENCODING), false));
        envelopedData.setNumDigits(configuration.getNumDigits());

        //save the following into the database
        sa.com.is.imapp.db.models.Account dbAccount = envelopedData.toAccount(configuration);
        dbAccount.setAccountName(account.getUserName());
        boolean result = this.accountsDAO.insertAccount(dbAccount);

        String redirectUrl = null;

        if(result)
        {
           redirectUrl = String.format("http://%s:%s/imapp/%s/activate?id=%s",configuration.getServerAddress(),configuration.getPort(),
                   configuration.getRestPath(),dbAccount.getId());
        }



        //Encrypt that
        return redirectUrl;

    }

    public String compress(String QRPayload) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        byte[] payload = QRPayload.getBytes(CHARSET_ENCODING);
        gzipOutputStream.write(payload);
        gzipOutputStream.flush();
        gzipOutputStream.close();
        //Base64.encodeToString(outputStream.toByteArray(),false);
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


        String finalSeed = toHex(account.getUserName());

        return finalSeed;
    }

    public String toHex(String arg) {
        try {
            return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return "";
        }
    }



    private int hashCode(String value)
    {
        int hashcode = value.hashCode();
        return Math.abs(hashcode);
    }
}

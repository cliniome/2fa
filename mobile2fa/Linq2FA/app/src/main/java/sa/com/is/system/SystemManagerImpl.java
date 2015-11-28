package sa.com.is.system;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import sa.com.is.db.DatabaseManager;
import sa.com.is.models.EnvelopedData;
import sa.com.is.security.SecurityManager;
import sa.com.is.security.SecurityManagerImpl;
import sa.com.is.security.SystemConfiguration;
import sa.com.is.utils.VerificationResult;

/**
 * Created by snouto on 26/11/15.
 */
public class SystemManagerImpl implements SystemManager {

    private SecurityManager securityManager;
    private DatabaseManager databaseManager;

    private SystemConfiguration configuration;

    public SystemManagerImpl(SystemConfiguration configuration)
    {
        this.setConfiguration(configuration);
        initSystem();
    }

    private void initSystem() {
        try
        {
            this.setSecurityManager(new SecurityManagerImpl(getConfiguration()));
            this.setDatabaseManager(new DatabaseManager(getConfiguration().getContext()));

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }


    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public SystemConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public VerificationResult verifyContents(String contents, EnvelopedData data) {

        VerificationResult result = new VerificationResult();

        try
        {

            if(contents == null ||contents.length() <=0)
            {
                result.setReason("System Manager : Payload is null ");
                result.setResult(false);
                throw new Exception("System Manager : Payload is null");
            }

            //Convert into bytes array
            byte[] bytesContents = contents.getBytes("UTF-8");
            //Decode it
            bytesContents = Base64.decode(bytesContents,Base64.DEFAULT);
            //Decompress it
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesContents);
            GZIPInputStream inputStream = new GZIPInputStream(byteArrayInputStream);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);
            StringBuffer buffer = new StringBuffer();

            String line = null;

            while((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }



            //Convert them into json
            String jsonContents = buffer.toString();

            //Convert them into Enveloped Data
            Gson jsonHandler = new GsonBuilder().create();
            EnvelopedData tempEnveloped = jsonHandler.fromJson(jsonContents,EnvelopedData.class);

            if(tempEnveloped != null)
            {
                //First Decrypt the symmetric key using your private key
                byte[] decryptedKey = decryptKey(tempEnveloped.getKey(),tempEnveloped);
                //Second , Decrypt the seed using the symmetric key
                byte[] decodedSeedBytes = tempEnveloped.getSeed().getBytes("UTF-8");
                decodedSeedBytes = Base64.decode(decodedSeedBytes,Base64.DEFAULT);
                decodedSeedBytes = getSecurityManager().decryptEnvelope(decodedSeedBytes,decryptedKey);
                //Get the seed value
                String finalSeedValue = new String(decodedSeedBytes);
                //Third , generate the payload data to verify
                byte[] payload = generatePayload(finalSeedValue,tempEnveloped.getSeconds(),tempEnveloped.getNumDigits());
                //Fourth , Get the signature data
                byte[] signatureData = tempEnveloped.getSignature().getBytes("UTF-8");
                signatureData = Base64.decode(signatureData,Base64.DEFAULT);
                boolean verified = getSecurityManager().verifySignature(signatureData,payload);

                //Fifth , If the data was verified correctly now pass the information to the application
                if(verified)
                {
                    result.setResult(verified);
                    result.setReason("Verification Was Successful");

                    data.setBinPassword(tempEnveloped.getBinPassword());
                    data.setSeed(finalSeedValue);
                    data.setSeconds(tempEnveloped.getSeconds());
                    data.setNumDigits(tempEnveloped.getNumDigits());
                }else
                {
                    result.setReason("The Signature data does not match the contents , Verification was not successful");
                    result.setResult(verified);
                }


            }else throw new Exception("System Manager : There was a problem deserializing the encapsulated data");




        }catch (Exception s)
        {
            s.printStackTrace();
            result.setReason(s.getMessage());
        }
        finally {

            return result;
        }
    }

    @Override
    public boolean activatedAccount() {

        try
        {
            return getDatabaseManager().isAccountActivated();

        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isPinPasswordCorrect(String password) {

        try
        {

            return getDatabaseManager().verifyPinPassword(password);

        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }
    }

    private byte[] generatePayload(String seed, int numberOfSeconds , int numDigits) throws Exception {
        //get the bytes for the seed
        byte[] seedBytes = seed.getBytes("UTF-8");
        byte[] newBytes = new byte[seedBytes.length];

        if(seedBytes != null && seedBytes.length > 0){


            for(int i=0;i<seedBytes.length;i++){

                newBytes[i] = new Integer((seedBytes[i] ^ numberOfSeconds) ^ numDigits).byteValue();
            }
        }else throw new Exception("There was a problem generating the payload for the seed , the seed is empty");

        return newBytes;
    }

    private byte[] decryptKey(String key, EnvelopedData tempEnveloped) throws Exception {
        byte[] bytesKey = key.getBytes("UTF-8");
        //decode it
        bytesKey = Base64.decode(bytesKey,Base64.DEFAULT);
        return getSecurityManager().decryptSymmetricKey(bytesKey);

    }
}

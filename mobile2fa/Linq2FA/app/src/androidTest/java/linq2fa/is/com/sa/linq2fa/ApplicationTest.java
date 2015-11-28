package linq2fa.is.com.sa.linq2fa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import sa.com.is.activity.MainActivity;
import sa.com.is.activity.R;
import sa.com.is.fragments.WizardFragment;
import sa.com.is.models.EnvelopedData;
import sa.com.is.security.SecurityManagerImpl;
import sa.com.is.security.SystemConfiguration;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public static final int BARCODE_REQUEST_CODE = 0;
    public static final String BARCODE_RESULT_EXTRA = "SCAN_RESULT";
    public static final String ENCRYPTION_DECRYPTION_MODE = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    public static final String PRIVATE_KEY_NAME = "private.der";
    public static final String PUBLIC_KEY_NAME = "verificationpubkey.der";
    public static final String SYMMETRIC_ALGORITHM_NAME = "DESede";

    public ApplicationTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }




   public void testCase(){

       String url = "http://192.168.1.4:8080/imapp/rest/account/activate?id=91f32fb2-8feb-4d5c-8b21-fe61f3af8e5e";

       processContents(url);
   }


    private void processContents(final String contents) {

        try
        {


            //Create the Thread for verifying the current account
            Runnable verificationThread = new Runnable() {
                @Override
                public void run() {


                    try {

                        final EnvelopedData envelopedData = openConnection(new URL(contents));

                        updateAccountInformation(envelopedData);

                        if(envelopedData != null)
                        {
                            ((Activity)getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    //Do additional logic in here on the main UI Thread of the current fragment

                                }
                            });
                        }else
                        {
                            ((Activity)getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                }
                            });
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }catch (Exception s)
                    {
                        s.printStackTrace();
                    }

                    finally {

                    }

                }

                private void updateAccountInformation(EnvelopedData envelopedData) {

                    try
                    {
                        //Decrypt the seed value
                        byte[] symmetricKey = envelopedData.getKey().getBytes("UTF-8");
                        SystemConfiguration configuration = new SystemConfiguration();
                        configuration.setSymmetricAlgorithm(SYMMETRIC_ALGORITHM_NAME);
                        configuration.setPublicKeyName(PUBLIC_KEY_NAME);
                        configuration.setPrivateKeyName(PRIVATE_KEY_NAME);
                        configuration.setEncdecryptMode(ENCRYPTION_DECRYPTION_MODE);
                        configuration.setContext(getActivity());

                        sa.com.is.security.SecurityManager securityManager = new SecurityManagerImpl(configuration);

                        byte[] SeedBytes = envelopedData.getSeed().getBytes("UTF-8");
                        byte[] decodedSeedBytes = Base64.decode(SeedBytes, Base64.DEFAULT);

                        byte[] decryptedSeedValue = securityManager.decryptEnvelope(decodedSeedBytes,symmetricKey);

                        WizardFragment.envelopedData.setSeed(new String(decryptedSeedValue));
                        WizardFragment.envelopedData.setNumDigits(envelopedData.getNumDigits());
                        WizardFragment.envelopedData.setSeconds(envelopedData.getSeconds());
                        WizardFragment.envelopedData.setKey(envelopedData.getKey());

                    }catch (Exception s)
                    {
                        s.printStackTrace();
                    }
                }

                private EnvelopedData openConnection(URL url) {

                    try
                    {
                        URLConnection connection = url.openConnection();

                        InputStream inputStream = connection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                        StringBuffer buffer = new StringBuffer();

                        String line = null;

                        while((line = reader.readLine()) != null)
                        {
                            buffer.append(line);
                        }

                        String payload = buffer.toString();

                        Gson json = new GsonBuilder().create();

                        EnvelopedData  data = json.fromJson(payload,EnvelopedData.class);

                        return data;

                    }catch (Exception s)
                    {
                        s.printStackTrace();
                        return null;
                    }

                }
            };



            //now run The Thread
            Thread currentThread = new Thread(verificationThread);
            currentThread.start();



        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }
}
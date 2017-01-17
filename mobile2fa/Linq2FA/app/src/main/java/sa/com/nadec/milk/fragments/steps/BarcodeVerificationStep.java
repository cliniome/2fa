package sa.com.nadec.milk.fragments.steps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import sa.com.nadec.milk.activity.MainActivity;
import sa.com.nadec.milk.R;
import sa.com.nadec.milk.db.DatabaseManager;
import sa.com.nadec.milk.fragments.WizardFragment;
import sa.com.nadec.milk.models.EnvelopedData;
import sa.com.nadec.milk.security.*;
import sa.com.nadec.milk.utils.VerificationResult;
import sa.com.nadec.milk.wizard.StepType;
import sa.com.nadec.milk.wizard.Wizard;
import sa.com.nadec.milk.wizard.WizardStep;
import static sa.com.nadec.milk.wizard.StepType.*;
/**
 * Created by snouto on 26/11/15.
 */
public class BarcodeVerificationStep extends Fragment implements WizardStep {



    public static final int BARCODE_REQUEST_CODE = 0;
    public static final String BARCODE_RESULT_EXTRA = "SCAN_RESULT";
    public static final String ENCRYPTION_DECRYPTION_MODE = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    public static final String PRIVATE_KEY_NAME = "private.der";
    public static final String PUBLIC_KEY_NAME = "verificationpubkey.der";
    public static final String SYMMETRIC_ALGORITHM_NAME = "DESede";



    private Fragment parentFragment;

    public static VerificationResult verificationResult;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.barcode_reading_step,container,false);
        
        initView(rootView);
        
        return rootView;
    }


    private void initView(View rootView) {

        try
        {
            //Access the Web View First
            WebView webView = (WebView)rootView.findViewById(R.id.barcodewebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(getloadText(),"text/html","UTF-8");

            //Access the Button and Bind it
            Button openBarcodeBtn = (Button)rootView.findViewById(R.id.barcodebtn);

            openBarcodeBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //open the capture activity in here
                            MainActivity.SHOULD_RESUME = false;
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                            startActivityForResult(intent, BARCODE_REQUEST_CODE);
                           /* String url = "http://192.168.1.4:8080/imapp/rest/account/activate?id=34d8b32c-f044-40d3-b9fb-16731aaba553";
                            processContents(url);*/



                        }
                    }
            );


        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private String getloadText() throws IOException {

        InputStream is = this.getActivity().getAssets().open("barcode.html");
        byte[] data = new byte[is.available()];
        is.read(data);
        is.close();

        return new String(data,"UTF-8");
    }


    @Override
    public void setParent(Fragment parentFragment) {

        this.parentFragment = parentFragment;
    }

    @Override
    public StepType onFinish() throws Exception {

        try
        {
            return SUCCESS;

        }catch (Exception s)
        {
            throw  s;
        }
    }

    @Override
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception {


        if(data != null && data.hasExtra(BARCODE_RESULT_EXTRA))
        {
            //That means the barcode scanner has captured some data
            //begin processing it in here

            String contents = data.getStringExtra(BARCODE_RESULT_EXTRA);

            if(contents != null && contents.length() > 0 )
            {
                MainActivity.SHOULD_RESUME = true;
                //give it to the system Manager to process it on another thread other than the Main UI Thread
                processContents(contents);
            }else
            {
                //Notify the user that the scanning process was bad and nothing has returned from the Capture Activity
            }
        }



    }
    private  void processContents(final String contents) {

        try
        {
            //Show a Alert Dialog of the progress
            final ProgressDialog progressDialog = new ProgressDialog(((WizardFragment)parentFragment).getContext());
            progressDialog.setTitle("Please Wait.");
            progressDialog.setMessage("Activating Your Account.");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);


            //Create the Thread for verifying the current account
            final Runnable verificationThread = new Runnable() {
                @Override
                public void run() {


                    try {

                        boolean isUrl = false;
                        EnvelopedData envelopedData;

                            if (contents.startsWith("http") || contents.startsWith("https")){

                                isUrl = true;
                                envelopedData = openConnection(new URL(contents));

                            }else
                            {
                                //it is comma separated list , access it and feel the enveloped Data
                                envelopedData = new EnvelopedData();
                                //parse the contents string
                                String[] parsedContents = contents.split(",");
                                String numOfDigits = parsedContents[0];
                                String seconds = parsedContents[1];
                                String actualSeed = parsedContents[2];
                                String accountName = parsedContents[3];
                                byte[] actualDecodedSeed = Base64.decode(actualSeed.getBytes("UTF-8"),Base64.DEFAULT);
                                //set these values into the envelopedData
                                envelopedData.setNumDigits(Integer.parseInt(numOfDigits));
                                envelopedData.setSeconds(Integer.parseInt(seconds));
                                envelopedData.setSeed(new String(actualDecodedSeed));
                                envelopedData.setAccountName(accountName);

                            }


                        boolean finalResult = false;

                        if (envelopedData != null){

                            finalResult = updateAccountInformation(envelopedData, isUrl);
                        }

                        if(finalResult)
                        {
                            ((Activity)((WizardFragment) BarcodeVerificationStep.this.parentFragment).getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    //Do additional logic in here on the main UI Thread of the current fragment
                                    WizardFragment wizardFragment = ((WizardFragment)BarcodeVerificationStep.this.parentFragment);

                                    if(wizardFragment != null)
                                    {
                                        //move to next
                                        Wizard wizard = wizardFragment.getWizard();

                                        //move to the last step
                                        wizard.moveToLast();
                                    }

                                }
                            });
                        }else
                        {
                            ((Activity)((WizardFragment) BarcodeVerificationStep.this.parentFragment).getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progressDialog.dismiss();

                                    //alert the user of the reason behind that failure
                                    AlertDialog alertDialog = new AlertDialog.Builder(((WizardFragment) BarcodeVerificationStep.this.parentFragment).getContext())
                                            .setTitle("Activation Failure")
                                            .setMessage("Failed to Activate your Account Please Try again")
                                            .setIcon(R.drawable.error)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).create();


                                    alertDialog.show();


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
                        progressDialog.dismiss();
                    }

                }

                private boolean updateAccountInformation(EnvelopedData envelopedData,boolean isUrl) {

                    boolean result = false;

                    try
                    {
                        SystemConfiguration configuration = new SystemConfiguration();
                        configuration.setSymmetricAlgorithm(SYMMETRIC_ALGORITHM_NAME);
                        configuration.setPublicKeyName(PUBLIC_KEY_NAME);
                        configuration.setPrivateKeyName(PRIVATE_KEY_NAME);
                        configuration.setEncdecryptMode(ENCRYPTION_DECRYPTION_MODE);
                        configuration.setContext(((WizardFragment) BarcodeVerificationStep.this.parentFragment).getContext());

                        if (isUrl){

                            //Decrypt the seed value
                            byte[] symmetricKey = envelopedData.getKey().getBytes("UTF-8");
                            byte[] decodedKey = Base64.decode(symmetricKey,Base64.DEFAULT);


                            sa.com.nadec.milk.security.SecurityManager securityManager = new SecurityManagerImpl(configuration);

                            byte[] SeedBytes = envelopedData.getSeed().getBytes("UTF-8");
                            byte[] decodedSeedBytes = Base64.decode(SeedBytes, Base64.DEFAULT);

                            byte[] decryptedSeedValue = securityManager.decryptEnvelope(decodedSeedBytes,decodedKey);

                            WizardFragment.envelopedData.setSeed(new String(decryptedSeedValue, "UTF-8"));
                            WizardFragment.envelopedData.setNumDigits(envelopedData.getNumDigits());
                            WizardFragment.envelopedData.setSeconds(envelopedData.getSeconds());
                            WizardFragment.envelopedData.setKey(envelopedData.getKey());
                            WizardFragment.envelopedData.setAccountName(envelopedData.getAccountName());

                        }else
                        {
                            WizardFragment.envelopedData.setAccountName(envelopedData.getAccountName());
                            WizardFragment.envelopedData.setSeed(envelopedData.getSeed());
                            WizardFragment.envelopedData.setSeconds(envelopedData.getSeconds());
                            WizardFragment.envelopedData.setNumDigits(envelopedData.getNumDigits());

                        }
                        //Save it to the database
                        DatabaseManager manager = new DatabaseManager(configuration.getContext());

                        boolean activationResult = manager.createAccount(WizardFragment.envelopedData);

                        if(activationResult)
                        {
                            verificationResult = new VerificationResult(true,"Account has been Activated Successfully");
                            result = true;
                        }else
                        {
                            verificationResult = new VerificationResult(false,"Account is not Activated , Please Try again!");
                            result = false;
                        }

                    }catch (Exception s)
                    {
                        s.printStackTrace();

                        return result;
                    }


                    return result;
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


            //now show the progress dialog
            progressDialog.show();

            //now run The Thread
            Thread currentThread = new Thread(verificationThread);
            currentThread.start();



        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }
}

package sa.com.is.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.client.android.PreferencesActivity;

import sa.com.is.fragments.Linq2FAMainFragment;
import sa.com.is.fragments.PinPasswordFragment;
import sa.com.is.fragments.WizardFragment;
import sa.com.is.fragments.steps.BarcodeVerificationStep;
import sa.com.is.security.SystemConfiguration;
import sa.com.is.system.SystemManager;
import sa.com.is.system.SystemManagerImpl;

import static sa.com.is.fragments.steps.BarcodeVerificationStep.*;


public class MainActivity extends ActionBarActivity implements LoginListener {


    private WizardFragment wizardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadMainFragment();
    }



    private void initView() {

        try
        {
            //Set the main Fragment
            loadMainFragment();


        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void loadMainFragment() {
        try
        {
            SystemConfiguration configuration = new SystemConfiguration();
            configuration.setContext(this);
            configuration.setEncdecryptMode(BarcodeVerificationStep.ENCRYPTION_DECRYPTION_MODE);
            configuration.setPrivateKeyName(BarcodeVerificationStep.PRIVATE_KEY_NAME);
            configuration.setPublicKeyName(BarcodeVerificationStep.PUBLIC_KEY_NAME);
            configuration.setSymmetricAlgorithm(BarcodeVerificationStep.SYMMETRIC_ALGORITHM_NAME);
            SystemManager systemManager = new SystemManagerImpl(configuration);

            if(systemManager.activatedAccount()){

                //load the pin password fragment
                PinPasswordFragment pinPasswordFragment = new PinPasswordFragment();
                pinPasswordFragment.setLoginListener(this);
                pinPasswordFragment.setConfiguration(configuration);
                loadThis(pinPasswordFragment);

            }else
            {
                //Load the wizard Fragment
                WizardFragment wizardFragment = new WizardFragment();
                wizardFragment.setContext(this);
                this.wizardFragment = wizardFragment;

                loadThis(wizardFragment);
            }

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void loadThis(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }


    private void loadPinPasswordOnChange(){

        SystemConfiguration configuration = new SystemConfiguration();
        configuration.setSymmetricAlgorithm(SYMMETRIC_ALGORITHM_NAME);
        configuration.setPublicKeyName(PUBLIC_KEY_NAME);
        configuration.setPrivateKeyName(PRIVATE_KEY_NAME);
        configuration.setEncdecryptMode(ENCRYPTION_DECRYPTION_MODE);
        configuration.setContext(this);

        PinPasswordFragment pinPasswordFragment = new PinPasswordFragment();
        pinPasswordFragment.setLoginListener(this);
        pinPasswordFragment.setConfiguration(configuration);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Pass here the intent with the data
        //Something like
        if(wizardFragment != null)
        {

            try
            {
                wizardFragment.getWizard().registerIntent(requestCode,resultCode,data);

            }catch (Exception s)
            {
                s.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode ==KeyEvent.KEYCODE_BACK)
            return true;

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setClassName(this, PreferencesActivity.class.getName());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void doLogin(PinPasswordFragment fragment) {
        try
        {
            //That mean the pin Password login attempt was successful
            //load the main Application Fragment in here

            Linq2FAMainFragment mainFragment = new Linq2FAMainFragment();
            //set the main fragment in here

            //finally load it
            loadThis(mainFragment);

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }
}

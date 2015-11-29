package sa.com.is.fragments;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.kamranzafar.otp.OTP;

import sa.com.is.activity.R;
import sa.com.is.db.DatabaseManager;
import sa.com.is.fragments.views.OTPView;
import sa.com.is.models.EnvelopedData;
import sa.com.is.security.SystemConfiguration;

/**
 * Created by snouto on 28/11/15.
 */
public class Linq2FAMainFragment extends Fragment {


    private DatabaseManager databaseManager;
    private Context context;
    private EnvelopedData envelopedData;
    //UI Reference Handlers
    private ProgressBar progressBar;
    private TextView accountNameUI;
    private TextView otpTextValue;
    private CountDownTimer countDownTimer;
    private static LinearLayout otpView;
    public static boolean expired = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.linq2fa_main_fragment,container,false);
        
        initView(rootView);
        
        return rootView;
    }

    private void initView(View rootView) {
        try
        {
            //get a handler to the progress bar
            progressBar = (ProgressBar)rootView.findViewById(R.id.countDownchrono);
            //get a handler to the account name
            accountNameUI = (TextView)rootView.findViewById(R.id.accountName);
            //get a handler to the OTP value
            otpTextValue = (TextView)rootView.findViewById(R.id.otp_value);
            databaseManager = new DatabaseManager(getContext());
            //Get the current active enveloped data
            envelopedData = databaseManager.getActiveAccount();
            //initial View
            if(expired || otpView == null)
            {
                otpView = (LinearLayout)rootView.findViewById(R.id.otpview);
                otpView.setTag(this);

                //set the initial thing
                //Generate an Time based OTP
                BindValuesToView(otpView, envelopedData);

                otpView.invalidate();
                otpView.requestLayout();
            }
            //set on Click listener
            otpView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try
                    {
                        LinearLayout current = ((LinearLayout)v);

                        if(current != null)
                        {
                            if(((Linq2FAMainFragment)current.getTag()).expired)
                            {
                                enableViews();
                                //set an initial values again
                                BindValuesToView(current,envelopedData);
                            }
                        }

                    }catch (Exception s)
                    {
                        s.printStackTrace();
                    }
                }
            });



        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    public void set(final int seconds , String accountName, String otpValue){

        //initialize the views to their initial defaults

        //apply them to the ui
        this.accountNameUI.setText(accountName);
        this.otpTextValue.setText(otpValue);
        //set the progress bar information
        this.progressBar.setMax(seconds);
        this.progressBar.setProgress(seconds);

        //initialize a new CountDownTimer
        countDownTimer = new CountDownTimer(seconds * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long currentProgress = (millisUntilFinished / 1000);
                Linq2FAMainFragment.this.progressBar.setProgress((int) currentProgress);
                //request UI Update
                Linq2FAMainFragment.this.requestUIUpdate();


            }

            @Override
            public void onFinish() {
                Linq2FAMainFragment.this.expired = true;

                //Set the whole View to Red and disable all controls
                Linq2FAMainFragment.this.disableViews();

                progressBar.setProgress(seconds);

                Linq2FAMainFragment.this.requestUIUpdate();

            }
        };

        //start the count down timer
        countDownTimer.start();

    }
    private void disableViews() {

        try
        {
           otpView.setBackgroundColor(Color.YELLOW);
        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void enableViews(){

        try
        {
            otpView.setBackgroundColor(Color.WHITE);



        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }



    private void requestUIUpdate(){

        this.otpView.invalidate();
        this.otpView.requestLayout();
    }

    private void BindValuesToView(LinearLayout otpView, EnvelopedData envelopedData) {


        String otpValue = OTP.generate(envelopedData.getSeed(),String.valueOf(System.currentTimeMillis()),envelopedData.getNumDigits(),"totp");

        this.set(envelopedData.getSeconds(), envelopedData.getAccountName(), otpValue);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

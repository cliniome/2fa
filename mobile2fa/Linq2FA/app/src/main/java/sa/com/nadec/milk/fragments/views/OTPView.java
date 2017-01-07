package sa.com.nadec.milk.fragments.views;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.com.nadec.milk.R;

/**
 * Created by snouto on 29/11/15.
 */
public class OTPView extends ViewGroup {


    private int NumberOfSeconds;
    private CountDownTimer countDownTimer;
    private String accountName;
    private String otpValue;

    //UI Reference Handlers
    private ProgressBar progressBar;
    private TextView accountNameUI;
    private TextView otpTextValue;


    private boolean expired = false;

    public OTPView(Context context) {
        super(context);
        View.inflate(context, R.layout.otp_view_layout, this);
        this.initView();
    }

    public OTPView(Context context , AttributeSet attrs){
        super(context, attrs);
        View.inflate(context, R.layout.otp_view_layout, this);
        this.initView();
    }

    public OTPView(Context context, AttributeSet attrs , int defStyle)
    {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.otp_view_layout, this);
        this.initView();
    }


    public void set(int seconds , String accountName, String otpValue){

        //initialize the views to their initial defaults
        this.initView();
        this.setNumberOfSeconds(seconds);
        this.setAccountName(accountName);
        this.setOtpValue(otpValue);
        //apply them to the ui
        this.accountNameUI.setText(this.getAccountName());
        this.otpTextValue.setText(otpValue);
        //set the progress bar information
        this.progressBar.setMax(this.getNumberOfSeconds());
        this.progressBar.setProgress(this.getNumberOfSeconds());

        //initialize a new CountDownTimer
        countDownTimer = new CountDownTimer(this.getNumberOfSeconds() * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long currentProgress = (millisUntilFinished / 1000);
                OTPView.this.progressBar.setProgress((int) currentProgress);
                //request UI Update
                OTPView.this.requestUIUpdate();


            }

            @Override
            public void onFinish() {
                OTPView.this.setExpired(true);

                //Set the whole View to Red and disable all controls
                OTPView.this.disableViews();

            }
        };

        //start the count down timer
        countDownTimer.start();

    }

    private void disableViews() {

        try
        {
            this.accountNameUI.setEnabled(false);
            this.otpTextValue.setEnabled(false);

            //make them red
            this.accountNameUI.setBackgroundColor(Color.RED);
            this.otpTextValue.setBackgroundColor(Color.RED);

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }



    private void requestUIUpdate(){

        this.invalidate();
        this.requestLayout();
    }


    private void initView()
    {
        try
        {
            //get a handler to the progress bar
            progressBar = (ProgressBar)this.findViewById(R.id.countDownchrono);
            //get a handler to the account name
            accountNameUI = (TextView)this.findViewById(R.id.accountName);
            //get a handler to the OTP value
            otpTextValue = (TextView)this.findViewById(R.id.otp_value);




        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    public int getNumberOfSeconds() {
        return NumberOfSeconds;
    }

    public void setNumberOfSeconds(int numberOfSeconds) {
        NumberOfSeconds = numberOfSeconds;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

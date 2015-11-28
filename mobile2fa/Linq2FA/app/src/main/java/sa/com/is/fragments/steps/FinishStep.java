package sa.com.is.fragments.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sa.com.is.activity.R;
import sa.com.is.wizard.WizardStep;

/**
 * Created by snouto on 28/11/15.
 */
public class FinishStep extends Fragment implements WizardStep {


    private Fragment parentFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.finish_step_layout,container,false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        try
        {
            ImageView imageView = (ImageView)rootView.findViewById(R.id.finishImg);
            TextView textView = (TextView)rootView.findViewById(R.id.accountMessage);

            if(BarcodeVerificationStep.verificationResult == null || !BarcodeVerificationStep.verificationResult.isResult())
            {
                imageView.setImageResource(R.drawable.failure);
                String reason = ((BarcodeVerificationStep.verificationResult == null) ? "There was a problem Activating Your Account , Please Try again " :
                BarcodeVerificationStep.verificationResult.getReason());
                textView.setText(reason);
            }else if (BarcodeVerificationStep.verificationResult != null && BarcodeVerificationStep.verificationResult.isResult())
            {
                imageView.setImageResource(R.drawable.success);
                textView.setText(BarcodeVerificationStep.verificationResult.getReason());
            }



        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    @Override
    public void setParent(Fragment parentFragment) {

        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onFinish() throws Exception {

        return true;
    }


    @Override
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception {

    }
}

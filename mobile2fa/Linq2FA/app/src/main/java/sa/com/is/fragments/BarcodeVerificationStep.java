package sa.com.is.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sa.com.is.activity.R;
import sa.com.is.wizard.WizardStep;

/**
 * Created by snouto on 26/11/15.
 */
public class BarcodeVerificationStep extends Fragment implements WizardStep {



    private Fragment parentFragment;

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

        try
        {
            return true;

        }catch (Exception s)
        {
            throw  s;
        }
    }

    @Override
    public void processIntent(Intent data) throws Exception {

    }
}

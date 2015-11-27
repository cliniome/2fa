package sa.com.is.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sa.com.is.activity.R;
import sa.com.is.fragments.steps.AccountPasswordStep;
import sa.com.is.fragments.steps.BarcodeVerificationStep;
import sa.com.is.fragments.steps.WelcomeStep;
import sa.com.is.models.EnvelopedData;
import sa.com.is.wizard.Wizard;
import sa.com.is.wizard.WizardImpl;

/**
 * Created by snouto on 26/11/15.
 */
public class WizardFragment extends Fragment {


    private static Wizard wizard;

    public static EnvelopedData envelopedData;

    private Context context;

    public static Wizard getWizard() {
        return wizard;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        envelopedData = new EnvelopedData();
        View rootView = inflater.inflate(R.layout.wizard_layout,container,false);

        this.initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        try
        {

            wizard = new WizardImpl(this);

            //Add the wizard steps in here one by one
            getWizard().addStep(new WelcomeStep());
            getWizard().addStep(new AccountPasswordStep());
            getWizard().addStep(new BarcodeVerificationStep());



            //Finally attach to the view
            getWizard().attachToView(rootView);


        }catch (Exception s)
        {
            s.printStackTrace();
        }


    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

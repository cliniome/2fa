package sa.com.nadec.milk.fragments.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

import sa.com.nadec.milk.wizard.StepType;
import sa.com.nadec.milk.wizard.WizardStep;
import static sa.com.nadec.milk.wizard.StepType.*;

/**
 * Created by snouto on 26/11/15.
 */
public class WelcomeStep extends Fragment implements WizardStep {


    private Fragment parentFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View parentView = new WebView(getActivity());

        initView(parentView);

        return parentView;
    }

    private void initView(View parentView)  {

        try
        {
            WebView currentView = (WebView)parentView;

            currentView.getSettings().setJavaScriptEnabled(true);
            currentView.loadDataWithBaseURL("file:///android_asset/", getloadText(),"text/html","UTF-8",null);


        }catch (Exception s)
        {
            s.printStackTrace();

        }

    }

    private String getloadText() throws IOException {

        InputStream is = this.getActivity().getAssets().open("welcomepage.html");

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

            //Nothing in here , so just return true
            return SUCCESS;

        }catch (Exception s)
        {
            throw s;
        }


    }

    @Override
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception {

    }

}

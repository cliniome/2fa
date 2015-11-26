package sa.com.is.wizard;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import sa.com.is.activity.R;
import sa.com.is.fragments.WizardFragment;

/**
 * Created by snouto on 26/11/15.
 */
public class WizardImpl implements Wizard , OnClickListener {

    private  List<WizardStep> steps;
    private static final String NEXT_TAG = "NEXT";
    private static final String PREVIOUS_TAG = "PREVIOUS";
    private WizardFragment parentFragment;
    private Button previousBtn;
    private Button nextBtn;
    private int current_step = 0;


    public WizardImpl(WizardFragment parent){

        this.parentFragment = parent;
        steps = new ArrayList<WizardStep>();

    }


    @Override
    public void attachToView(View view) throws Exception {



        //Set the bindings to the previous and Next Buttons

        //Get the previous Button
        previousBtn = (Button)view.findViewById(R.id.previous_btn);
        nextBtn = (Button)view.findViewById(R.id.next_btn);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        //set the first Fragment
        setInitialView();

    }

    private void setInitialView() {

        current_step = 0;
        moveTo(current_step);
    }

    private void preProcess(int step){


        if(step == 0)
        {
            previousBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }else if(step == steps.size() - 1)
        {
            previousBtn.setEnabled(true);
            nextBtn.setEnabled(false);
        }else if (step > 0 && step < steps.size() - 1)
        {
            previousBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }

        if(step == 0 && step == steps.size() - 1)
        {
            previousBtn.setEnabled(false);
            nextBtn.setEnabled(false);
        }

    }

    @Override
    public void addStep(WizardStep step) {
        if(steps == null)
            steps = new ArrayList<WizardStep>();
        steps.add(step);
    }

    @Override
    public void registerIntent(Intent data) throws Exception {

        //pass the intent to the current wizard Step
        WizardStep currentStep = steps.get(current_step);

        if(currentStep != null)
            currentStep.processIntent(data);
    }



    @Override
    public void onClick(View v) {

        if(v.getTag().equals(NEXT_TAG))
        {

            onNext();

        }else if (v.getTag().equals(PREVIOUS_TAG))
        {
            onPrevious();
        }

    }


    private void onPrevious() {
        try
        {
            int previousStep = --current_step;

            if(previousStep >=0)
            {
                moveTo(previousStep);
            }

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void onNext() {
        try
        {
            //Access the current step
            WizardStep currentStep = steps.get(current_step);

            if(currentStep.onFinish())
            {
                //move to next fragment
                int nextStep = ++current_step;
                if(nextStep < steps.size())
                {
                    moveTo(nextStep);
                }
            }

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void moveTo(int step) {

        preProcess(step);

        //ribaviser

        WizardStep currentStep = steps.get(step);
        currentStep.setParent(parentFragment);
        FragmentManager manager = parentFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.wizard_pane, (Fragment) currentStep);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

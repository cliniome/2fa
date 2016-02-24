package sa.com.nadec.milk.fragments.steps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import sa.com.nadec.milk.R;
import sa.com.nadec.milk.fragments.WizardFragment;
import sa.com.nadec.milk.wizard.StepType;
import sa.com.nadec.milk.wizard.WizardStep;
import static sa.com.nadec.milk.wizard.StepType.*;

/**
 * Created by snouto on 23/02/16.
 * This activity will verify the chosen account password , if verified correctly, the wizard will proceed to next step otherwise , the app will return back to the previous
 * step
 */
public class AccountPasswordVerificationStep extends Fragment implements WizardStep {


    private Fragment parentFragment;

    private EditText pinPasswordText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pin_verification_step,container,false);

        pinPasswordText = (EditText)rootView.findViewById(R.id.pinPassword);

        return rootView;
    }


    @Override
    public void setParent(Fragment parentFragment) {

        this.parentFragment = parentFragment;

    }

    @Override
    public StepType onFinish() throws Exception {
        /*return SUCCESS;*/

       try
       {
           StepType resultType = SUCCESS;

           String chosenPassword = WizardFragment.envelopedData.getBinPassword();
           String currentPassword = this.pinPasswordText.getText().toString();

           if(chosenPassword == null || !(chosenPassword.equals(currentPassword)))
           {
               AlertDialog dialog = new AlertDialog.Builder(getActivity())
                       .setTitle("Validation Error")
                       .setMessage("Please , Your Application Pin does not Match.")
                       .setIcon(R.drawable.error)
                       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               pinPasswordText.getText().clear();

                               dialog.dismiss();
                           }
                       }).create();

               dialog.show();
               WizardFragment.envelopedData.setBinPassword("");
               resultType = RETURN;
           }

           return resultType;

       }catch (Exception s)
       {
           throw s;
       }

       finally
       {
           //Clear the inserted pin password
           this.pinPasswordText.getText().clear();
           //Clear the saved Pin Password again
           Context context = ((WizardFragment)parentFragment).getContext();
           InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(pinPasswordText.getWindowToken(), 0);
       }
    }
    @Override
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception {

    }
}

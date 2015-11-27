package sa.com.is.fragments.steps;

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

import sa.com.is.activity.R;
import sa.com.is.fragments.WizardFragment;
import sa.com.is.wizard.WizardStep;

/**
 * Created by snouto on 26/11/15.
 */
public class AccountPasswordStep extends Fragment implements WizardStep {

    private Fragment parentFragment;

    private EditText pinPasswordText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pin_password_step,container,false);

        pinPasswordText = (EditText)rootView.findViewById(R.id.pinPassword);

        return rootView;
    }

    @Override
    public void setParent(Fragment parentFragment) {

        this.parentFragment = parentFragment;

    }

    @Override
    public boolean onFinish() throws Exception {
        try
        {
            //check the length of the chosen pin
            if(checkStrength())
            {
                //store that into the enveloped Data
                WizardFragment.envelopedData.setBinPassword(pinPasswordText.getText().toString());

                return true;
            }else
            {
                Context currentContext = ((WizardFragment)parentFragment).getContext();
                AlertDialog dialog = new AlertDialog.Builder(currentContext)
                        .setTitle("Validation Error")
                        .setMessage("Please , Choose at least 8 Alphanumeric Pin Password before Proceeding.")
                        .setIcon(R.drawable.error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                pinPasswordText.getText().clear();

                                dialog.dismiss();
                            }
                        }).create();

                dialog.show();

                return false;
            }



        }catch (Exception s)
        {
            throw s;
        }

        finally
        {
            Context context = ((WizardFragment)parentFragment).getContext();
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pinPasswordText.getWindowToken(), 0);
        }


    }

    @Override
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception {

    }


    private boolean checkStrength() {
        try
        {
            String pin_passwd = pinPasswordText.getText().toString();

            if(pin_passwd != null && pin_passwd.length() >= 8)
                return true;
            else return false;

        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }
    }
}

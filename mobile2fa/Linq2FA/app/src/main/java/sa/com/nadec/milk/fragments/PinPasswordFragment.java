package sa.com.nadec.milk.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import sa.com.nadec.milk.activity.LoginListener;
import sa.com.nadec.milk.R;
import sa.com.nadec.milk.security.SystemConfiguration;
import sa.com.nadec.milk.system.SystemManager;
import sa.com.nadec.milk.system.SystemManagerImpl;

/**
 * Created by snouto on 28/11/15.
 */
public class PinPasswordFragment extends Fragment {


    private LoginListener loginListener;

    private SystemConfiguration configuration;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pin_password_fragment_layout,container,false);
        initView(rootView);

        return rootView;

    }

    private void initView(View rootView) {

        try
        {
            //Get the pin password passed in



            //Get the button when clicked
            Button pinBtn = (Button)rootView.findViewById(R.id.pinBtn);

            pinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    final EditText password = ((EditText)((ViewGroup) v.getParent()).findViewById(R.id.pin_password_txt));
                    try
                    {
                        SystemManager systemManager = new SystemManagerImpl(getConfiguration());

                        if(systemManager.isPinPasswordCorrect(password.getText().toString()))
                        {
                            getLoginListener().doLogin(PinPasswordFragment.this);
                        }else
                        {
                            AlertDialog dialog = new AlertDialog.Builder(getConfiguration().getContext())
                                    .setTitle("Invalid Login Details")
                                    .setIcon(R.drawable.error)
                                    .setMessage("Pin Password entered is incorrect. Try again !")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            password.setText("");
                                            dialog.dismiss();
                                        }
                                    }).create();

                            dialog.show();
                        }

                    }catch (Exception s)
                    {
                        s.printStackTrace();
                    }

                    finally {

                        InputMethodManager imm = (InputMethodManager)PinPasswordFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                    }


                }
            });

        }catch (Exception s)
        {
            s.printStackTrace();
        }


    }


    public LoginListener getLoginListener() {
        return loginListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public SystemConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }

}

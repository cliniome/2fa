package sa.com.is.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

import sa.com.is.activity.R;
import sa.com.is.wizard.WizardStep;

/**
 * Created by snouto on 26/11/15.
 */
public class BarcodeVerificationStep extends Fragment implements WizardStep {



    public static final int BARCODE_REQUEST_CODE = 0;



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
            //Access the Web View First
            WebView webView = (WebView)rootView.findViewById(R.id.barcodewebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(getloadText(),"text/html","UTF-8");

            //Access the Button and Bind it
            Button openBarcodeBtn = (Button)rootView.findViewById(R.id.barcodebtn);

            openBarcodeBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //open the capture activity in here
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                            startActivityForResult(intent, BARCODE_REQUEST_CODE);

                        }
                    }
            );


        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private String getloadText() throws IOException {

        InputStream is = this.getActivity().getAssets().open("barcode.html");
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

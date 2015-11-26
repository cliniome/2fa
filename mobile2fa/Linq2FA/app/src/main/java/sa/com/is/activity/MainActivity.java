package sa.com.is.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.client.android.PreferencesActivity;

import sa.com.is.fragments.WizardFragment;

public class MainActivity extends ActionBarActivity {


    private WizardFragment wizardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        try
        {
            //Set the main Fragment
            loadMainFragment();


        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private void loadMainFragment() {
        try
        {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            WizardFragment wizardFragment = new WizardFragment();
            wizardFragment.setContext(this);
            this.wizardFragment = wizardFragment;
            transaction.replace(R.id.content_frame,wizardFragment);
            transaction.commit();

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Pass here the intent with the data
        /*//Something like
        if(wizardFragment != null)
        {
            try
            {
                wizardFragment.getWizard().registerIntent(data);

            }catch (Exception s)
            {
                s.printStackTrace();
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setClassName(this, PreferencesActivity.class.getName());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

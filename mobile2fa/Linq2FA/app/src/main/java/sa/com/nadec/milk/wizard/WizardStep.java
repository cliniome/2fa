package sa.com.nadec.milk.wizard;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by snouto on 26/11/15.
 */
public interface WizardStep {

    public void setParent(Fragment parentFragment);
    public StepType onFinish() throws Exception;
    public void processIntent(int requestCode, int resultCode, Intent data) throws Exception;

}

package sa.com.is.wizard;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by snouto on 26/11/15.
 */
public interface WizardStep {

    public void setParent(Fragment parentFragment);
    public boolean onFinish() throws Exception;
    public void processIntent(Intent data) throws Exception;

}

package sa.com.is.wizard;

import android.content.Intent;
import android.view.View;

/**
 * Created by snouto on 26/11/15.
 */
public interface Wizard {

    public void attachToView(View view) throws Exception;

    public void addStep(WizardStep step);

    public void registerIntent(Intent data) throws Exception;
}

package sa.com.nadec.milk.system;

import sa.com.nadec.milk.models.EnvelopedData;
import sa.com.nadec.milk.utils.VerificationResult;

/**
 * Created by snouto on 27/11/15.
 */
public interface SystemManager {

    public VerificationResult verifyContents(String contents , EnvelopedData data);
    public boolean activatedAccount();
    public boolean isPinPasswordCorrect(String password);
}

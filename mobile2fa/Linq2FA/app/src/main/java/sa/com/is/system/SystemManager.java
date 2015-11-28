package sa.com.is.system;

import sa.com.is.models.EnvelopedData;
import sa.com.is.utils.VerificationResult;

/**
 * Created by snouto on 27/11/15.
 */
public interface SystemManager {

    public VerificationResult verifyContents(String contents , EnvelopedData data);
    public boolean activatedAccount();
    public boolean isPinPasswordCorrect(String password);
}

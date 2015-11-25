package sa.com.is.imapp.utils;

import sa.com.is.imapp.models.Account;

/**
 * Created by snouto on 25/11/15.
 */
public interface AccountManager {


    public String generateQR(Account account) throws Exception;

    public String compress(String QRPayload) throws Exception;

}

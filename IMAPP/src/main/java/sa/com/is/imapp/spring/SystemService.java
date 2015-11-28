package sa.com.is.imapp.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sa.com.is.imapp.db.beans.AccountsDAO;

/**
 * Created by snouto on 28/11/15.
 */
@Component
@Scope("prototype")
public class SystemService {

    @Autowired
    private AccountsDAO accountsDAO;

    public AccountsDAO getAccountsDAO() {
        return accountsDAO;
    }

    public void setAccountsDAO(AccountsDAO accountsDAO) {
        this.accountsDAO = accountsDAO;
    }
}

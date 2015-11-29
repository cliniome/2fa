package sa.com.is.imapp.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sa.com.is.imapp.db.beans.AccountsDAO;
import sa.com.is.imapp.utils.SystemConfiguration;

/**
 * Created by snouto on 28/11/15.
 */
@Component
@Scope("prototype")
public class SystemService {

    @Autowired
    private AccountsDAO accountsDAO;

    @Autowired
    private SystemConfiguration configuration;

    public AccountsDAO getAccountsDAO() {
        return accountsDAO;
    }

    public void setAccountsDAO(AccountsDAO accountsDAO) {
        this.accountsDAO = accountsDAO;
    }

    public SystemConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }
}

package sa.com.is.system;
import sa.com.is.db.DatabaseManager;
import sa.com.is.security.SecurityManager;
import sa.com.is.security.SecurityManagerImpl;
import sa.com.is.security.SystemConfiguration;

/**
 * Created by snouto on 26/11/15.
 */
public class SystemManager {

    private SecurityManager securityManager;
    private DatabaseManager databaseManager;

    private SystemConfiguration configuration;

    public SystemManager(SystemConfiguration configuration)
    {
        this.setConfiguration(configuration);
        initSystem();
    }

    private void initSystem() {
        try
        {
            this.setSecurityManager(new SecurityManagerImpl(getConfiguration()));
            this.setDatabaseManager(new DatabaseManager(getConfiguration().getContext()));

        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }


    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public SystemConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(SystemConfiguration configuration) {
        this.configuration = configuration;
    }
}

package sa.com.is.system;
import sa.com.is.db.DatabaseManager;
import sa.com.is.models.EnvelopedData;
import sa.com.is.security.SecurityManager;
import sa.com.is.security.SecurityManagerImpl;
import sa.com.is.security.SystemConfiguration;
import sa.com.is.utils.VerificationResult;

/**
 * Created by snouto on 26/11/15.
 */
public class SystemManagerImpl implements SystemManager {

    private SecurityManager securityManager;
    private DatabaseManager databaseManager;

    private SystemConfiguration configuration;

    public SystemManagerImpl(SystemConfiguration configuration)
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

    @Override
    public VerificationResult verifyContents(String contents, EnvelopedData data) {

        VerificationResult result = new VerificationResult();

        try
        {

        }catch (Exception s)
        {
            s.printStackTrace();
        }
        finally {

            return result;
        }
    }
}

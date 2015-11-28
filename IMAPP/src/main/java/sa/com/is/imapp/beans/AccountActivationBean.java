package sa.com.is.imapp.beans;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sa.com.is.imapp.db.beans.AccountsDAO;
import sa.com.is.imapp.models.Account;
import sa.com.is.imapp.spring.SpringSystemBridge;
import sa.com.is.imapp.spring.SystemService;
import sa.com.is.imapp.utils.AccountManagerImpl;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Created by snouto on 22/11/15.
 */
@Component("accountActivationBean")
@Scope("prototype")
public class AccountActivationBean implements Serializable {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String userName;
    private AccountManagerImpl accountManager;
    private String qrPayload = "http://www.is.com.sa";

    private SystemService systemService;

    @PostConstruct
    public void onInit(){
       try
       {
          // AccountsDAO accountsDAO = (AccountsDAO) SpringUtils.getSpringBean("accountDAO");
           systemService = SpringSystemBridge.services();
           accountManager = new AccountManagerImpl(systemService.getAccountsDAO());

       }catch (Exception s)
       {
           s.printStackTrace();
       }
    }


    public String onFlowProcessor(FlowEvent event)
    {
        if(event.getNewStep() != null && event.getNewStep().equals("barcode"))
        {
            Account account = getAccount();

            if(account != null && accountManager != null)
            {
                //Generate the QR Enveloped Data

                try {

                    String tempPayload = accountManager.generateQR(account);
                    if(tempPayload != null)
                    {
                        qrPayload = tempPayload;

                        clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

        return event.getNewStep();
    }

    private void clear() {

        this.setEmailAddress("");
        this.setFirstName("");
        this.setLastName("");
        this.setUserName("");
    }

    private Account getAccount() {

        Account account = new Account();
        account.setEmailAddress(this.getEmailAddress());
        account.setFirstName(this.getFirstName());
        account.setLastName(this.getLastName());
        account.setUserName(this.getUserName());

        return account;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }


}

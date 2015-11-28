package sa.com.is.imapp.db.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sa.com.is.imapp.db.models.Account;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * Created by snouto on 28/11/15.
 */
@Component("accountDAO")
@Scope(value = "prototype")
public class AccountsDAO extends AbstractDAO<Account>{


    @Override
    public String getEntityName() {
        return "Account";
    }


    public boolean insertAccount(Account account)
    {
        return addEntity(account);
    }

    public Account getAccountByID(String id)
    {
        String query = "select account from Account account where account.id = :id and account.deleted = :deleted";
        Query currentQuery = getManager().createQuery(query);
        currentQuery.setParameter("id",id);
        currentQuery.setParameter("deleted",false);
        return (Account) currentQuery.getSingleResult();
    }

    public boolean updateAccount(Account account)
    {
        return updateEntity(account);
    }



}

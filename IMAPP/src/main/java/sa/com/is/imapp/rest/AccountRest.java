package sa.com.is.imapp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import sa.com.is.imapp.db.models.Account;
import sa.com.is.imapp.spring.SpringSystemBridge;
import sa.com.is.imapp.spring.SystemService;
import sa.com.is.imapp.utils.EnvelopedData;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by snouto on 28/11/15.
 */
@Path("/account")
@Scope("prototype")
public class AccountRest {


    @Autowired
    private SystemService systemService;


    @Path("/activate")
    @GET
    @Produces("application/json")
    public Response activate(

            @QueryParam("id") String id
    )
    {
        try
        {
            systemService = SpringSystemBridge.services();

            if(systemService == null) throw new Exception("Internal Error");

            //The URL should be something like
            //http://<Server Address>:<Port>/imapp/rest/account/activate?id=<ID>

            Account account = systemService.getAccountsDAO().getAccountByID(id);

            if(account == null) throw new Exception("Internal  Error");

            EnvelopedData envelopedData = account.toEnvelopedData();


            account.setDeleted(true);

            //systemService.getAccountsDAO().updateAccount(account);


            return Response.ok(envelopedData).build();




        }catch (Exception s)
        {
            s.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }



    @Path("/update")
    @GET
    @Produces("application/json")
    public Response update(

            @QueryParam("id") String id
    )
    {

        try
        {
            systemService = SpringSystemBridge.services();

            if(systemService == null) throw new Exception("Internal Error");

            //The URL should be something like
            //http://<Server Address>:<Port>/imapp/rest/account/activate?id=<ID>

            Account account = systemService.getAccountsDAO().getAccountByID(id);

            if(account == null) throw new Exception("Internal  Error");

            account.setDeleted(true);

            //now update
            boolean result = systemService.getAccountsDAO().updateAccount(account);

            if(result)
            {
                return Response.ok().build();
            }else throw new Exception("Internal Error");


        }catch (Exception s)
        {
            s.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }




}

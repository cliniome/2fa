package sa.com.is.imapp.beans;

import org.kamranzafar.otp.OTP;
import sa.com.is.imapp.spring.SpringSystemBridge;
import sa.com.is.imapp.spring.SystemService;
import sa.com.is.imapp.utils.SpringUtils;
import sa.com.is.imapp.utils.SystemConfiguration;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by snouto on 29/11/15.
 */
public class AccountVerificationBean implements Serializable {

    private String userName;
    private String otpValue;
    private SystemService systemService;


    @PostConstruct
    public void onInit(){

        try
        {
            systemService = SpringSystemBridge.services();

        }catch (Exception s)
        {
            s.printStackTrace();
        }

    }



    public void onValidate(ActionEvent event)
    {
        try
        {
            //Get the seed value for the passed in user Name
            String seedValue = getSeed(getUserName());
            //0508172582
            //0508172582
            //now verify the otp
            boolean verificationResult = OTP.verify(getOtpValue(),seedValue
            ,String.valueOf(System.currentTimeMillis()),systemService.getConfiguration().getNumDigits(),
                    systemService.getConfiguration().getValidWindow(),"totp");

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if(verificationResult)
            {
                FacesMessage message= new FacesMessage("Thank You","OTP Verification was Successful");
                facesContext.addMessage(null,message);
            }else
            {
                FacesMessage message = new FacesMessage("Verification Failure","OTP Verification " +
                        "was not Successful");
                facesContext.addMessage(null,message);
            }


        }catch (Exception s)
        {
            s.printStackTrace();
        }
    }

    private String getSeed(String account){


        String finalSeed = toHex(account);

        return finalSeed;
    }

    public String toHex(String arg) {
        try {
            return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return "";
        }
    }



    private int hashCode(String value)
    {
        int hashcode = value.hashCode();
        return Math.abs(hashcode);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }
}

import org.kamranzafar.otp.OTP;
import org.primefaces.util.Base64;

import java.math.BigInteger;

/**
 * Created by snouto on 30/12/15.
 */

/* String originalSeed = "MDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDczNmU2Zjc1NzQ2Zg==";
        byte[] decodedSeed = Base64.decode(originalSeed.getBytes());

        originalSeed = new String(decodedSeed);

        System.out.println(originalSeed);

        byte[] hexedBytes = hexStr2Bytes(originalSeed);

        System.out.println(bytesToString(hexedBytes));*/
public class TestingClass {

    public static void main(String[] args){



        String key = "0000000000000000000000000000736e6f75746f";
        String base = "1451488284588";
        int digits = 7;
        String provider = "totp";

        String otp = OTP.generate(key,base,digits,provider);

        System.out.println("The finalized OTP : " + otp);



    }

    private static String bytesToString(byte[] bytes){

        String bytesString = "[";

        for(byte current : bytes){

            bytesString += current + ",";
        }
        bytesString += "]";


        return bytesString;
    }

    private static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i + 1];
        return ret;
    }
}

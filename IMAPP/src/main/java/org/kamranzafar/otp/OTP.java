/**
 * Copyright 2012 Kamran Zafar 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 */

package org.kamranzafar.otp;

/**
 * @author Kamran Zafar
 * 
 */
public class OTP {
	public static String generate(String key, String base, int digits, String provider) {
		return OTPProviderFactory.getOTPProvider(provider).generate(key, base, digits);
	}

	public static boolean verify(String otp , String key , String base, int digits, int validWindow,
						  String provider){

		boolean result = false;

		try
		{

			//Get the tart and end
			Long startWindow = Long.parseLong(base) - (validWindow * 1000);
			Long endWindow = Long.parseLong(base) + (validWindow * 1000);

			for(long i = startWindow;i<=endWindow;i++){

				String currentOtp = generate(key,String.valueOf(i),digits,provider);

				if(currentOtp !=null && currentOtp.equals(otp))
				{
					result = true;
					break;
				}

			}


		}catch (Exception s)
		{
			s.printStackTrace();
		}

		finally {

			return result;
		}

	}
}

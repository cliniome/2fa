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

import org.kamranzafar.otp.provider.HOTPProvider;
import org.kamranzafar.otp.provider.TOTPProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamran Zafar
 * 
 */
public class OTPProviderFactory {
	private static final String PROPS_CLASS = "class";
	private static final String PROPS_PROVIDERS = "providers";
	private static final String PROVIDERS = "org/kamranzafar/otp/providers.properties";
	public static Map<String, OTPProvider> providers = new HashMap<String, OTPProvider>();

	static {
		/*loadProvider(OTPProviderFactory.class.getClassLoader().getResourceAsStream(PROVIDERS));

		String ext = Configuration.getExternalOTPProviders();
		if (ext != null) {
			try {
				loadProvider(new FileInputStream(ext));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}*/

		providers.put("totp",new TOTPProvider());
		providers.put("hotp",new HOTPProvider());
	};


	public static OTPProvider getOTPProvider(String name) {
		return providers.get(name.toLowerCase());
	}

	public static void addProvider(String name, OTPProvider provider) {
		providers.put(name, provider);
	}
}

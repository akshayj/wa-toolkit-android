package com.windowsazure.samples.internal.authentication;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.windowsazure.samples.internal.util.Base64;

public abstract class SharedKey {

	public static String signDataWithKey(String data, String key)
		throws	InvalidKeyException,
				NoSuchAlgorithmException,
				UnsupportedEncodingException {
		
		byte[] accessKeyBytes = Base64.decode(key.getBytes());
	    byte[] requestStringBytes = data.getBytes("UTF8");
	    
	    SecretKeySpec secret = new SecretKeySpec(accessKeyBytes,"HMACSHA256");
	    Mac hMac = Mac.getInstance("HMACSHA256");
	    hMac.init(secret);
	    byte[] sig = hMac.doFinal(requestStringBytes);
	    String encodedSig = new String(Base64.encode(sig));
	    return encodedSig;
	}
	
	public String getAuthorizationHeader(String account, String key)
		throws	InvalidKeyException,
				NoSuchAlgorithmException,
				UnsupportedEncodingException {
		
		return "SharedKey " + account + ":" + signDataWithKey(getStringToSign(), key);
	}
	
	protected abstract String getStringToSign();
}

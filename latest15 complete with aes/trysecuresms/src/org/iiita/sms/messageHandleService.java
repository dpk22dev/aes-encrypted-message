package org.iiita.sms;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class messageHandleService extends Service {

	private String encyptedText;
	private String message;

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside messageHandleService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        

		Bundle extras = intent.getExtras();
		encyptedText = extras.get("body").toString();
	    
		decrypt();
		
		return START_STICKY;
	    
	}
	
	
	private void decrypt() {
		// TODO Auto-generated method stub
			String s = "fr_sk";
			String read_key = "";
			String strLine;
			
			FileInputStream fin;
			try {
				fin = openFileInput( s );
				DataInputStream in = new DataInputStream(fin);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				while ((strLine = br.readLine()) != null)   {
					  // Print the content on the console
					 read_key += strLine;
			    }
				
				Toast.makeText(this, s +" : "+read_key , Toast.LENGTH_SHORT).show();
				
				in.close();
				//may be required to reset bufferReader
				br.close();	 			
				fin.close();

				getApplicationContext().deleteFile(s);
	 			Toast.makeText(this, s + " deleted" , Toast.LENGTH_SHORT).show();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 						 				 												
		//convert string to secretKey and decrypt the encrypted text
			
			byte[] skdecoded = Base64.decodeBase64( read_key.getBytes() );
	        SecretKey secretKey = new SecretKeySpec(skdecoded, 0, skdecoded.length, "AES");
	        
	        Cipher dcipher;
	        
	        try {
				dcipher = Cipher.getInstance("AES");
				dcipher.init(Cipher.DECRYPT_MODE, secretKey);
				
				byte[] encryptedBytes = Base64.decodeBase64(encyptedText.getBytes());
		        byte[] utf8 = dcipher.doFinal( encryptedBytes );
				
		        message = new String(utf8, "UTF8");
		        
		        Toast.makeText(this,"message: " + message, Toast.LENGTH_SHORT).show();
		        
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
	        
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

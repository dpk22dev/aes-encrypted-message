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

public class encryptService extends Service {

	private String skey;
	private String[] list_files;
	private String encodedText;
	private String number = "";
	
	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside encryptService", Toast.LENGTH_SHORT).show();		
		
	}
		
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();			    
	    skey = extras.get("skey").toString();
	    
	    encrypt();
	    encodedText = "@message"+encodedText;
	    sendSMS();
	    
	    return START_STICKY;
	    
	}
	
	private void sendSMS() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),sendSMSService.class);		
		i.putExtra("number", number);
		i.putExtra("body",encodedText);	
		startService( i );
		
	}

	private void encrypt() {
		// TODO Auto-generated method stub
		byte[] skdecoded = Base64.decodeBase64( skey.getBytes() );
        SecretKey secretKey = new SecretKeySpec(skdecoded, 0, skdecoded.length, "AES");
        
        Toast.makeText(this,"hi: " + secretKey.toString(), Toast.LENGTH_SHORT).show();
        
        list_files = getApplicationContext().fileList();
        FileInputStream fin = null;
        String read_key = null;
        
		for( String s : list_files ){
			Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
			
			String strLine;
 			read_key = "";
 			
 			try{

				if( s.endsWith("m") == true ){										
					//******** get the number *****
					
				for( int i = 0; i < s.length(); i++ ){
					if( s.charAt(i) >= '0' && s.charAt(i) <= '9' ){
						number += s.charAt(i);
					}
				}
					
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
	 			
	 			//******** delete to_5545768_m file *********
	 			getApplicationContext().deleteFile(s);
	 			Toast.makeText(this, s + " DELETED" , Toast.LENGTH_SHORT).show();
			}
 			
		}catch(FileNotFoundException e){				
			e.printStackTrace();
			Toast.makeText(this, s + " NOT FOUND" , Toast.LENGTH_SHORT).show();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, s + " INPUT OUTPUT EXCEPTION" , Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
 			
		}
		
		//encrypt readkey with secretKey
		Cipher ecipher = null;
		try {
			ecipher = Cipher.getInstance("AES");
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] utf8;
		byte[] enc = null;
		try {
			utf8 = read_key.getBytes("UTF8");
	        enc = ecipher.doFinal(utf8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] skencoded = Base64.encodeBase64( enc );
		encodedText = new String(skencoded);

	}
	
	@Override	
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

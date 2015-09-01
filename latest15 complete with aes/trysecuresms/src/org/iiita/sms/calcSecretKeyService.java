package org.iiita.sms;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import org.apache.commons.codec.binary.Base64;

public class calcSecretKeyService extends Service {

	private boolean sender;
	private Object mPhoneNumber;
	private String[] list_files;	
	private FileInputStream fin;
	private String prstring;
	private String pbstring;
	private byte[] prkBytes;
	private byte[] pbkBytes;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private SecretKey secretKey;
	private byte[] skBytes;
	private String file_text;
	private String start;
	private String prefix;

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside calcSecretKeyService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        

		Bundle extras = intent.getExtras();			    
	    sender = extras.getBoolean("sender");	    	   
// remove this if-else it will common to sender and receiver to calculate sk
	    Toast.makeText(this,"sender is:" + sender, Toast.LENGTH_SHORT).show();
	    if( sender == true ){	    		    	
	    	calculateSk();
	    	prefix = "to";
	    	//call_encrypt();	    	    		    	
	    }else{
	    	prefix = "fr";
	    	calculateSk();
	    	//Toast.makeText(this,"secret key will be generated at runtime", Toast.LENGTH_SHORT).show();
	    }
	    
	    return START_STICKY;
	    
	}
	
	private void call_encrypt() {
		// TODO Auto-generated method stub
		//******************call for encryption***************on sender side
		Intent i = new Intent(getApplicationContext(),encryptService.class);				
		i.putExtra("skey", file_text);
		startService( i );
		
	}

	private void calculateSk() {
		// TODO Auto-generated method stub
		
		Toast.makeText(this,"****** inside calcsk() *****", Toast.LENGTH_SHORT).show();
		
		list_files = getApplicationContext().fileList();
		for( String s : list_files ){
			Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
			
			String strLine;
 			String read_key = "";
 			
 			if( s.endsWith("pr") == true )
 				Toast.makeText(this,"end: pr", Toast.LENGTH_SHORT).show();
 			if( s.endsWith("pb") == true )
 				Toast.makeText(this,"end: pb", Toast.LENGTH_SHORT).show();
 			
			try{

				if( s.endsWith("pb") == true || s.endsWith("pr") == true  ){										
					
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
	 			
	 			if( s.endsWith("pr") == true )
	 				prstring = read_key;
	 			else if( s.endsWith("pb") == true )
	 				pbstring = read_key;
	 				 			
	 			getApplicationContext().deleteFile(s);
	 			Toast.makeText(this, s + " deleted" , Toast.LENGTH_SHORT).show();
	 			
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
		
		Toast.makeText(this,"calcSectKey-pr: " + prstring, Toast.LENGTH_SHORT).show();
		Toast.makeText(this,"calcSectKey-pb: " + pbstring, Toast.LENGTH_SHORT).show();
		
		prkBytes = Base64.decodeBase64( prstring.getBytes() );
		pbkBytes = Base64.decodeBase64(pbstring.getBytes());
		
		Toast.makeText(this,"calcSectKey-prkBytes: " + prkBytes.toString(), Toast.LENGTH_SHORT).show();
		Toast.makeText(this,"calcSectKey-pbkBytes: " + pbkBytes.toString(), Toast.LENGTH_SHORT).show();
		
		
			    
		try {
			
			KeyFactory keyFact = KeyFactory.getInstance("DH");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pbkBytes);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(prkBytes);
			
			publicKey = keyFact.generatePublic(publicKeySpec);				
			privateKey = keyFact.generatePrivate(privateKeySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			 		
		
		Toast.makeText(this,"hi: "+publicKey.toString(), Toast.LENGTH_SHORT).show();
		Toast.makeText(this,"hi: "+privateKey.toString(), Toast.LENGTH_SHORT).show();
				
	    
		String algorithm = "AES";
		
		KeyAgreement ka = null ;
		try {
			ka = KeyAgreement.getInstance("DH");
			ka.init(privateKey);
		    ka.doPhase(publicKey, true);
		    secretKey = ka.generateSecret(algorithm);
		    
		    Toast.makeText(this,"hi: " + secretKey.toString(), Toast.LENGTH_SHORT).show();
			
		    skBytes = secretKey.getEncoded();
			byte[] skencoded = Base64.encodeBase64(skBytes);
			file_text = new String(skencoded);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this,"NO SUCH ALGO", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this,"INVALID KEY EXCEPTION", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}						
			
		Toast.makeText(this,"reached here safely", Toast.LENGTH_SHORT).show();												

		if( sender == false ){
		String FILENAME = "fr_sk";		

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, getApplicationContext().MODE_PRIVATE);
			fos.write(file_text.getBytes());
			fos.close();
			Toast.makeText(getApplicationContext(),"written to "+FILENAME, Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}
		}else{
			call_encrypt();
		}
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

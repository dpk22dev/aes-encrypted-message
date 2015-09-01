package org.iiita.sms;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import org.apache.commons.codec.binary.Base64;


public class generateKeyService extends Service {

	private String body;
	
	public static PrivateKey pr;
	public static PublicKey pb;
	public static byte[] prkbytes = null;
	public static byte[] pbkbytes = null;
	private static BigInteger p;
	private static BigInteger g;
	private static int l;	
	
	private String status;
	private String number;
	private String keytype;

	private String file_text;

	private byte[] readbytes;

	private String[] list_files;		

	public void onCreate() {
		super.onCreate();
		// how to get data from 		
		Toast.makeText(this,"Inside generateKeyService", Toast.LENGTH_SHORT).show();
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
		Bundle extras = intent.getExtras();		
	    status = extras.get("status").toString();	    
	    number = extras.get("number").toString();
	    keytype = extras.get("type").toString();
	    
	    //Toast.makeText(getApplicationContext(),status+number+keytype, Toast.LENGTH_SHORT).show();
	    if( status.compareTo("to") == 0 )
	    	generateKeys();
	    else if( status.compareTo("fr") == 0 )
	    	generateKeysRec();
	    
	    String FILENAME = status+"_"+number+"_"+keytype;
	    
	    FileOutputStream fos;	    
		try {
			fos = openFileOutput(FILENAME, getApplicationContext().MODE_PRIVATE);
// ********put prkbytes in file after BASE64encoding*********
			
			byte[] prkencoded = Base64.encodeBase64(prkbytes);
			file_text = new String(prkencoded);
			
			fos.write( file_text.getBytes() );
			fos.close();			
			System.out.println("written to "+FILENAME);
			System.out.println("before writing to file:" + file_text);
			Toast.makeText(getApplicationContext(),"show pr: " + file_text, Toast.LENGTH_SHORT).show();			
			
//			File file = getApplicationContext().getFileStreamPath(FILENAME);			
/* 			fin = openFileInput(FILENAME); 		
 			DataInputStream in = new DataInputStream(fin);
 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
 			
 			String strLine;
 			String read_key = "";
 			
 			while ((strLine = br.readLine()) != null)   {
 				  // Print the content on the console
 				 read_key += strLine;
 		    }
 			
//			fin.read( readbytes,0,(int)file.length());
			System.out.println("after reading from file:" + read_key );
			Toast.makeText(getApplicationContext(),read_key, Toast.LENGTH_SHORT).show();
			fin.close();
*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] pbkencoded = Base64.encodeBase64(pbkbytes);
		body = new String(pbkencoded);
		
//		Toast.makeText(getApplicationContext(),body, Toast.LENGTH_SHORT).show();
		System.out.println( "generateKeyService public encoded: "+body );
		
	    send_sms();
	
//*************** uncomment following to attain previous state ***************
	 // for on receiver side	    
	    if( status.compareTo("fr") == 0 ){	    	
	    	calculate_sk();
	    }
	    
		return START_STICKY;
	    
	}		

	private void send_sms() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),sendSMSService.class);		
		i.putExtra("number", number);
		if( status.compareTo("to") == 0 )
			body = "@pub" + body;
		else if( status.compareTo("fr") == 0 ){
			body = "@#pub" + body;			
		}
		i.putExtra("body",body);	
		startService( i );
	}

	private void calculate_sk() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(),calcSecretKeyService.class);		
		i.putExtra("sender", false);
		startService( i );
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void generateKeys( ){
		// Create the parameter generator for a 1024-bit DH key pair				
		
		try {
        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
        paramGen.init(128);        
        // Generate the parameters
        AlgorithmParameters params = paramGen.generateParameters();
        
		DHParameterSpec dhSpec = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);

		p = dhSpec.getP();
		g = dhSpec.getG();
		l = dhSpec.getL();
		
		// Use the values to generate a key pair
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
	    DHParameterSpec ndhSpec = new DHParameterSpec(p, g, l);
	    keyGen.initialize(ndhSpec);
	    KeyPair keypair = keyGen.generateKeyPair();

	    // Get the generated public and private keys
	    pr = keypair.getPrivate();
	    pb = keypair.getPublic();
	    
	    prkbytes = pr.getEncoded();
	    pbkbytes = pb.getEncoded();
			    	   	  
	    Toast.makeText(this,"private: "+prkbytes.toString(), Toast.LENGTH_SHORT).show();
	    Toast.makeText(this,"public: "+pbkbytes.toString(), Toast.LENGTH_SHORT).show();

	    System.out.println("generateKeyService private.toString(): "+ prkbytes.toString());
	    System.out.println("generateKeyService public.toString(): "+ pbkbytes.toString());
	    
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void generateKeysRec() {
		// TODO Auto-generated method stub
		
		String strLine;
		String read_key = "";		
		
		list_files = getApplicationContext().fileList();
		for( String s : list_files ){
			Toast.makeText(this,s, Toast.LENGTH_SHORT).show();						
 		
			read_key = "";
			
 			try{
 			if( s.startsWith("fr") && s.endsWith("pb") == true ){
 				
 				FileInputStream fin = openFileInput( s ); 		
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
		
		//readkey has public key
		byte[] readPbkBytes = Base64.decodeBase64(read_key.getBytes());
		PublicKey readPublicKey = null;
		
		try {
			KeyFactory keyFact = KeyFactory.getInstance("DH");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(readPbkBytes);
			readPublicKey = keyFact.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DHParameterSpec dhParamSpec = ((DHPublicKey)readPublicKey).getParams();

        // Bob creates his own DH key pair
        System.out.println("BOB: Generate DH keypair ...");
        KeyPairGenerator bobKpairGen;
        KeyPair bobKpair = null;
		try {
			bobKpairGen = KeyPairGenerator.getInstance("DH");
			bobKpairGen.initialize(dhParamSpec);
	        bobKpair = bobKpairGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//generate pr and pb key from bobKpair
		pr = bobKpair.getPrivate();
	    pb = bobKpair.getPublic();
	    
	    prkbytes = pr.getEncoded();
	    pbkbytes = pb.getEncoded();
		
	    Toast.makeText(this, "rec: "+prkbytes.toString() , Toast.LENGTH_SHORT).show();
	    Toast.makeText(this, "sen: "+pbkbytes.toString() , Toast.LENGTH_SHORT).show();
			    
	}

}

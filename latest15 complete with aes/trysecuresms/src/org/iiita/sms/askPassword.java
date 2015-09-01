package org.iiita.sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class askPassword extends Activity {
    /** Called when the activity is first created. */
	
	private String old_passwd = "";
	private String new_passwd = "";
	private String retype_passwd = "";
	 
	private String show = "";
	private boolean warn = false;
	private String FILE_NAME = "passfile";
	private EditText old_edit;
	private File file;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.askmaster);

//check if passfile exists or not changed getContext to getBaseContext
        final File file = getBaseContext().getFileStreamPath(FILE_NAME);
        if( file.exists() == false ){
           	old_edit = (EditText) findViewById(R.id.old_pass);
        	old_edit.setEnabled(false);
//        	Toast.makeText(this,"file doesn't exists", 
//                    Toast.LENGTH_SHORT).show();

        }       
        
//change id : master_password        
        Button okButton = (Button) findViewById(R.id.ok_pass);        
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {            	
            	try {
            		if( file.exists() == true ){
            			old_passwd = ((EditText) findViewById(R.id.old_pass)).getText().toString();
            			check();
            		}
				} catch (GeneralSecurityException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	new_passwd = ((EditText)findViewById(R.id.new_pass)).getText().toString();            	
            	retype_passwd = ((EditText)findViewById(R.id.retype_pass)).getText().toString();
            	
//            	Toast.makeText(askPassword.this,new_passwd + "  " + retype_passwd, 
//    	                Toast.LENGTH_LONG).show();
            	
            	check_length();
            	match();
            	//do changes in passfile
            	if( warn == false ){
            		
            		//
            		String temp = null;
            		try {
						temp = hash( new_passwd );
					} catch (GeneralSecurityException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
            		FileOutputStream fos = null;
					try {
						fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.print("File not found");
						e.printStackTrace();
					}
            		try {
						fos.write(temp.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.print("conversion to bytes problem");
						e.printStackTrace();
					}
            		try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.print("File could not be closed");
						e.printStackTrace();
					}
            		Toast msg = Toast.makeText(askPassword.this, "Master password changed", Toast.LENGTH_SHORT);

            		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

            		msg.show();

            		finish();
            		
            	}else{
            		//if warn is true show warnings
            		Toast msg = Toast.makeText(askPassword.this, show , Toast.LENGTH_SHORT);

            		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

            		msg.show();
            		show = "";
//            		old_edit = (EditText) findViewById(R.id.old_pass);
                	old_edit.setText("");
                	((EditText)findViewById(R.id.new_pass)).setText("");
                	((EditText)findViewById(R.id.retype_pass)).setText("");                	
            		
            	}	
            }           
            
            //finish();
            
        });
        
        Button cancelButton = (Button) findViewById(R.id.cancel_pass);        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	finish();
            }
        });
        
        
    }

	protected void match() {
		// TODO Auto-generated method stub
		if( new_passwd.compareTo(retype_passwd) == 0 ){		
//			Toast.makeText(this,"passwords are equal", 
//	                Toast.LENGTH_LONG).show();
		}else{
			warn = true;
//			Toast.makeText(this,new_passwd + "  " + retype_passwd, 
//	                Toast.LENGTH_LONG).show();
			show += "\nNew and retyped password don't match";
		}
	}

	protected void check_length() {
		// TODO Auto-generated method stub
		if( new_passwd.length() < 5 ){
			warn = true;
			show += "\nNew password is too short";
		}
		
	}

	protected void check() throws GeneralSecurityException {
		// TODO Auto-generated method stub
		//code
		String a = null, b;
		BufferedReader input = null;
		try {
		  input = new BufferedReader(new InputStreamReader(
			openFileInput(FILE_NAME)));
		  String line;

		  StringBuffer buffer = new StringBuffer();
		  while ((line = input.readLine()) != null) {
			buffer.append(line);	//appending eol may give error			
		  }
		  a = buffer.toString();
		} catch (Exception e) {
		 	e.printStackTrace();
		} finally {
		if (input != null) {
		  try {
			input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
		
		//code ends for input from file
		
		//code for conversion of old_passwd into hash
		b = hash( old_passwd );
		//
        
        if( a.compareTo(b) != 0 ){        	
        	warn = true;
        	show += "\nOld Password doesn't match";
        }
//        Toast.makeText(this,"file value:" + a + " b: " + b, 
//                Toast.LENGTH_LONG).show();
	}
	
	private String hash(String p) throws GeneralSecurityException {
		// TODO Auto-generated method stub
		String password = p;
		 
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
		return sb.toString();
	}
	
}
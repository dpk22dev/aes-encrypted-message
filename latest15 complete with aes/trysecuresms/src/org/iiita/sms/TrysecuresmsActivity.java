package org.iiita.sms;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrysecuresmsActivity extends Activity {
    /** Called when the activity is first created. */
	private String user_pass;
	private String FILE_NAME = "passfile";
	private File file;
	private boolean allow;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button masterButton = (Button) findViewById(R.id.master_password);        
        masterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	askMasterPassword();
            }
        });
        
        Button memoryButton = (Button) findViewById(R.id.memory);        
        memoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	generateRandom();
            }
        });        
        
        Button composeButton = (Button) findViewById(R.id.compose);        
        composeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	inputSMSBody();
            }
        });
        
        Button viewButton = (Button) findViewById(R.id.view_messages);        
        viewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	//
            	//Context mContext = getApplicationContext();
            	final Dialog dialog = new Dialog(TrysecuresmsActivity.this);

            	dialog.setContentView(R.layout.passworddialog);
            	dialog.setCancelable(true);
            	Button cancelButton = (Button)dialog.findViewById(R.id.pressed_cancel);        
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                    	finish();
                    }
                });
            	//
            	//
                Button okButton = (Button)dialog.findViewById(R.id.pressed_ok);        
                okButton.setOnClickListener(new View.OnClickListener() {                	
                	public void onClick(View view) {
                    	user_pass = ((EditText)dialog.findViewById(R.id.entered_pass)).getText().toString();
                		try {
							check();
							take_action();
						} catch (GeneralSecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                	
                	private void take_action() {
						// TODO Auto-generated method stub
                		if( allow == true){
    						//go to next step
//*************** show GUI by LALA ***********                			
    					}else{
    						Toast.makeText(getApplicationContext(),"Wrong Password", 
                                  Toast.LENGTH_SHORT).show();
    					}
						//
					}
					//
                	
                	//
					private void check() throws GeneralSecurityException {
						// TODO Auto-generated method stub
						String a = null, b;
                		BufferedReader input = null;
                		try {
                		  file = new File(FILE_NAME);
                		  if( file.exists() == false ){
                			  allow = true;
                			  return ;
                		  }
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
                		
                		
                		b = hash( user_pass );
                		
                        
                        if( a.compareTo(b) != 0 ){        	
                        	allow = false;
                        }else{
                        	allow = true;
                        }
						//
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
					
            	});
                //
                dialog.show();
            }
        });
        
    }

	protected void generateRandom() {
		// TODO Auto-generated method stub
		//Intent i = new Intent(this, tempService.class);
		//startService( i );
	}

	protected void inputSMSBody() {
		// TODO Auto-generated method stub
		Intent i = new Intent( this, inputText.class);
        startActivity(i);
	}

	protected void askMasterPassword() {
		// TODO Auto-generated method stub
		Intent i = new Intent( this, askPassword.class);
        startActivity(i);
	}
}
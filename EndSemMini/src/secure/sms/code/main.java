package secure.sms.code;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class main extends Activity {
	
	
	public String data;	
	//TextView user;
	TextView passwd;
	//EditText user_name ;	
	EditText password;	
	Button Ok;	
	Button ChangePassword;	
	Button receivedButton;
	
	public static int passwd_length = 5;
		   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    
        LinearLayout layout = new LinearLayout(this);        
        layout.setOrientation(LinearLayout.VERTICAL); 
                
/*        user = new TextView(this);
        user.setText("Username");
        layout.addView(user , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        //	adding EditText user_name to activity	
        user_name = new EditText(this);
        user_name.setText("");
        layout.addView(user_name , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
*/
        /**	adding EditText passwd to activity */
        passwd = new TextView(this);
        passwd.setText("Enter Master Password : ");
        layout.addView(passwd , new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        /** adding EditText password to activity */
        password = new EditText(this);
        password.setTransformationMethod(new android.text.method.PasswordTransformationMethod()); 
        password.setText("");
        layout.addView(password, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        
        Ok = new Button(this);
        Ok.setText("Log In");
              
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boolean fg  = true;
            	FileInputStream fIn = null;	/** InputStream for reading file */
         	   InputStreamReader isr = null;/** InputStreamReader for reading file */
      
         	   try{
         		   //System.out.println("File writing done");
         	   char[] inputBuffer = new char[passwd_length];

         	   data = null;
         	   System.out.println("File reading ");
         	   fIn = openFileInput("public.dat");	/** Opening File */
         	   System.out.println("File present");
         	   isr = new InputStreamReader(fIn);

         	   isr.read(inputBuffer);
         	   System.out.println("File reading done");

         	   data = new String(inputBuffer); /** Reading file */
         	   System.out.println("Data in the file :: \n"+ data);
         	   isr.close();

         	   fIn.close();
         	   
         	   //Toast.makeText(HelloAndroid.this, "File Read " + data, Toast.LENGTH_SHORT).show();
         	   }
         	   catch(Exception e){
         		   
         		   fg = false;
         		   e.printStackTrace(System.err);
         		   System.out.println("tehere");
         	   }
         	   if(!fg)
         	   {
         		   /**
         		    * 	Default passsowrd
         		    */
            	//System.out.println("check "+user_name.getText().toString());
                if ( password.getText().toString().equals("admin")){
                	System.out.println("User Confirmed");
                	Toast.makeText(getBaseContext(), "Welcome" , Toast.LENGTH_SHORT).show();
                	
                	//user_name.setText("");
                	password.setText("");
                                      	              
                	
                	Intent i = new Intent();
                	i.setClassName("secure.sms.code", "secure.sms.code.send_sms");
                	startActivity(i);                	                
                	
                	
                }else{
                	System.out.println(" Security attack or Login unsucessfull ");
                	Toast.makeText(getBaseContext(), "Login Unsuccessfull" , Toast.LENGTH_SHORT).show();
                }
         	   }
         	   else
         	   {
         		   
         		  if ( password.getText().toString().equals(data)){
                  	System.out.println("User Confirmed");
                  	Toast.makeText(getBaseContext(), "Welcome" , Toast.LENGTH_SHORT).show();
                  	
                  	//user_name.setText("");
                  	password.setText("");                         
                  	
                  	Intent i = new Intent();
                  	i.setClassName("secure.sms.code", "secure.sms.code.send_sms");
                  	startActivity(i);                  	                  	
                  	
                  	
                  }else{
                  	System.out.println(" Security attack or Login unsucessfull ");
                  	Toast.makeText(getBaseContext(), "Login Unsuccessfull" , Toast.LENGTH_SHORT).show();
                  }
        	
         	   }
            }
        });

        
        layout.addView(Ok, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        /** Adding Button ChangePassword in activity */
        ChangePassword = new Button(this);
        ChangePassword.setText("Change Master Password");
       
        /** Setting ClickEvent of Button ChangePassword in activity */
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boolean fg  = true;
            	FileInputStream fIn = null;	/** InputStream to Read file */
         	    InputStreamReader isr = null;	/** InputStreamReader to Read file */

         	    
         	   try{
         		  // System.out.println("File writing done");
         	   char[] inputBuffer = new char[passwd_length];

         	   data = null;
         	   System.out.println("File reading ");
         	   fIn = openFileInput("public.dat");
         	   System.out.println("File present");
         	   isr = new InputStreamReader(fIn);

         	   isr.read(inputBuffer);
         	   System.out.println("File reading done");

         	   data = new String(inputBuffer);
         	   System.out.println("Data in the file :: \n"+ data);
         	   isr.close();

         	   fIn.close();
         	   //Toast.makeText(HelloAndroid.this, "File Read " + data, Toast.LENGTH_SHORT).show();
         	   }
         	   catch(Exception e){
         	   
         		   fg = false;
         		   e.printStackTrace(System.err);
         		   System.out.println("tehere");
         	   }
         	   if(!fg)
         	   {
            	//System.out.println("check "+user_name.getText().toString());
                if ( password.getText().toString().equals("admin")){
                	System.out.println("User Confirmed");
                	//Toast.makeText(getBaseContext(), " Successfully" , Toast.LENGTH_SHORT).show();
                	
                	//user_name.setText("");
                	password.setText("");                
                	Intent i1 = new Intent();
                	i1.setClassName("secure.sms.code", "secure.sms.code.passwd_change");
                	startActivity(i1);
                }
                else
                {
                	System.out.println(" Security attack or Login unsucessfull ");
                  	Toast.makeText(getBaseContext(), "Wrong Master Password" , Toast.LENGTH_SHORT).show();
                }
                
              	
         	   }
         	   else
         	   {
         		  if ( password.getText().toString().equals(data)){
                  	System.out.println("User Confirmed");
                  	Toast.makeText(getBaseContext(), "" + "clicked Successfully" , Toast.LENGTH_SHORT).show();
                  	
                  	//user_name.setText("");
                  	password.setText("");
                  
                  	Intent i1 = new Intent();
                  	i1.setClassName("secure.sms.code", "secure.sms.code.passwd_change");
                  	startActivity(i1);
                  }
                  else
                  {
                  	System.out.println(" Security attack or Login unsucessfull ");
                    	Toast.makeText(getBaseContext(), "Wrong Master Password" , Toast.LENGTH_SHORT).show();
                  }
         	   }
            }
        });

        
        layout.addView(ChangePassword, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));

        //add button received messages in activity
        /** Adding Button ChangePassword in activity */
        receivedButton = new Button(this);
        receivedButton.setText("Received Messages");
       
        /** Setting ClickEvent of Button ChangePassword in activity */
        receivedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boolean fg  = true;
            	FileInputStream fIn = null;	/** InputStream to Read file */
         	    InputStreamReader isr = null;	/** InputStreamReader to Read file */

         	    
         	   try{
         		  // System.out.println("File writing done");
         	   char[] inputBuffer = new char[passwd_length];

         	   data = null;
         	   System.out.println("File reading ");
         	   fIn = openFileInput("public.dat");
         	   System.out.println("File present");
         	   isr = new InputStreamReader(fIn);

         	   isr.read(inputBuffer);
         	   System.out.println("File reading done");

         	   data = new String(inputBuffer);
         	   System.out.println("Data in the file :: \n"+ data);
         	   isr.close();

         	   fIn.close();
         	   //Toast.makeText(HelloAndroid.this, "File Read " + data, Toast.LENGTH_SHORT).show();
         	   }
         	   catch(Exception e){
         	   
         		   fg = false;
         		   e.printStackTrace(System.err);
         		   System.out.println("tehere");
         	   }
         	   if(!fg)
         	   {
            	//System.out.println("check "+user_name.getText().toString());
                if ( password.getText().toString().equals("admin")){
                	System.out.println("User Confirmed");
                	//Toast.makeText(getBaseContext(), " Successfully" , Toast.LENGTH_SHORT).show();
                	
                	//user_name.setText("");
                	password.setText("");
                
                	/**
                	 * 		Going to call Activity to change password
                	 */
                	Intent i1 = new Intent();
//*********************                	************* change activity ************
                	i1.setClassName("secure.sms.code", "secure.sms.code.display_sender");
                	startActivity(i1);
                }
                else
                {
                	System.out.println(" Security attack or Login unsucessfull ");
                  	Toast.makeText(getBaseContext(), "Wrong Master Password" , Toast.LENGTH_SHORT).show();
                }
                
              	
         	   }
         	   else
         	   {
         		  if ( password.getText().toString().equals(data)){
                  	System.out.println("User Confirmed");
                  	//Toast.makeText(getBaseContext(), "" + "clicked Successfully" , Toast.LENGTH_SHORT).show();
                  	
                  	//user_name.setText("");
                  	password.setText("");
                  
                  	Intent i1 = new Intent();
//*************************** change activity ****************                  	
                  	i1.setClassName("secure.sms.code", "secure.sms.code.display_sender");
                  	startActivity(i1);
                  }
                  else
                  {
                  	System.out.println(" Security attack or Login unsucessfull ");
                  	password.setText("");
                    	Toast.makeText(getBaseContext(), "Wrong Master Password" , Toast.LENGTH_SHORT).show();
                  }
         	   }
            }
        });

        
        layout.addView(receivedButton, new LinearLayout.LayoutParams( 
        		ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        //

        setContentView(layout);
        
    }
}
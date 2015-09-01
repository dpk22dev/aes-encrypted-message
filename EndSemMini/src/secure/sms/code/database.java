package secure.sms.code;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class database {
	
	   private static final String DATABASE_NAME = "Secure_SMS.db";		
	   private static final int DATABASE_VERSION = 1;	  
	   public static String TABLE_NAME ;	   
	   private static final String TABLE_SENDER = "TABLE_SENDER";
	   
	   private Context context;	   
	   private SQLiteDatabase db;
	   	  	   	  
	   private SQLiteStatement insertStmt_sender;
	   	  
	   private static final String INSERT_SENDER = "insert into " + TABLE_SENDER + "(phone) values(?)";
	   
	   /**
	    *	Constructor of database 
	    * @param context :  caller of the database constructor 
	    */
	   public database(Context context) {
		   System.out.println("In database Constructor"); 
		      
		   	  this.context = context;
		   	  /** Creating Database */
		      data_creater DB_CR = new data_creater(this.context);
		      this.db = DB_CR.getWritableDatabase();
		      
		      System.out.println("After geting database"); 
		      
		//      this.insertStmt = this.db.compileStatement(INSERT);
		      /** Getting precomplied statement of insertStmt_sender */
		      this.insertStmt_sender = this.db.compileStatement(INSERT_SENDER);
		      
		      System.out.println("Database Created in constructor of DataHelper"); 
		   }
		   
	   		/**
	   		 * 	accesor of db
	   		 * @return
	   		 */
		   public SQLiteDatabase getDb() {
		      return this.db;
		   }

		   /**
		    * 	Creating a table with the name of sender number
		    * @param Table : Sender phone number
		    */
		   public void create_sender ( String Table ){
			   /** Executing Query */
			   db.execSQL("CREATE TABLE " + "Table_" + Table + " (id INTEGER PRIMARY KEY autoincrement ,message TEXT , key TEXT,voice_tag INTEGER )");
		   }
		   
		   /**
		    * 	inserting the messsage in table of sender
		    * 
		    * @param message : encrypted message
		    * @param key : key with which it is encrypted
		    * 
		    */
		   public long insert(String message , String key) {
			   System.out.println("hello inside insert");
				  // db.execSQL("insert into " + TABLE_NAME + " values(" + message + "," + key + ")" );
				   ContentValues initialValues = new ContentValues();
			        initialValues.put("message", message);
			        initialValues.put("key", key);
			        initialValues.put("voice_tag",0);
			        System.out.println("hello inside inserting values putting into table  " + message);
			        return db.insert(TABLE_NAME, null, initialValues); 
		   }
		   
		   /**
		    * 	Insering a sender in sender table
		    * 
		    * @param phone : phone number of sender
		    * 
		    */
		   public long insert(String phone) {
			      this.insertStmt_sender.bindString(1, phone);
			      
			      return this.insertStmt_sender.executeInsert();
			   }
		   
		   /**
		    * Deleting all the entries from TABLE_NAME
		    */
		   public void deleteAll() {
		      this.db.delete(TABLE_NAME, null, null);
		   }
		   
		   /**
		    * Deleting a tupple from TABLE_NAME
		    * @param Rid : primary key of tuppel to be deleted
		    * 
		    */
		   public boolean deleterow(int Rid)
		   {
			   return db.delete(TABLE_NAME, Rid + " = id" , null) > 0;
		   }

		   /**
		    * 	Getting all tupples of TABLE_NAME
		    * @return : List containg all tupples of TABLE_NAME
		    */
		   public List<message_record> selectAll() {
		      List<message_record> list = new ArrayList<message_record>();
//*********************** edited may be required to be changed *******//
		      //list = null;
		      //
		      /** Cursor to fetch all records */
		      Cursor cursor = this.db.query(TABLE_NAME, new String[] {"id","message" , "key","voice_tag"}, null, null, null, null, null);
		      /**	Inserting all the cursor values to return list	*/
		      if (cursor.moveToFirst()) {
		         do {
		        	 message_record mr = new message_record();
		        	 mr.id = Integer.parseInt(cursor.getString(0));
		        	 mr.message = cursor.getString(1);
		        	 mr.key = cursor.getString(2);
		        	 mr.voice_tag = Integer.parseInt(cursor.getString(3));
		        	 list.add(mr); 
		         } while (cursor.moveToNext());
		      }
		      if (cursor != null && !cursor.isClosed()) {
		         cursor.close();
		      }
		      return list;
		   }

		   /**
		    * 	Getting All tupples of TABLE_SENDER
		    * @return : List Containg all tuples of TABLE_SENDER
		    */
		   public List<String> selectAll_sender() {
			      List<String> list = new ArrayList<String>();
			      /** Cursor for getting all senders */
			      Cursor cursor = this.db.query(TABLE_SENDER, new String[] { "phone" }, null, null, null, null, null);
			      /** Inserting all senders to return list */
			      if (cursor.moveToFirst()) {
			         do {
			            list.add(cursor.getString(0)); 
			         } while (cursor.moveToNext());
			      }
			      if (cursor != null && !cursor.isClosed()) {
			         cursor.close();
			      }
			      return list;
			   }
		   
		   /**
		    * 	Class for getting database and setting and upgrading updatabase 
		    *
		    */
		   private static class data_creater extends SQLiteOpenHelper {

		      data_creater(Context context) {
		         super(context, DATABASE_NAME, null, DATABASE_VERSION);
		      }

		      @Override
		      public void onCreate(SQLiteDatabase db) {
		    	  /** Creating  Table TABLE_SENDER */
		         db.execSQL("CREATE TABLE " + TABLE_SENDER + " (phone TEXT PRIMARY KEY)");
		      }

		      @Override
		      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		         db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDER);
		         onCreate(db);
		      }
		   }
	   
}

package com.dalayach.WOT_PlayerComp_V1;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import com.cedarsoftware.util.io.JsonWriter;

import java.util.Scanner;
import javax.swing.JOptionPane;


/**  <p>This program is designed to inform the user of their percentage of chance to be able to beat an enemy player (on equal ground and in the same tank) in the game World Of Tanks based off of several factors such as battles won and average damage</p>
 *   <p>Last updated - August 20 2018</p>
 *   <p>Update status: Incomplete</p>
 *   
 *   
 *   @author David Alayachew
 *   @version 0.1
 */
   
   //Links
   //https://developers.wargaming.net/reference/all/wot/account/list/?application_id=4cb32cc3859abbef155c5c3d49d4b52b&r_realm=na
   //https://developers.wargaming.net/reference/all/wot/account/info/?application_id=4cb32cc3859abbef155c5c3d49d4b52b&r_realm=na


//driver class
class WOT_PlayerComp_V1 
{

   private final String APP_ID = "4cb32cc3859abbef155c5c3d49d4b52b";
   private final int LIMIT = 1;
   private Scanner scan;
   private boolean program_Working = true;      
   
   private String response_from_URL = "";
   
   private String username;
   private String user_account_ID;
   private JSONObject user_Object;
   
   
   
   private final String DEV_EMAIL = "davidalayachew@gmail.com";
   private final String USERNAME_DOESNT_EXIST = "That username does not exist.\nCheck for spelling, including proper case and no spaces.\n";
   private final String THIS_IS_A_BUG = "This is a bug. Please contact the developer at " + DEV_EMAIL + " and this issue will be resolved.\nThank you for your cooperation.\n";
   private final String SEARCH_WAS_NOT_SUCCESSFUL = "Search was not successful, please pick a different name.\nRemember, usernames must be at least 3 characters long.\n";
   private final String ERROR = "\n!! ERROR !!\n";
   private final String URL_DID_NOT_WORK = "It seems that the URL did not work.\n" + THIS_IS_A_BUG;
   private final String ERROR_IN_STREAM = "It seems there was an error in opening the stream for the URL" + THIS_IS_A_BUG;

   WOT_PlayerComp_V1()
   {
   
      set_up();
      
      do{
      
         program_Working = ensure_we_have_proper_username();
      
      }while(program_Working == false);
      
      this.user_account_ID = retrieve_account_id(this.user_Object);
      print("Here is your userID - " + this.user_account_ID + "\n\n");
   
   }
   
   
   void print(String message)
   {//this method will be a godsend when it comes time to upgrade to V2
   
      System.out.println(message);
   
   }
   
   void set_up()
   {
   
      this.scan = new Scanner(System.in);
   
   }
   
   void pretty_Print(String text)
   {
   
      String temp = JsonWriter.formatJson(text);
      print("" + temp);
   
   }
   
   boolean ensure_we_have_proper_username()
   {
   
      boolean result = true;//did the method succeed?
   
      String temp;
      String status;
      JSONObject transfer = new JSONObject();
      int count;//the number of names that the search returned
   
      print("Enter your username.");//prompts user to enter their username
      this.username = this.scan.next();//scans user input
         
      result = read_URL(
         "http://api.worldoftanks.com/wot/account/list/?application_id=" + this.APP_ID
         + "&search=" + this.username
         + "&limit=" + this.LIMIT
         );//reads url to get the JSON data
      
      this.user_Object = JSONObject.fromObject(this.response_from_URL);//build a JSONObject
      
      status = this.user_Object.getString("status");//get status of search
         
      if(status.equals("ok"))//if the search was properly executed
      {
            
         transfer = this.user_Object.getJSONObject("meta");
         count = transfer.getInt("count");   
         
         if(count == 0)//if no names match up with input
         {
            
            result = false;
            print(USERNAME_DOESNT_EXIST);
            
         }
         
      }
         
      else if(status.equals("error"))
      {
                  
         print(ERROR);
         print(SEARCH_WAS_NOT_SUCCESSFUL);
         result = false;
                     
      }
      
      else
      {
      
         result = false;
         print(THIS_IS_A_BUG);
      
      }
   
      return result;
   
   }
   
   String retrieve_account_id(JSONObject object)
   {
          
      String result = "";
                  
      /** json array is made from data */
      JSONArray dataArray = (object.getJSONArray("data"));
      /** 1st element from array of JSONObjects is stored into a JSONObject */
      JSONObject data = dataArray.getJSONObject(0);
      
      result = data.getString("account_id");
   
      return result;
   
   }
   
   /** will store JSON data into this.response_from_URL
    *  @return boolean to tell whether method succeeded or failed
    *
    */
   boolean read_URL(String webservice)
   {//TODO -- add functionality to trigger uniquely for each of the
    //possible exceptions
      
      String input_Line;
      boolean result = true;
          
      try
      {
      
         URL oracle = new URL(webservice);
      
         BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
      
         this.response_from_URL = "";//must clear response so that we start anew each time
      
         while ((input_Line = in.readLine()) != null)
         {
         
            this.response_from_URL = this.response_from_URL + input_Line;
         
         }
      
         in.close();
         result = true;
      
      }
      catch(MalformedURLException murle)
      {
      
         print(URL_DID_NOT_WORK);
         result = false;
      
      }
      catch(IOException ioe)
      {
      
         print(ERROR_IN_STREAM);
         result = false;
      
      }
      
      return result;
      
   }


   public static void main(String[] args)
   {
   
      WOT_PlayerComp_V1 wot = new WOT_PlayerComp_V1();
   
   }

}
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
   
//Sources for methods 
//http://na.wargaming.net/developers/api_explorer/wot/account/list/?application_id=4cb32cc3859abbef155c5c3d49d4b52b&http_method=GET
//http://na.wargaming.net/developers/api_reference/wot/account/list/
//http://na.wargaming.net/developers/api_reference/wot/account/info/


//driver class
class WOT_PlayerComp_V1 
{

   private final String APP_ID = "4cb32cc3859abbef155c5c3d49d4b52b";
   private final int LIMIT = 1;
   private Scanner scan;
   private boolean status = true;      
   
   private String response_from_URL = "";
   
   private String username;
   private String user_account_ID;
   private JSONObject user_object;

   WOT_PlayerComp_V1()
   {
   
      set_up();
      ensure_we_have_proper_username();
   
   }
   
   void set_up()
   {
   
      this.scan = new Scanner(System.in);
   
   }
   
   void ensure_we_have_proper_username()
   {
   
      String temp;
   
      System.out.println("Enter your username.");//prompts user to enter their username
      this.username = this.scan.next();//scans user input
         
      this.status = read_URL(
         "http://api.worldoftanks.com/wot/account/list/?application_id=" + this.APP_ID
         + "&search=" + this.username
         + "&limit=" + this.LIMIT
         );//reads url to get the JSON data
      
      // oppInfo = transfer;//transfers info to userinfo
      // z = JSONObject.fromObject(oppInfo);//Stores info as a jsonobject
      this.user_object = JSONObject.fromObject(this.response_from_URL);
      temp = JsonWriter.formatJson(this.user_object.toString());
      System.out.println("" + temp);
      // status = "";//status is emptied
      // status += z.get("status");//status will hold "ok" if the search was properly executed
   //       
      // if(status.equals("ok"))//if the search was properly executed
      // {
      //       
         // transfer = "";//transfer is emptied
         // JSONObject myMeta = z.getJSONObject("meta");
         // transfer += myMeta.get("count");//transfer holds count which holds the number of possible names that would match up to input   
         // count = Integer.parseInt(transfer);//turns count into a number to be better handled
      //    
      // }
   //       
      // if(!(status.equals("ok")))//if the search was not properly executed
      // {
      //    
         // if(status.equals("error")){System.out.println("\n\nIT WAS AN ERROR\n\n");}
      //    
         // System.out.println("Search was not successful, please pick a different name. Remember, usernames must be at least 3 characters long.");//inform the user that the search was not properly executed
         // myNameCorrectlyInputted = false;//set checker to false, so that the while loop will make it run back again
      //    
      // }
      //    
      // else if(count == 0)//if no names match up with input
      // {
      //       
         // myNameCorrectlyInputted = false;//prevent user from exiting loop
         // System.out.println("That username does not exist. Check for spelling, including proper case and no spaces.");//inform user of bad input
      //       
      // }
   
   
   }
   
   /** will store JSON data into this.response_from_URL
    *  @return boolean to tell whether method succeeded or failed
    *
    */
   boolean read_URL(String webservice)
   {//TODO -- add functionality to trigger uniquely for each of the
    //possible exceptions
      
      String input_Line;
      boolean result;
          
      try
      {
      
         URL oracle = new URL(webservice);
      
         BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
      
         while ((input_Line = in.readLine()) != null)
         {
         
            this.response_from_URL = this.response_from_URL + input_Line;
         
         }
      
         in.close();
         result = true;
      
      }
      catch(MalformedURLException murle)
      {
      
         System.out.println("It seems that the URL did not work.");
         result = false;
      
      }
      catch(IOException ioe)
      {
      
         System.out.println("It seems there was an error in opening the stream for the URL");
         result = false;
      
      }
      
      System.out.println("\n\nYAYAYAYA\n\n");
      return result;
      
   }


   public static void main(String[] args)
   {
   
      WOT_PlayerComp_V1 wot = new WOT_PlayerComp_V1();
   
   }

}
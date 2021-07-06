package com.dalayach.WOT_PlayerComp_V1;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.json.JsonArray;
import javax.json.JsonObject;

import java.util.Scanner;
import javax.swing.JOptionPane;


/** <p>This program is designed to inform the user of their percentage of chance to be able to
 *  beat an enemy player (on equal ground and in the same tank) in the game World Of Tanks
 *  based off of several factors such as battles won and average damage</p>
 *   
 *   
 *   @author David Alayachew
 */
 
 //TODO
 //print_stats should only use the JsonObject
 //Separate into 2 classes - one for retrieving data, and another for manipulating/presenting
 //data


//driver class
class WOT_PlayerComp_V1 
{
   
   private final String APP_ID;
   private final int LIMIT = 1;
   private Scanner scan;
   
   private String response_from_URL = "";
   private JsonObject temp_object;
   private JsonArray temp_array;
   
   
   private String enemy_username;
   private String enemy_user_account_ID;
   private JsonObject enemy_user_object;        //holds superficial info, such as ID number, region, etc.
   private JsonObject enemy_user_stats;         //holds all the play records achieved by the enemy user
   private int enemy_user_battle_avg_xp;
   private int enemy_user_hits_percents;
   private int enemy_user_XP;
   private double enemy_user_damage_ratio;
   private double enemy_user_win_ratio;  
   private double enemy_user_chance = 0;   
   
   private final String DEV_EMAIL = "davidalayachew@gmail.com";
   private final String USERNAME_DOESNT_EXIST = "That username does not exist.\nCheck for spelling, including proper case and no spaces.\n";
   private final String THIS_IS_A_BUG = "This is a bug. Please contact the developer at " + DEV_EMAIL + " and this issue will be resolved.\nThank you for your cooperation.\n";
   private final String SEARCH_WAS_NOT_SUCCESSFUL = "Search was not successful, please pick a different name.\nRemember, usernames must be at least 3 characters long.\n";
   private final String ERROR = "\n!! ERROR !!\n";
   private final String URL_DID_NOT_WORK = "It seems that the URL did not work.\n" + THIS_IS_A_BUG;
   private final String ERROR_IN_STREAM = "It seems there was an error in opening the stream for the URL.\n" + THIS_IS_A_BUG;
   private final String COMPARE_TO_SELF = "\n\nWould you like to compare these statistics to yourself? (Y/N)";
   private final String RESEARCH_ANOTHER_PLAYER = "\n\nWould you like to research another player? (Y/N)";

   WOT_PlayerComp_V1()
   {
         
      set_up();
      
      retrieve_user_ID();
      retrieve_user_data();
      print_stats(this.user_stats, this.username, this.user_damage_ratio, this.user_win_ratio);
               
      do
      {
      
         retrieve_enemy_user_ID();
         retrieve_enemy_user_data();
      
         print_stats(this.enemy_user_stats, this.enemy_username, this.enemy_user_damage_ratio, this.enemy_user_win_ratio);
         
         if(question(COMPARE_TO_SELF) == true)
         {
         
            compare_stats(this.user_stats, this.enemy_user_stats);
         
         }
         
      }while(question(RESEARCH_ANOTHER_PLAYER) == true);
         
   }

   boolean question(String prompt)
   {
   
      boolean result = true;
      boolean valid_response = false;
      String choice;
         
      do
      {
         
         print(prompt);
         choice = this.scan.next();            
         
         switch (choice)
         {
         
            case "y":
            case "Y":
            case "yes":
            case "Yes":
            case "YES":
               result = true;
               valid_response = true;
               break;
               
            case "n":
            case "N":
            case "no":
            case "No":
            case "NO":
               result = false;
               valid_response = true;
               break;
               
            case "q":
               valid_response = true;
               break;
               
            default:
               print("INVALID RESPONSE\nTry again.\n\n");
               break;
               
         
         }
      
      }while(valid_response == false);
   
            
      return result;
   
   }
   
   double calculate_points(double difference, double smaller_value)
   {
   
      double result;
   
      if(difference > smaller_value)//if the difference between traits is so great that
      {                             //one trait is over twice the size of the other
      
         result = 20;               //automatically award the highest point value possible
      
      }
      
      else
      {
      
         result = (difference + smaller_value)/(smaller_value);
         result *= 10;
      
      }
      
      return result;
   
   }
   
   void who_has_better(String trait, double user_trait, double enemy_trait)
   {
   
      double temp = 0;
   
      if(user_trait > enemy_trait)
      {
      
         temp = calculate_points((user_trait - enemy_trait), enemy_trait);
         print("YOU have a better " + trait + " than " + this.enemy_username);
         this.user_chance += temp;
         this.enemy_user_chance += 20 - temp;
      
      }
      
      else if(user_trait < enemy_trait)
      {
      
         temp = calculate_points((enemy_trait - user_trait), user_trait);
         print(this.enemy_username + " has a better " + trait + " than you");
         this.enemy_user_chance += temp;
         this.user_chance += 20 - temp;
      
      }
      
      else
      {
      
         print("SURPRISE! Both of you have the same " + trait + "!");
      
      }
   
   }
   
   String clean_up_trailing_decimal_digits(double big_number)
   {
   
      DecimalFormat numberFormat = new DecimalFormat("0.##");
      return (numberFormat.format(big_number));
   
   }
   
   void compare_stats(JsonObject p1, JsonObject p2)
   {
   
      String clean_user_chance;
   
      who_has_better("Average Battle Experience", p1.getDouble("battle_avg_xp"), p2.getDouble("battle_avg_xp"));
   
      who_has_better("Average Percentage of Hits per Shot", p1.getDouble("hits_percents"), p2.getDouble("hits_percents"));
   
      who_has_better("Lifetime EXP Total", p1.getDouble("xp"), p2.getDouble("xp"));
            
      who_has_better("Damage Dealt to Damage Received Ratio", this.user_damage_ratio, this.enemy_user_damage_ratio);
   
      who_has_better("Win Ratio", this.user_win_ratio, this.enemy_user_win_ratio);
                   
      clean_user_chance = clean_up_trailing_decimal_digits(this.user_chance);
                                            
      print("\nYou have a " + clean_user_chance + "% chance of winning.");//inform user of their chances of winning
                       
   
   }
   
   void print_stats(JsonObject player, String username, double damage_ratio, double win_ratio)
   {
   
      String clean_damage_ratio = clean_up_trailing_decimal_digits(damage_ratio);
      String clean_win_ratio = clean_up_trailing_decimal_digits(win_ratio);
   
      print("Here are the statistics for the player " + username 
            + "\n\n" + player.get("battle_avg_xp") + " = Average Battle Experience\n" 
            + player.get("hits_percents") + "% = Average Percentage of Hits per Shot\n"
            + player.get("xp") + " = Lifetime EXP Total\n"
            + clean_damage_ratio + "% = Damage Dealt to Damage Received Ratio\n"
            + clean_win_ratio + "% = Win Ratio\n\n");
   
   }
   
   void retrieve_user_data()
   {
    
      String temp;
         
      temp = retrieve_all_stats(this.user_account_ID);
      this.user_stats = JsonObject.fromObject(temp);
      
      this.user_battle_avg_xp = this.user_stats.getInt("battle_avg_xp");
      
      double wins = this.user_stats.getInt("wins");
   
      double draws = this.user_stats.getInt("draws");
   
      double losses = this.user_stats.getInt("losses");
         
      this.user_hits_percents = this.user_stats.getInt("hits_percents");
         
      this.user_XP = this.user_stats.getInt("xp");
   
      double damage_dealt = this.user_stats.getInt("damage_dealt");
   
      double damage_received = this.user_stats.getInt("damage_received");
   
      this.user_damage_ratio = damage_dealt/damage_received;
      this.user_damage_ratio *=100;
   
      this.user_win_ratio = (double)((double)(wins)/(double)(wins + draws + losses + 1));
      this.user_win_ratio *=100;    
              
   }
   
   void retrieve_enemy_user_data()
   {
    
      String temp;
         
      temp = retrieve_all_stats(this.enemy_user_account_ID);
      this.enemy_user_stats = JsonObject.fromObject(temp);
      
      this.enemy_user_battle_avg_xp = this.enemy_user_stats.getInt("battle_avg_xp");
      
      double wins = this.enemy_user_stats.getInt("wins");
   
      double draws = this.enemy_user_stats.getInt("draws");
   
      double losses = this.enemy_user_stats.getInt("losses");
         
      this.enemy_user_hits_percents = this.enemy_user_stats.getInt("hits_percents");
         
      this.enemy_user_XP = this.enemy_user_stats.getInt("xp");
   
      double damage_dealt = this.enemy_user_stats.getInt("damage_dealt");
   
      double damage_received = this.enemy_user_stats.getInt("damage_received");
   
      this.enemy_user_damage_ratio = damage_dealt/damage_received;
      this.enemy_user_damage_ratio *=100;
   
      this.enemy_user_win_ratio = (double)((double)(wins)/(double)(wins + draws + losses + 1));
      this.enemy_user_win_ratio *=100;    
              
   }
   
   String retrieve_all_stats(String ID)
   {
   
      read_URL("http://api.worldoftanks.com/wot/account/info/?application_id=" + this.APP_ID + "&account_id=" + ID);
      this.temp_object = JsonObject.fromObject(this.response_from_URL);//retrieve and store all user play records
      
                  //goes through and captures all info on opponent
         
         /** goes into the JsonObject to retrieve data */
      this.temp_object = this.temp_object.getJsonObject("data");
         /** goes into data to retrieve userID */
      this.temp_object = this.temp_object.getJsonObject(ID);
         /** goes into ID to retrieve stats */
      this.temp_object = this.temp_object.getJsonObject("statistics");
         /** goes into stats to get the general data under the label "all" */
      this.temp_object = this.temp_object.getJsonObject("all");
      
      return this.temp_object.toString();
   
   }
   
   void retrieve_user_ID()
   {
   
      do{
      
         this.username = get_username("Enter your username.");
      
      }while(ensure_we_have_proper_username(this.username) != true);
      
      this.user_object = JsonObject.fromObject(this.response_from_URL);//now that we know the proper username, we set user_object
      
      this.user_account_ID = retrieve_account_id(this.user_object);
      print("Here is your userID - " + this.user_account_ID + "\n\n");
   
   }
   
   void retrieve_enemy_user_ID()
   {
   
      do{
      
         this.enemy_username = get_username("Enter enemy username.");
      
      }while(ensure_we_have_proper_username(this.enemy_username) != true);
      
      this.enemy_user_object = JsonObject.fromObject(this.response_from_URL);//now that we know the proper username, we set user_object
      
      this.enemy_user_account_ID = retrieve_account_id(this.enemy_user_object);
      print("Here is the enemy's userID - " + this.enemy_user_account_ID + "\n\n");
   
   }
   
   
   void print(String message)
   {//this method will be a godsend when it comes time to upgrade to V2
    //at least, slightly so
   
      System.out.println(message);
   
   }
   
   boolean set_up()
   {
   
      boolean success = true;
      
         
      try
      {
      
         this.scan = new Scanner(System.in);
      
         return success;
      
      }
      catch(Exception e)
      {
      
         return !success;
      
      }
   
   }
   
   String get_username(String prompt)
   {
   
      print(prompt);//prompts user to enter a username
      return this.scan.next();//scans user input
   
   }
   
   boolean ensure_we_have_proper_username(String username)
   {
   
      boolean result = true;//did the method succeed?
   
      String temp;
      String status;
      JsonObject transfer = new JsonObject();
      int count;//the number of names that the search returned
   
   
         
      result = read_URL(
         "http://api.worldoftanks.com/wot/account/list/?application_id=" + this.APP_ID
         + "&search=" + username
         + "&limit=" + this.LIMIT
         );//reads url to get the JSON data
      
      this.temp_object = JsonObject.fromObject(this.response_from_URL);//build a JsonObject
      
      status = this.temp_object.getString("status");//get status of search
         
      if(status.equals("ok"))//if the search was properly executed
      {
            
         transfer = this.temp_object.getJsonObject("meta");
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
   
   String retrieve_account_id(JsonObject object)
   {
          
      String result = "";
                  
      /** json array is made from data */
      this.temp_array = object.getJsonArray("data");
      /** 1st element from array of JsonObjects is stored into a JsonObject */
      this.temp_object = this.temp_array.getJsonObject(0);
      
      result = this.temp_object.getString("account_id");
   
      return result;
   
   }
   
   /** will store JSON data into this.response_from_URL
    *  @return boolean to tell whether method succeeded or failed
    *
    */
   boolean read_URL(String webservice)
   {
      
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
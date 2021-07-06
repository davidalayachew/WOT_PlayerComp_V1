import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.Json;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/** An API Client that allows you to fetch data from the WOT API endpoints. */
public class ApiClient
{

   /** Contains the Application ID Token that will permit us to be able to hit WOT API endpoints. */
   private static final String APP_ID = fetchAppId();
   
   /**
    * 
    * Fetches the Application ID upon initialization.
    * 
    * @return  the Application ID
    * 
    */
   private static String fetchAppId()
   {
   
      try
      {
      
         File f = new File(ApiClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                
         while (!f.getName().contains("WOT_PlayerComp"))
         {
         
            f = f.getParentFile();
         
         }
         
         String sep = java.nio.file.FileSystems.getDefault().getSeparator();
      
         f = new File(f.getAbsolutePath() + sep + "src" + sep + "main" + sep + "res" + sep + "DO_NOT_COMMIT.txt");
      
         System.out.println("JAR Path :" + f);
      
         List<String> temp = Files.readAllLines(f.toPath());
         
         return temp.get(0);
      
      }
      
      catch (IOException ioe)
      {
      
         throw new IllegalArgumentException("Could not fetch APP ID! ", ioe);
      
      }
      
      catch (URISyntaxException e)
      {
            
         throw new IllegalStateException("Bad URI! ", e);
            
      }
            
   }

   /** Unused. */
   private ApiClient() { /** Unused. */ }

   /**
    * 
    * Fetches statistics on the given Player - specifically, fetches the ALL statistics, as defined by the WOT API.
    * 
    * @param   player      Player that we are fetching statistics for
    * @return              the statistics of the Player
    * 
    */
   public static StatisticsAll fetchStatistics(Player player)
   {
   
      String accountId = player.fetch(Player.Attribute.ACCOUNT_ID).toString();
   
      final String endpoint =
         "https://api.worldoftanks.com/wot/account/info/?application_id=" + APP_ID + "&account_id=" + accountId;
   
      System.out.println(endpoint);
   
      String response = performGET(endpoint);
      
      System.out.println(response);
      
      JsonObject jsonObject = createJsonObjectFromResponse(response);
      
      JsonObject playerObject = jsonObject.getJsonObject("data").getJsonObject(accountId);
      
      String statisticsAllJson = playerObject.getJsonObject("statistics").getJsonObject("all").toString();
   
      return new StatisticsAll(statisticsAllJson);
   
   }

   /**
    * 
    * Endpoint that accepts a username, then fetches the relevant Player objects that match the search query.
    * This endpoint usually serves as a starting point because you usually need an account_id to hit most other endpoints.
    * 
    * @param   searchQuery    username of the player we are searching for
    * @return                 list of players that match the given searchQuery 
    * 
    */
   public static List<Player> fetchPlayers(final String searchQuery)
   {
   
      final String endpoint =
         "https://api.worldoftanks.com/wot/account/list/?application_id=" + APP_ID + "&search=" + searchQuery;
   
      String response = performGET(endpoint);
      
      JsonObject jsonObject = createJsonObjectFromResponse(response);
         
      List<Player> players = new ArrayList<>();
         
      for (JsonValue each : jsonObject.getJsonArray("data"))
      {
            
         players.add(new Player(each.toString()));
            
      }
         
      return players;
         
   }
   
   /**
    * 
    * Method that allows us to create a JsonObject from the response a WOT API endpoint gives us.
    * Most WOT API endpoints have the same basic response format.
    * 
    * <ul>
    *   <li>status - tells you if the request worked</li>
    *   <li>meta - tells you how many results you have</li>
    *   <li>data - wrapper object/array that holds the actual results from the endpoint</li>
    * </ul>
    * 
    * As a result, we can save lines of code by just dealing with this common use case in this method.
    * 
    * @param   json     holds the json that we will convert into a JsonObject
    * @return           
    * 
    */
   private static JsonObject createJsonObjectFromResponse(final String json)
   {
   
      try (JsonReader reader = Json.createReader(new StringReader(json)))
      {
         
         JsonObject responseObject = reader.readObject();
         
         if ("ok".equals(responseObject.getString("status")))
         {
         
            return responseObject;
         
         }
         
         else
         {
         
            throw new IllegalStateException("Did not receive an OK status! responseObject = " + responseObject.toString());
         
         }
      
      }
      
   
   }
   
   /**
    * 
    * Performs a REST GET on the given endpoint.
    * 
    * @param   endpoint    the endpoint to perform a REST GET upon
    * @return              the response (potentially) given by the endpoint
    * 
    */
   private static String performGET(final String endpoint)
   {
   
      URL url = createUrl(endpoint);
   
      try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())))
      {
      
         String response = "";
      
         String line = in.readLine();
      
         while (line != null)
         {
         
            response += line;
            line = in.readLine();
         
         }
         
         return response;
         
      }
      
      catch (Exception e)
      {
      
         throw new IllegalStateException("Failed to perform a GET! ", e);
      
      }
      
   }
   
   /**
    * 
    * Constructs a URL Object from a String endpoint.
    * 
    * @param   endpoint    endpoint to be converted into a URL Object
    * @return              URL Object that has just been created
    * 
    */
   private static URL createUrl(final String endpoint)
   {
   
      try
      {
      
         return new URL(endpoint);
      
      }
      
      catch (IOException ioe)
      {
      
         throw new IllegalArgumentException("Bad endpoint! endpoint = " + endpoint, ioe);
      
      }   
      
   }
   
}

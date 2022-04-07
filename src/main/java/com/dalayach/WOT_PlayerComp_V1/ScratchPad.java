import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.Json;

/** A Scratch Pad to test out ideas. */
public class ScratchPad
{
   /** Constructor. */
   public ScratchPad()
   {
   
      //testObjectBuilder();
      
   
   }

   private void testObjectBuilder()
   {
   
      JsonValue jsonValue = Json.createObjectBuilder()
         .add("firstName", "John")
         .build();
         
      System.out.println(jsonValue.toString());
         
      JsonObject jsonObject = Json.createObjectBuilder()
         .add("firstName", "John")
         .build();
         
      System.out.println(jsonObject.toString());
         
   }

}

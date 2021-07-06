import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * 
 * Class to hold the Generic Data Object as JSON that endpoints will return to us.
 * 
 * @param   <K>      ReturnType that represents the return type of the attribute
 * 
 */
public class GenericDataObject<K extends ReturnType>
{

   /** JsonObject that allows us to easily fetch data from the JSON returned to us by endpoints. */
   private final JsonObject jsonObject;
   
   /**
    * 
    * Constructor that constructs the object based off of the given JSON.
    * 
    * @param   json     the json String that will be used to construct the JsonObject
    * 
    */
   public GenericDataObject(String json)
   {
   
      JsonReader jsonReader = Json.createReader(new StringReader(json));
      this.jsonObject = jsonReader.readObject();
      
   }
   
   /**
    * 
    * Getter for the JsonObject.
    * 
    * @return  the JsonObject
    * 
    */
   public JsonObject getJsonObject()
   {
   
      return this.jsonObject;
   
   }

   /**
    * 
    * Fetches an attribute based on the given return type.
    * 
    * @param   <T>            the return type
    * @param   returnType     the attribute we are trying to fetch
    * @return                 the value
    * 
    */
   public <T> T fetch(K returnType)
   {
   
      Class<T> type = returnType.fetchJsonValueType().fetchClass();
   
      return fetch(getJsonObject(), returnType);
   
   }
   
   /**
    * 
    * Fetches an attribute from the jsonObj based on the given return type.
    * 
    * @param   jsonObj        the jsonObj that we are fetching the attribute from
    * @param   returnType     the attribute we are trying to fetch
    * @return                 the value
    * 
    */
   private <T> T fetch(JsonObject jsonObj, K returnType)
   {
   
      validate(jsonObj, returnType);
   
      Class<T> thisIsNecessary = returnType.fetchJsonValueType().fetchClass();
      String attribute = returnType.name().toLowerCase();
   
      switch (returnType.fetchJsonValueType())
      {
      
         case OBJECT:
            return thisIsNecessary.cast(jsonObj.getJsonObject(attribute));
      
         case ARRAY:
            return thisIsNecessary.cast(jsonObj.getJsonArray(attribute));
      
         case STRING:
            return thisIsNecessary.cast(jsonObj.getString(attribute));
      
         case NUMBER:
            return thisIsNecessary.cast(jsonObj.getJsonNumber(attribute).bigDecimalValue());
      
         case BOOLEAN:
            return thisIsNecessary.cast(jsonObj.getBoolean(attribute));
      
         default:
            throw new IllegalArgumentException("Invalid ReturnType! -- " + returnType.fetchJsonValueType());
      
      }
   
   }
   
   /**
    * 
    * Validates the jsonObject and returnType.
    * 
    * @param   jsonObject     the jsonObject that we are validating
    * @param   returnType     the attribute that we are validating
    * 
    */
   private static void validate(JsonObject jsonObject, ReturnType returnType)
   {
   
      if (jsonObject == null)
      {
      
         throw new IllegalArgumentException("Invalid JsonObject! -- " + jsonObject);
      
      }
      
      if (returnType == null || returnType.name() == null || returnType.fetchJsonValueType() == null)
      {
      
         throw new IllegalArgumentException("Invalid returnType! -- " + returnType);
      
      }
      
   }

   @Override
   public String toString()
   {
   
      return this.jsonObject.toString();
   
   }

}

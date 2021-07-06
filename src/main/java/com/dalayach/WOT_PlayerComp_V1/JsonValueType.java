import javax.json.JsonArray;
import javax.json.JsonObject;
import java.math.BigDecimal;

/** Enum that represents the different value types that JSON can have. */
public enum JsonValueType
{

   OBJECT(JsonObject.class),
   ARRAY(JsonArray.class),
   STRING(String.class),
   NUMBER(BigDecimal.class),
   BOOLEAN(Boolean.class),
   ;

   /** Class that represents the Java equivalent class of this enum value. */
   @SuppressWarnings("rawtypes")
   private final Class clazz;
   
   /** Constructor that maps the Java type to the enum type. */   
   <T> JsonValueType(Class<T> clazz)
   {
      
      this.clazz = clazz;
      
   }
   
   /**
    * 
    * Getter for the Java type class.
    * 
    * @param   <T>      the Java type
    * @return           the Java type class
    *  
    */
   @SuppressWarnings("unchecked")
   public <T> Class<T> fetchClass()
   {
   
      return this.clazz;
      
   }
      
}

/** Generic Data Object that holds the basic Player info, such as the Account ID and the nickname. */
public class Player extends GenericDataObject<Player.Attribute>
{

   /** Enum that holds the possible keys that are contained within this object. */
   public enum Attribute implements ReturnType
   {
   
      ACCOUNT_ID(JsonValueType.NUMBER),
      NICKNAME(JsonValueType.STRING),
      ;
      
      /** The JsonValueType that we expect this attribute to return. */
      private final JsonValueType returnType;
      
      /** Constructor. */
      Attribute(JsonValueType returnType)
      {
      
         this.returnType = returnType;
      
      }
      
      /** {@inheritDoc} */
      public JsonValueType fetchJsonValueType()
      {
      
         return this.returnType;
      
      }
      
   }
   
   /**
    * 
    * Constructor.
    * 
    * @param   json     the json that the parent object will construct a JsonObject with
    * 
    */
   public Player(String json) { super(json); }
   
}

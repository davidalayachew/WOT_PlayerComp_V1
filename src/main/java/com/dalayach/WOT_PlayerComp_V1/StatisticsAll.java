/** Generic Data Object that holds the detailed Player stats, such as XP. */
public class StatisticsAll extends GenericDataObject<StatisticsAll.Attribute>
{

   /** Enum that holds the possible keys that are contained within this object. */
   public enum Attribute implements ReturnType
   {
   
      BATTLE_AVG_XP(JsonValueType.NUMBER),
      HITS_PERCENTS(JsonValueType.NUMBER),
      XP(JsonValueType.NUMBER),
      DAMAGE_DEALT(JsonValueType.NUMBER),
      DAMAGE_RECEIVED(JsonValueType.NUMBER),
      WINS(JsonValueType.NUMBER),
      LOSSES(JsonValueType.NUMBER),
      DRAWS(JsonValueType.NUMBER),
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
    * @param   json     json that the parent object will use to construct a JsonObject
    * 
    */
   public StatisticsAll(String json) { super(json); }
         
}

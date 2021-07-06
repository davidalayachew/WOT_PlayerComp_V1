/** Interface that allows other classes to function as a return type for GenericDataObject attribute. */
public interface ReturnType
{

   /**
    * 
    * Fetches the JsonValueType for this enum value.
    * 
    * @return  the JsonValueType
    * 
    */
   JsonValueType fetchJsonValueType();
   
   /**
    * 
    * Fetches the name for this enum value.
    * 
    * @return  the name
    * 
    */
   String name();

}

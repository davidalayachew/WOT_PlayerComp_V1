import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class PlayerTest
{

   @Test
   public void defaultTest()
   {
     
      Player player = new Player("{ \"nickname\": \"davidaplays\", \"account_id\": 1006390305}");
      
      ApiClient.fetchStatistics(player);
      
      Assert.assertEquals("davidaplays", player.fetch(Player.Attribute.NICKNAME));
      Assert.assertEquals(BigDecimal.valueOf(1006390305), player.fetch(Player.Attribute.ACCOUNT_ID));
      
   }
   
}

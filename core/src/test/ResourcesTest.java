import aurumvorax.arcturus.Resources;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


@Ignore
@RunWith(GdxTestRunner.class)
public class ResourcesTest{
    @Test
    public void AssetLoading() throws Exception{

        Resources.queueTextureAssets();

        while(!Resources.loadAssets()){
        }
        Assert.assertNotNull(Resources.createSprite(Resources.SHIP_IMG_PATH + "TestShip"));

    }
}

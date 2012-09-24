import com.github.andoidMarkerParser.AndroidApplication;
import com.github.andoidMarkerParser.AndroidMarketHandler;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

/**
 * @author blackbass <o.salionov@zmeke.com>
 */
public class MarketRequestTest {

    @Test
    public void getRequest(){
        String request = "pname:com.fullfat.android.agentdash";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);
        assertNotNull(returnData);
        assertEquals(returnData.size(), 1);
    }

    @Test
    public void getSize(){
        String request = "pname:com.fullfat.android.agentdash";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        AndroidApplication app = returnData.get(0);
        assertNotNull(app);

        assertTrue(app.getFileBytes().equals(25165824L));

        request = "pname:com.kamagames.notepad";
        returnData = AndroidMarketHandler.marketSearch(request);
        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        app = returnData.get(0);
        assertNotNull(app);

        assertTrue(app.getFileBytes().equals(51200l));


        request = "pname:mobi.firedepartment";
        returnData = AndroidMarketHandler.marketSearch(request);
        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        app = returnData.get(0);
        assertNotNull(app);
        assertTrue(app.getFileBytes().equals(2831155l));

    }


    @Test
    public void badRequest(){
        String request = "pname:net.cachapa.libra31231231231231231";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertTrue(returnData.isEmpty());

    }

    @Test
    public void requestWithoutSize(){
        String request = "pname:net.cachapa.libra";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        AndroidApplication app = returnData.get(0);
        assertNotNull(app);

        assertTrue(app.getFileBytes().equals(0l));

    }
}

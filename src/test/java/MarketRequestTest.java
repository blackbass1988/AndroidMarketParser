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
    public void longNameInRequestTest() {
        String request = "UFO hotseat";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);
        assertNotNull(returnData);
        assertEquals(returnData.size(), 1);
    }

    @Test
    public void getRequest() {
        String request = "pname:com.fullfat.android.agentdash";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);
        assertNotNull(returnData);
        assertEquals(returnData.size(), 1);
    }

    @Test
    public void getSize() {
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
    public void badRequest() {
        String request = "pname:net.cachapa.libra31231231231231231";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertTrue(returnData.isEmpty());

    }

    @Test
    public void requestWithoutSize() {
        String request = "pname:net.cachapa.libra";
        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        AndroidApplication app = returnData.get(0);
        assertNotNull(app);

        assertTrue(app.getFileBytes() >= 0);
    }

    @Test
    public void parserTest() {
        String request = "pname:ru.yulagroup.book";

        List<AndroidApplication> returnData = AndroidMarketHandler.marketSearch(request);

        assertNotNull(returnData);
        assertFalse(returnData.isEmpty());
        assertEquals(returnData.size(), 1);

        AndroidApplication app = returnData.get(0);
        assertNotNull(app);

        assertEquals(app.getPackageName(), "ru.yulagroup.book");
        assertNotNull(app.getFileBytes());
        assertNotNull(app.getCategory());
        assertNotNull(app.getCurrency());
        assertNotNull(app.getDescription());
        assertNotNull(app.getDetailsUrl());
        assertNotNull(app.getImage());
        assertNotNull(app.getMinAndroidVersion());
        assertEquals(app.getMinAndroidVersion(), "2.2");

    }
}

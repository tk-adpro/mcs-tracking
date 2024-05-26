package id.ac.ui.cs.advprog.eshop.mcstracking.strategy;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShippingStrategyTest {

    private ShippingStrategyFactory factory;

    private String jte = "JTE";

    private String gobek = "Go-bek";

    private String siwuzz = "SiWuzz";

    private String dummy = "DUMMY";

    @BeforeEach
    void setUp(){
        factory = new ShippingStrategyFactory();
    }

    @Test
    void testShippingFactory(){
        assertNotNull(factory.getStrategy(jte));
        assertNotNull(factory.getStrategy(gobek));
        assertNotNull(factory.getStrategy(siwuzz));
        assertNull(factory.getStrategy(null));
        assertNull(factory.getStrategy(dummy));
    }

    @Test
    void testJteStrategy(){
        ShippingStrategy strategy = factory.getStrategy(jte);
        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(null));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(dummy));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("JTE-A12345678912"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("DSA-012345678912"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("JTE-0123456789123"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("JTE-3456789123"));

        assertDoesNotThrow(() ->
                strategy.validateTrackingCode("JTE-012345678912"));
    }

    @Test
    void testGobekStrategy(){
        ShippingStrategy strategy = factory.getStrategy(gobek);
        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(null));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(dummy));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("GBK-A123456789121"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("DSA-012345678912"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("GBK-9123001384"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("GBK-ab9123ED1384"));

        assertDoesNotThrow(() ->
                strategy.validateTrackingCode("GBK-AB9123ED1384"));

        assertDoesNotThrow(() ->
                strategy.validateTrackingCode("GBK-009123001384"));

        assertDoesNotThrow(() ->
                strategy.validateTrackingCode("GBK-ABKSAHDKAHDS"));
    }

    @Test
    void testSiWuzzStrategy(){
        ShippingStrategy strategy = factory.getStrategy(siwuzz);
        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(null));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode(dummy));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("SWZ-QHWJAMSTYAZJSFA"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("DSA-QHWJAMSTYAZJ"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("SWZ-ASJDHAIUD"));

        assertThrows(BadRequestException.class, () ->
                strategy.validateTrackingCode("SWZ-AB9123ED1384"));

        assertDoesNotThrow(() ->
                strategy.validateTrackingCode("SWZ-QHWJAMSTYAZJ"));
    }
}

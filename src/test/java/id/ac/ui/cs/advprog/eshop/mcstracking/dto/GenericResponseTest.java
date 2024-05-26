package id.ac.ui.cs.advprog.eshop.mcstracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericResponseTest {

    private GenericResponse genericResponse;

    private final Object dataDummy = new Object();

    @BeforeEach
    void setUp() {
        String dummy = "DUMMY";
        this.genericResponse = new GenericResponse();
        this.genericResponse.setStatus(dummy);
        this.genericResponse.setMessage(dummy);
        this.genericResponse.setData(dataDummy);
    }

    @Test
    void testCreate() {
        GenericResponse newGenericResponse = new GenericResponse();
        newGenericResponse.setStatus(genericResponse.getStatus());
        newGenericResponse.setMessage(genericResponse.getMessage());
        newGenericResponse.setData(genericResponse.getData());

        assertEquals(genericResponse.getStatus(), newGenericResponse.getStatus());
        assertEquals(genericResponse.getMessage(), newGenericResponse.getMessage());
        assertEquals(genericResponse.getData(), newGenericResponse.getData());
    }
}

package com.example.interface_testing;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class ITest {

    @Test
    void testInterfaceMethodWithoutImplementation() {

        Interface mockInterface = mock(Interface.class);

        when(mockInterface.getData()).thenReturn("Mocked Data");

        String result = mockInterface.getData();

        assertEquals("Mocked Data", result);
    }

    @Test
    void testVoidMethod() {

        Interface mockInterface = mock(Interface.class);

        mockInterface.process();

        verify(mockInterface).process();
    }

    @Test
    void testMethodCallCount() {

        Interface mockInterface = mock(Interface.class);

        mockInterface.execute();
        mockInterface.execute();
        mockInterface.execute();

        verify(mockInterface, times(3)).execute();
    }
}
package uk.co.billy.hive.doorlockservice.webbapp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.ServiceEvent;

@RunWith(MockitoJUnitRunner.class)
public class ActivatorTest {
	@Mock
	private ServiceEvent event;
	
	@InjectMocks
	private Activator activator;

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

	@Test
	public void testStop() {
		fail("Not yet implemented");
	}

	@Test
	public void testServiceChanged() {
		Mockito.when(event.getType()).thenReturn(ServiceEvent.REGISTERED);
		
		activator.serviceChanged(event);
		Mockito.verify(event, Mockito.atLeastOnce())
		.getType();
	}

}

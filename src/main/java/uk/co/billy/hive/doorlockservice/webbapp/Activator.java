package uk.co.billy.hive.doorlockservice.webbapp;

import org.apache.log4j.Logger;
import org.osgi.framework.*;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import uk.co.billy.hive.doorlockservice.api.DoorLockServiceAPI;

import javax.servlet.ServletException;

/**
 * This sevice needs two other services:
 * 1. http.service : generic osgi service
 * 2. doorlock-service : service provided by the Hive
 *
 * There is a machanism in place that servlet is only registered if both services are available. If one of them
 * disappears, the servlet will go down.
 */
public class Activator implements BundleActivator, ServiceListener {
	
    private BundleContext bundleContext;
    private Logger logger = Logger.getLogger(Activator.class);
    
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;

        doRegister();

        synchronized (this) {
            bundleContext.addServiceListener(this,
                    "(|(objectClass=" + DoorLockServiceAPI.class.getName() + ")" +
                    "(objectClass=" + HttpService.class.getName() + "))");
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        doUnregister();
    }

    public void serviceChanged(ServiceEvent event) {
        String objectClass = ((String[]) event.getServiceReference().getProperty("objectClass"))[0];
        
        if (event.getType() == ServiceEvent.REGISTERED) {
            doRegister();
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            doUnregister();
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            doUnregister();
            doRegister();
        }
    }

    private void register() throws InvalidSyntaxException, ServletException, NamespaceException {
       
    	 ServiceReference[] serviceReferences = bundleContext.getServiceReferences(DoorLockServiceAPI.class.getName(), null);
         DoorLockServiceAPI doorLockService = null;
         if (serviceReferences != null) {
             doorLockService = (DoorLockServiceAPI) bundleContext.getService(serviceReferences[0]);
         } else {
             logger.info("No doorlock service available");
         }
         
        ServiceReference[] httpReferences = bundleContext.getServiceReferences(HttpService.class.getName(), null);
        HttpService httpService = null;
        if (httpReferences != null) {
            httpService = (HttpService) bundleContext.getService(httpReferences[0]);
        } else {
            logger.info("No http service available");
        }
        
        if ((doorLockService != null) && (httpService != null)) {
            logger.info("training servlet will be registered.");
            httpService.registerServlet("/doorlock", new DoorLockServlet(doorLockService), null, null);
            httpService.registerResources("/","/htmls",null);
        } else {
            logger.info("No servlet to register, problem with training service or http service");
        }
    }

    private void unregister() throws InvalidSyntaxException {
        
        ServiceReference[] httpReferences = bundleContext.getServiceReferences(HttpService.class.getName(), null);
        if (httpReferences != null) {
            HttpService httpService = (HttpService) bundleContext.getService(httpReferences[0]);
            httpService.unregister("/doorlock");
            httpService.unregister("/");
        }
    }

    private void doUnregister() {
        try {
            unregister();
        } catch (InvalidSyntaxException e) {
            
        }
    }

    private void doRegister() {
        try {
            register();
        } catch (InvalidSyntaxException e) {
            
        } catch (ServletException e) {
            
        } catch (NamespaceException e) {
            
        }
    }
}

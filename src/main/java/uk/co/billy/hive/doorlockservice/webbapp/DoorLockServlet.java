package uk.co.billy.hive.doorlockservice.webbapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.co.billy.hive.doorlockservice.api.DoorLockServiceAPI;

public class DoorLockServlet extends HttpServlet{
	private static final long serialVersionUID = -184231950071835407L;
	private DoorLockServiceAPI service;
	private Logger logger = Logger.getLogger(DoorLockServlet.class);
	
	public DoorLockServlet(DoorLockServiceAPI service) {	
		 this.service = service;
	}
	
	public void setTrainingService(DoorLockServiceAPI service) {
        this.service = service;
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        logger.info("doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)");
        String doorstate = service.getDoorState();			//using my doorLock service here!!!
        httpServletResponse.setContentType("text/html");
        ServletOutputStream out = httpServletResponse.getOutputStream();
        out.println("<html><body>");
        
            out.println(doorstate);
            out.println("<br/>");
       
        out.println("</body></html>");
        out.close();
    }

}

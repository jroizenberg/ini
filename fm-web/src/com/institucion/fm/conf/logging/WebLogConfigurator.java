package com.institucion.fm.conf.logging;
import org.apache.log4j.PropertyConfigurator;

public class WebLogConfigurator extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    static final long serialVersionUID = 1L;

    public void init() {
    	
        String prefix = getServletContext().getRealPath("/");
        String logDir = org.jboss.system.server.ServerConfigLocator.locate().getServerLogDir().toString();
        System.setProperty("rootPath",logDir);
        String file = getInitParameter("log4jFile");
        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
        //log.debug("prefix + file="+prefix + file);
    }
}

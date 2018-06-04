/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apirest;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 *
 * @author Guilherme Garcia
 */
public class Launcher {
    
    private static final String JERSEY_SERVLET_NAME = "jersey-container-servlet";
    
    public static void main(String[] args) throws Exception {
        new Launcher().start();
    }

    void start() throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8080);
         
        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
         
        Context context = tomcat.addContext(contextPath, docBase);
                  
        tomcat.addServlet(contextPath, JERSEY_SERVLET_NAME, new ServletContainer(new Main()));      
        context.addServletMappingDecoded("/api/*", JERSEY_SERVLET_NAME);
         
        tomcat.start();
        tomcat.getServer().await();
    }
}

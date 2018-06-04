/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apirest;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Guilherme
 */
public class Main extends ResourceConfig{
    
    //Dizendo para o Jersey que as classes Controller estão no pacote abaixo.
    public Main(){
        packages("com.apirest.controllers");
    }    
}

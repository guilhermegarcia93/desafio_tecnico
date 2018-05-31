/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apirest.controllers;

import com.apirest.models.Product;
import com.apirest.util.Functions;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;



/**
 *
 * @author Guilherme
 */
@Path("products")
public class ProductController {
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String test(String json_param, 
            @QueryParam("filter") String filter, 
            @QueryParam("order_by") String order_by) throws IOException{
        
        //Guilherme - 31/05/2018
        //Recebendo a String JSON com os produtos à serem agrupados e ordenados, e formatando em uma Lista de Produtos(Entity).
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        TypeReference<List<Product>> mapType = new TypeReference<List<Product>>() {};
    	List<Product> productList = objectMapper.readValue(json_param, mapType);
        
        
        Functions functions = new Functions();
        //Função que recebe a Lista de Produtos e os Filtros, e aplica o filtro na lista
        Stream<Product> filteredList = functions.filtering(productList, filter);
        //Função que recebe a Lista de Produtos, agrupa e ordena, e retorna no formado desejado.
        String[] _order_by = new String[0];
        if(order_by != null){
            try{
                _order_by = order_by.split(",");
            }catch(Exception e){
                return "{\"code\": \"001\","
                        + "\"message\": \"Parametro(s) de ordenação incorreto(s), verifique!\"}";
            }
        }
        List<Object> _groupAndOrderedList = functions.groupingAndOrdering(filteredList, _order_by);
        
        //Formatar o retorno conforme o desafio.
        Map<String, List<Object>> _data = new HashMap<>();
        _data.put("data", _groupAndOrderedList);
        
    	return objectMapper.writeValueAsString(_data);
        
    }
    
}

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
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;



/**
 *
 * @author Guilherme
 */
@Path("products")
public class ProductController {
    
    Functions functions = new Functions();
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJSONOrganizing(String json_param, 
            @Context UriInfo uriInfo,
            @QueryParam("filter") String filter, 
            @QueryParam("order_by") String order_by,
            @QueryParam("group_by") String group_by) throws IOException{
        /**
         * VALIDANDO OS PARÂMETROS DE ENTRADA @QUERYPARAM.
         */
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        if(queryParams.size() > 0){
            String queryParam = queryParams.keySet().stream().findFirst().get();

            List valid_params = Arrays.asList("FILTER","ORDER_BY","GROUP_BY");
            if (!valid_params.contains(queryParam.toUpperCase())) {
                return functions.formatReturnJSON("001", "Parametro(s) inválido(s).", valid_params, queryParam);
            }
        }
                
        /**
         * VALIDANDO PARAMETRO ORDER_BY @QUERYPARAM.
         */
        if(order_by != null){
            String ordenationKey = Arrays.stream(order_by.split(",")).map(String::trim)
                                    .flatMap(s -> Arrays.stream(s.split(":")).limit(1)).findFirst().get();
            String ordenationValues = Arrays.stream(order_by.split(",")).map(String::trim)
                                    .flatMap(s -> Arrays.stream(s.split(":")).skip(1)).findFirst().get();
            
            List valid_ordenationKeys = Arrays.asList("ID","EAN","TITLE","BRAND","PRICE","STOCK");
            if(!valid_ordenationKeys.contains(ordenationKey.toUpperCase())){
                return functions.formatReturnJSON("002", "Parametro(s) de ordenação inválidos.", valid_ordenationKeys, ordenationKey);
            }
            List valid_ordenationValues = Arrays.asList("ASC","DESC");
            if(!valid_ordenationValues.contains(ordenationValues.toUpperCase())){
                return functions.formatReturnJSON("003", "Parametro(s) de ordenação inválidos.", valid_ordenationValues, ordenationValues);
            }
            
        }else{
            /**
             * valor default caso o user não passar nada.
             */
            order_by = "stock:desc,price:asc";
        }
        
        /**
         * VALIDANDO PARAMETRO FILTER @QUERYPARAM.
         */
        if(filter != null){
            String filterKey = Arrays.stream(filter.split(",")).map(String::trim)
                                    .flatMap(s -> Arrays.stream(s.split(":")).limit(1)).findFirst().get();
            
            List valid_filtersKeys = Arrays.asList("ID","EAN","TITLE","BRAND","PRICE","STOCK");
            if(!valid_filtersKeys.contains(filterKey.toUpperCase())){
                return functions.formatReturnJSON("004", "Parametro(s) de ordenação inválidos.", valid_filtersKeys, filterKey);
            }            
        }
        
        /**
         * VALIDANDO PARAMETRO GROUP_BY @QUERYPARAM.
         */
        if(group_by != null){
            List valid_groups = Arrays.asList("EAN","TITLE","BRAND");
            if(!valid_groups.contains(group_by.toUpperCase())){
                return functions.formatReturnJSON("005", "Parametro de agrupamento inválidos.", Arrays.asList("ean", "title", "brand"), group_by);
            }
        }else{
            /**
             * valor default caso o user não passar nada.
             */
            group_by = "title";
        }
        
        /**
        * Recebendo a String JSON com os produtos à serem agrupados e ordenados, 
        * e formatando em uma Lista de Produtos(Entity).
        */
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        TypeReference<List<Product>> mapType = new TypeReference<List<Product>>() {};
        List<Product> productList = null;
        try{
            productList = objectMapper.readValue(json_param, mapType);
        }catch(Exception e){
            return functions.formatReturnJSON("006", "Falha na estrutura do JSON. Favor verificar!", null, null);
        }
        
        /**
        * Funções que recebem a Lista de Produtos, filtros, grupos e ordenações
        * e aplicar na lista.
        */
        Stream<Product> filteredList = functions.filtering(productList, filter);
        List<Object> _groupAndOrderedList = functions.groupingAndOrdering(filteredList, order_by, group_by);
        
        /**
        * Formatar o retorno conforme o desafio.
        */
        Map<String, List<Object>> _data = new HashMap<>();
        _data.put("data", _groupAndOrderedList);
        
    	return objectMapper.writeValueAsString(_data);
        
    }
    
}

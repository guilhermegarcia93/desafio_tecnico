/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apirest.util;

import com.apirest.models.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.simple.JSONObject;

/**
 *
 * @author Guilherme Garcia
 */
public class Functions {
    
    public Functions(){
        
    }
    
    public String formatReturnJSON(String code, String message, Object expected_values, Object value){
        JSONObject retorno = new JSONObject();
        retorno.put("code", code);
        retorno.put("message", message);
        if(expected_values != null){
            retorno.put("expected_values", expected_values);
        }
        if(value != null){
            retorno.put("value", value);
        }
        
        return retorno.toJSONString();
    }
    
    public List<Object> groupingAndOrdering(Stream<Product> productList, String order_by, String group_by){
        List<Object> retorno = new ArrayList<>();
        
        Map<String, List<Product>> listGrouped = null;
        //agrupa de acordo com o que o user passou de parametro
        switch (group_by) {
            case "ean":
                listGrouped = productList.collect(Collectors.groupingBy(p -> p.getEan()));
                break;
            case "brand":
                listGrouped = productList.collect(Collectors.groupingBy(p -> p.getBrand()));
                break;
            default:
                listGrouped = productList.collect(Collectors.groupingBy(p -> p.getTitle().substring(0, p.getTitle().indexOf(' '))));
                break;
        }
        
        Map<String, Object> data = new HashMap<>();
        for(List<Product> itens : listGrouped.values()){
            /**
             * metodo para odernar a lista
             */
            this.ordering(itens, order_by);

            data = new HashMap<>();
            switch (group_by) {
                case "ean":
                    data.put("description", itens.get(0).getEan());
                    break;
                case "brand":
                    data.put("description", itens.get(0).getBrand());
                    break;
                default:
                    data.put("description", itens.get(0).getTitle());
                    break;
            }
            data.put("items", itens);
            retorno.add(data);
        }

        return retorno;
    }
        
    public Stream<Product> filtering(List<Product> productList, String filter){
        Stream<Product> filteredList = productList.stream();
                
        if(filter != null){
            for(String _filter : filter.split(",")){
                String[] value = _filter.split(":");
                        
                switch(value[0]){
                    case "id":
                        filteredList = productList.stream().filter(p -> p.getId().equals(value[1]));
                        break;
                    case "ean":
                        filteredList = productList.stream().filter(p -> p.getEan().equals(value[1]));
                        break;
                    case "title":
                        filteredList = productList.stream().filter(p -> p.getTitle().toUpperCase().contains(value[1].toUpperCase()));
                        break;
                    case "brand":
                        filteredList = productList.stream().filter(p -> p.getBrand().equals(value[1]));
                        break;
                    case "price":
                        filteredList = productList.stream().filter(p -> p.getPrice() == Double.parseDouble(value[1]));
                        break;
                    case "stock":
                        filteredList = productList.stream().filter(p -> p.getStock() == Integer.parseInt(value[1]));
                        break;
                }
            }
        }
        
        return filteredList;
    }
    
    public void ordering(List<Product> productList, String order_by){
        
        Collections.sort(productList, (Product o1, Product o2) -> {
            int result = 0;
            
            for(String _order : order_by.split(",")){
                String[] values = _order.split(":");
                
                switch(values[0]){
                    case "id":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getId().compareTo(o2.getId());
                        }else{
                            result = o2.getId().compareTo(o1.getId());
                        }
                        break;
                    case "ean":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getEan().compareTo(o2.getEan());
                        }else{
                            result = o2.getEan().compareTo(o1.getEan());
                        }
                        break;
                    case "title":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getTitle().compareTo(o2.getTitle());
                        }else{
                            result = o2.getTitle().compareTo(o1.getTitle());
                        }
                        break;
                    case "brand":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getBrand().compareTo(o2.getBrand());
                        }else{
                            result = o2.getBrand().compareTo(o1.getBrand());
                        }
                        break;
                    case "price":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getPrice().compareTo(o2.getPrice());
                        }else{
                            result = o2.getPrice().compareTo(o1.getPrice());
                        }
                        break;
                    case "stock":
                        if(values[1].toUpperCase().equals("ASC")){
                            result = o1.getStock() - o2.getStock();
                        }else{
                            result = o2.getStock() - o1.getStock();
                        }
                        break;
                }
                if (result != 0) return result;
            }
            
            return result;
        });
    }
    
}

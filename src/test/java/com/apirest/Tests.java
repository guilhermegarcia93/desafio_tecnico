/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apirest;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 *
 * @author Guilherme Garcia
 */
public class Tests {
    
    JSONArray jsonArray;
    JSONParser parser = new JSONParser();
    
    String uriBase = "http://localhost:8080/ApiRest/rest/products/";
    
    public Tests() throws FileNotFoundException, IOException, ParseException {
        jsonArray = (JSONArray) parser.parse(new FileReader("files/file.json"));
    }
    
    @Test
    public void TesteFilterOnly() throws FileNotFoundException, IOException, ParseException{
        given()
            .contentType("application/json")
            .body(jsonArray)
            .relaxedHTTPSValidation()
                .queryParam("filter", "title:nikana")
            .when()
                .post(uriBase)
            .then()
                .statusCode(200) // O status http retornado foi 200
                .contentType(ContentType.JSON); // O response foi retornado no formato JSON
    }
    
    @Test
    public void TesteFilterOrder() throws FileNotFoundException, IOException, ParseException{
        given()
            .contentType("application/json")
            .body(jsonArray)
            .relaxedHTTPSValidation()
                .queryParam("filter", "title:nikana")
                .queryParam("order_by", "stock:asc")
            .when()
                .post(uriBase)
            .then()
                .statusCode(200) // O status http retornado foi 200
                .contentType(ContentType.JSON); // O response foi retornado no formato JSON
    }
    
    @Test
    public void TesteFilterOrderGroup() throws FileNotFoundException, IOException, ParseException{
        given()
            .contentType("application/json")
            .body(jsonArray)
            .relaxedHTTPSValidation()
                .queryParam("filter", "title:nikana")
                .queryParam("order_by", "stock:asc")
                .queryParam("group_by", "brand")
            .when()
                .post(uriBase)
            .then()
                .statusCode(200) // O status http retornado foi 200
                .contentType(ContentType.JSON); // O response foi retornado no formato JSON
    }
}

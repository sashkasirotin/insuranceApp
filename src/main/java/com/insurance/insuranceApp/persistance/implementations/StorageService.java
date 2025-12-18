package com.insurance.insuranceApp.persistance.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.insuranceApp.services.implementations.ManageProductApplicationService;
import com.insurance.insuranceApp.services.models.ClientInfo;
import com.insurance.insuranceApp.services.models.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private final HashMap<String, ClientInfo> clientsMap ;
    private final HashMap<String, ProductInfo> productsMap ;
    private final HashMap<String, List<ProductInfo>> clientsProductsMap ;

    public HashMap<String, ClientInfo> getClientsMap() {
        return clientsMap;
    }

    public HashMap<String, ProductInfo> getProductsMap() {
        return productsMap;
    }

    public HashMap<String, List<ProductInfo>> getClientsProductsMap() {
        return clientsProductsMap;
    }

    public StorageService() {
        log.info("storage service starting:");
        clientsMap=new HashMap<>();
        productsMap= new HashMap<>();
        clientsProductsMap= new HashMap<>();
        initiateProductMap();

    }
    private void initiateProductMap(){
        log.info("ProductMap created");
        productsMap.put("123",new ProductInfo("123","kids insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusYears(1), 200.0));
        productsMap.put("124",new ProductInfo("124","adult insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusYears(1), 200.0)) ;
        productsMap.put("125",new ProductInfo("125","flight insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusWeeks(1), 200.0));
        productsMap.put("126",new ProductInfo("126","extreme insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusWeeks(1), 200.0));
        productsMap.put("128",new ProductInfo("127","extreme  flight insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusWeeks(1), 200.0));
        productsMap.put("129",new ProductInfo("128","extreme car insurance", "full cover", LocalDateTime.now(), LocalDateTime.now().plusWeeks(1), 200.0));



    }

}

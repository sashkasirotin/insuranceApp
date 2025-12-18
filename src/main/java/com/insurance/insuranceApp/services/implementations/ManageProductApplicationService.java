package com.insurance.insuranceApp.services.implementations;

import com.insurance.insuranceApp.dto.ProductListResponse;
import com.insurance.insuranceApp.exception.ClientNotFoundException;
import com.insurance.insuranceApp.exception.ProductNotFoundException;
import com.insurance.insuranceApp.persistance.implementations.StorageService;
import com.insurance.insuranceApp.services.models.OrderInfo;
import com.insurance.insuranceApp.services.models.ProductInfo;
import com.insurance.insuranceApp.services.security.implementation.JwtApplicationService;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManageProductApplicationService {
    private static final Logger log = LoggerFactory.getLogger(ManageProductApplicationService.class);

    @Autowired
    public StorageService storageService;

    public ProductListResponse buyNewProduct(OrderInfo orderInfo){
        log.debug("Processing order: {}", orderInfo);
        HashMap<String, ProductInfo> products=storageService.getProductsMap();
        HashMap<String, List<ProductInfo>> clientsProductsMap=storageService.getClientsProductsMap() ;
        String clientId=orderInfo.getClientId();

        if(clientId==null){
            log.warn("Order rejected: missing clientId");
            throw new ValidationException("clientId is required");
        }
        for(String pid: orderInfo.getProducts()) {
            if (!products.containsKey(pid)) {
                log.error("Order rejected: product with id {} doesn't exist",pid);
                throw new ProductNotFoundException(pid);
            }
        }

        ProductListResponse newProductList=new ProductListResponse();
        newProductList.setCustomerId(orderInfo.getClientId());
        Map<String,ProductInfo> productList=new HashMap<>();

        for(String pid:orderInfo.getProducts()){
            productList.put(pid,products.get(pid));
            if(!clientsProductsMap.isEmpty() && clientsProductsMap.containsKey(clientId)
                    && !clientsProductsMap.get(clientId).get(0).getProductId().equals(pid))
            {
                clientsProductsMap.get(clientId).add(products.get(pid));
            }
            else{
                if(!clientsProductsMap.isEmpty() && !clientsProductsMap.containsKey(clientId))
                {
                clientsProductsMap.put(clientId,(new ArrayList<ProductInfo>()));
                clientsProductsMap.get(clientId).add(products.get(pid));
                }
                else{
                    clientsProductsMap.put(clientId,(new ArrayList<ProductInfo>()));
                    clientsProductsMap.get(clientId).add(products.get(pid));
                }

            }
        }
        newProductList.setProducts(productList);
        log.info("Creating order for clientId={}", orderInfo.getClientId());
        return newProductList;
    }

    public List<ProductInfo> updateProduct(String clientId,ProductInfo updatedProductInfo){
        log.info("Updating product. clientId={}, productId={}", clientId, updatedProductInfo.getProductId());
        boolean isProductUpdatedFlag=false;
        HashMap<String, ProductInfo> products=storageService.getProductsMap();
        HashMap<String, List<ProductInfo>> clientsProductsMap=storageService.getClientsProductsMap() ;
        if(clientsProductsMap.containsKey(clientId)){
            for(ProductInfo product:clientsProductsMap.get(clientId)){
                if(product.getProductId().equals(updatedProductInfo.getProductId())){
                    product.setEffectiveDate(updatedProductInfo.getEffectiveDate());
                    product.setExpirationDate(updatedProductInfo.getExpirationDate());
                    product.setPrice(updatedProductInfo.getPrice());
                    isProductUpdatedFlag=true;
                    log.info("Product updated successfully");

                }
            }
            if(!isProductUpdatedFlag){
            log.warn("Product not found. clientId={}, productId={}",
                    clientId, updatedProductInfo.getProductId());
            throw new ProductNotFoundException(updatedProductInfo.getProductId());
            }
        }else{
            log.warn("Update failed. Client not found: {}", clientId);
            throw new ClientNotFoundException(clientId);
        }
        log.info("Product updated successfully");

        return clientsProductsMap.get(clientId);
    }

    public  List<ProductInfo> getCustomerProductList(String clientId){
        HashMap<String, ProductInfo> products=storageService.getProductsMap();
        HashMap<String, List<ProductInfo>> clientsProductsMap=storageService.getClientsProductsMap() ;

        if(clientsProductsMap.containsKey(clientId)){
            return clientsProductsMap.get(clientId);
        }
        return null;
    }

}

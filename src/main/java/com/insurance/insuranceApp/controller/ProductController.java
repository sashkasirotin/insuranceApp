package com.insurance.insuranceApp.controller;

import com.insurance.insuranceApp.dto.ApiError;
import com.insurance.insuranceApp.dto.ApiResponse;
import com.insurance.insuranceApp.dto.ErrorResponse;
import com.insurance.insuranceApp.dto.ProductListResponse;
import com.insurance.insuranceApp.services.implementations.ManageProductApplicationService;
import com.insurance.insuranceApp.services.models.OrderInfo;
import com.insurance.insuranceApp.services.models.ProductInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/v1")
@io.swagger.v3.oas.annotations.tags.Tag(
        name = "Products",
        description = "Product management operations"
)
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ManageProductApplicationService manageProductApplicationService;

    @Operation(
            summary = "Get products",
            description = "Retrieve products for a client. If clientId is omitted, returns all products."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access Denied - Cannot access another client's data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<List<ProductInfo>>> getProducts(
            @Parameter(
                    description = "Client ID",
                    example = "1234",
                    required = true
            )
            @RequestParam(required = true) String clientId,
            Authentication authentication
    ) {
        String authenticatedClientId = authentication.getName();

        // Validate that user is accessing their own data
        if (clientId != null && !clientId.equals(authenticatedClientId)) {
            log.warn("Access denied: User {} attempted to access client {}",
                    authenticatedClientId, clientId);
            throw new AccessDeniedException("Cannot access another client's data");
        }

        log.info("Get products request. clientId={}, authenticatedUser={}",
                clientId, authenticatedClientId);

        List<ProductInfo> products = manageProductApplicationService.getCustomerProductList(clientId);

        log.info("Products retrieved. clientId={}, count={}",
                clientId, products == null ? 0 : products.size());

        return ResponseEntity.ok(
                ApiResponse.success(products, "Products retrieved successfully")
        );
    }

    @Operation(
            summary = "Update product",
            description = "Update an existing product for a specific client"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access Denied - Cannot update another client's data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping(
            value = "/products/update/{clientId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<List<ProductInfo>>> updateProducts(
            @Parameter(description = "Client ID", example = "1234")
            @PathVariable String clientId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductInfo.class))
            )
            @RequestBody ProductInfo productInfo,

            Authentication authentication
    ) {
        String authenticatedClientId = authentication.getName();

        // Validate that user is updating their own data
        if (clientId != null && !clientId.equals(authenticatedClientId)) {
            log.warn("Access denied: User {} attempted to update client {}",
                    authenticatedClientId, clientId);
            throw new AccessDeniedException("Cannot modify another client's data");
        }

        log.info("Update product request. clientId={}, productId={}",
                clientId, productInfo.getProductId());

        List<ProductInfo> updatedProducts = manageProductApplicationService.updateProduct(clientId, productInfo);

        log.info("Product updated successfully. clientId={}", clientId);

        return ResponseEntity.ok(
                ApiResponse.success(updatedProducts, "Product updated successfully")
        );
    }

    @Operation(
            summary = "Add (buy) new product",
            description = "Create a new product order for a client"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product added successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access Denied - Cannot modify another client's data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(
            value = "/products/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<ProductListResponse>> addProducts(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order information",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderInfo.class))
            )
            @RequestBody OrderInfo orderInfo,

            Authentication authentication
    ) {
        String authenticatedClientId = authentication.getName();

        // Validate that user is creating order for their own account
        if (!authenticatedClientId.equals(orderInfo.getClientId())) {
            log.warn("Access denied: User {} attempted to create order for client {}",
                    authenticatedClientId, orderInfo.getClientId());
            throw new AccessDeniedException("Cannot create order for another client");
        }

        log.info("Buy product request. clientId={}, productCount={}",
                orderInfo.getClientId(),
                orderInfo.getProducts() == null ? 0 : orderInfo.getProducts().size());

        ProductListResponse response = manageProductApplicationService.buyNewProduct(orderInfo);

        log.info("Products purchased successfully. clientId={}, productCount={}",
                orderInfo.getClientId(),
                orderInfo.getProducts() == null ? 0 : orderInfo.getProducts().size());

        return ResponseEntity.ok(
                ApiResponse.success(response, "Product added successfully")
        );
    }
}
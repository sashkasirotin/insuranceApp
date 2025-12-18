package com.insurance.insuranceApp.controller;


import com.insurance.insuranceApp.dto.ErrorResponse;
import com.insurance.insuranceApp.services.models.ClientInfo;
import com.insurance.insuranceApp.services.security.dto.AuthenticationRequest;
import com.insurance.insuranceApp.services.security.implementation.JwtApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@io.swagger.v3.oas.annotations.tags.Tag(
        name = "Authentication",
        description = "Client authentication and registration"
)
public class AuthController {

    @Autowired
    private JwtApplicationService jwtService;

    @Operation(
            summary = "Register new client",
            description = "Register a new client in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client registered successfully",
                    content = @Content(schema = @Schema(implementation = ClientInfo.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/newUser")
    public ClientInfo addNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Client information",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClientInfo.class))
            )
            @RequestBody ClientInfo userInfo
    ) throws IllegalArgumentException {
        return jwtService.register(userInfo);
    }

    @Operation(
            summary = "Authenticate client",
            description = "Authenticate client using ID, contact method and password. Returns JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server error")

    })
    @PostMapping("/authenticate")
    public Map<String, String> authenticateAndGetToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class))
            )
            @RequestBody AuthenticationRequest authRequest
    ) throws Exception {

        ClientInfo client = jwtService.authenticate(
                authRequest.getClientId(),
                authRequest.getContactMethodeType(),
                authRequest.getContactMethodeValue(),
                authRequest.getPassword()
        );

        if (client == null) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        return Map.of("token", jwtService.generateToken(client));
    }
}

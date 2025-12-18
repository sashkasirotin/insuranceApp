
package com.insurance.insuranceApp.services.security.implementation;


import com.insurance.insuranceApp.exception.AuthenticationFailedException;
import com.insurance.insuranceApp.exception.UserAlreadyExistsException;
import com.insurance.insuranceApp.persistance.implementations.StorageService;
import com.insurance.insuranceApp.services.models.ClientInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtApplicationService {
    private static final Logger log = LoggerFactory.getLogger(JwtApplicationService.class);
    //bad practice to save the key here better in secrets.yaml config map in key vault not hardcoded
    public static final String SECRET = "3f9a1c7d4e8b2f6a9c3d7e5f1b4a8d02e7c6f9a1d3b5e8f7c2a4d6e9b0f1c3d";
    @Autowired
    public StorageService storageService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public String extractClientId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String clientId = extractClientId(token);
        return (clientId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(ClientInfo client) {

        log.debug("Generating JWT for clientId={}", client.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientId", client.getId());
        claims.put("contactType", client.getContactMethodeType());
        claims.put("contactValue", client.getContactMethodeValue());
        claims.put("role", "ROLE_USER");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(client.getId())   // clientId = subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
         return Keys.hmacShaKeyFor(keyBytes);
    }


    public ClientInfo register(ClientInfo userInfo) throws UserAlreadyExistsException {
        HashMap<String, ClientInfo> clients=storageService.getClientsMap();

        // Check if user exists
        if(clients.containsKey(userInfo.getId())){
                throw new UserAlreadyExistsException("User already exists");
            }


        ClientInfo newUser = new ClientInfo(userInfo.getId(),userInfo.getContactMethodeValue(),userInfo.getContactMethodeType(),
                "USER",
                passwordEncoder.encode(userInfo.getPassword()),
                userInfo.getName()
        );

        clients.put(userInfo.getId(),newUser);
        return newUser;
    }

    public ClientInfo authenticate(String id,String contactType,String contactValue, String password) throws AuthenticationFailedException {
        log.info("Authentication attempt. clientId={}, contactType={}",
                id, contactType);
        HashMap<String, ClientInfo> clients=storageService.getClientsMap();

        for (ClientInfo u : clients.values()) {
            if (u.getId().equals(id) && u.getContactMethodeType().equals(contactType) && u.getContactMethodeValue().equals(contactValue)) {
                if (passwordEncoder.matches(password, u.getPassword())) {
                    log.info("Authentication successful. clientId={}", id);
                    return u; // SUCCESS
                }
            }
        }
        log.warn("Authentication failed. clientId={}", id);
        return null; // failed
    }

    public UserDetails loadClientByClintId(String clientId) {
        HashMap<String, ClientInfo> clients=storageService.getClientsMap();

        try{
            if (clients.containsKey(clientId)) {
                return new org.springframework.security.core.userdetails.User(
                        clients.get(clientId).getId(),
                        clients.get(clientId).getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + clients.get(clientId).getRole()))
                );
            }
            else {
                throw new UsernameNotFoundException("User not found");
            }
        }catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }


}


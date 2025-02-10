package com.spring_cloud.eureka.client.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/api/auth/signIn") || path.equals("/api/auth/signUp")){
            return chain.filter(exchange); // 해당
        }

        String token = extractToken(exchange);

        if (token == null || !validateToken(token ,exchange)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange){
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return  authHeader.substring(7); //Bearer 만 제거
        }
        return null;
    }

    private boolean validateToken(String token, ServerWebExchange exchange){
        try{
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            log.info("#####payload :: " + claimsJws.getPayload().toString());
            Claims claims = claimsJws.getBody();
            exchange.getRequest().mutate()
                    .header("X-User-Id", claims.get("USER_ID").toString())
                    .header("X-Role", claims.get("USER_ROLE").toString())
                    .build();
            // 검증 로직 추가 가능
            return true;
        }catch (Exception e){
            return false;
        }
    }



}

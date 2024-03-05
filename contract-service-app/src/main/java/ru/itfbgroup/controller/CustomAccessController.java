package ru.itfbgroup.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * REST контроллер с кастомным доступок к эндпоинтам.
 */
@RestController
@RequestMapping(value = "/api/custom-access")
@RequiredArgsConstructor
public class CustomAccessController {

    /**
     * Метод с доступом без аутентификации. Исключение в {@link ru.itfbgroup.config.WebSecurityConfig}
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong");
    }

    /**
     * Метод с аутентификацией.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> getMe() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var keycloakAccount = (SimpleKeycloakAccount) authentication.getDetails();
        var token = keycloakAccount.getKeycloakSecurityContext().getToken();

        var response = new HashMap<String, String>();
        response.put("Principal", keycloakAccount.getPrincipal().getName());
        if (Objects.nonNull(token)) {
            response.put("Token ID", token.getId());
            response.put("Token Issuer", token.getIssuer());
            response.put("Token IssuedFor", token.getIssuedFor());
            if (Objects.nonNull(token.getPreferredUsername())) {
                response.put("Token Username", token.getPreferredUsername());
                response.put("Token Name", token.getName());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

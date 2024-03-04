package ru.itfbgroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.itfbgroup.config.KeycloakAdminClientConfig;
import ru.itfbgroup.exception.ApplicationException;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuthClientService implements AuthClientService{

    private static final String GRANT_TYPE = "grant_type";
    private static final String TOKEN = "token";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private final KeycloakAdminClientConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    private MultiValueMap<String, String> createParams(Map<String, String> customParams) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CLIENT_ID, config.getClientId());
        paramMap.add(CLIENT_SECRET, config.getClientSecret());

        customParams.forEach(paramMap::add);

        return paramMap;
    }

    private String createAuthUrl() {
        return config.getAuthServerUrl() + "realms/"
                + config.getRealm() + "/protocol/openid-connect/token";
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity(Map<String, String> customParams) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(createParams(customParams), headers);
    }

    public String authenticate(Map<String, String> customParams) {
        try {
            log.trace("Try to authenticate");

            var response = restTemplate.exchange(createAuthUrl(),
                    HttpMethod.POST,
                    createHttpEntity(customParams),
                    AccessTokenResponse.class);

            log.trace("Authentication response: {}", response);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ApplicationException("Failed to authenticate. Http status:" + response.getStatusCode());
            }

            log.trace("Authentication success");

            return Optional.ofNullable(response.getBody()).map(AccessTokenResponse::getToken).orElse(null);
        } catch (Exception e) {
            throw new ApplicationException("Error occurred while authentication", e);
        }
    }

    @Override
    public String authenticate() {
        return authenticate(Map.of(GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS));
    }

    @Override
    public String authenticate(String username, String password) {
        return authenticate(Map.of(
                GRANT_TYPE, GRANT_TYPE_PASSWORD,
                USERNAME, username,
                PASSWORD, password));
    }

    @Override
    public boolean verifyToken(String accessToken) {
        try {
            log.trace("Try to verify");

            var response = restTemplate.exchange(createAuthUrl() + "/introspect",
                    HttpMethod.POST,
                    createHttpEntity(Map.of(TOKEN, accessToken)),
                    Map.class);
            log.trace("Verification response: {}", response);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ApplicationException("Failed to verify. Http status:" + response.getStatusCode());
            }

            boolean result = Optional.ofNullable(response.getBody())
                    .map(map -> map.get("active"))
                    .filter(Boolean.TRUE::equals)
                    .isPresent();

            log.trace("Verification passed: {}", result);

            return result;
        } catch (Exception e) {
            throw new ApplicationException("Error occurred while token verification", e);
        }
    }
}

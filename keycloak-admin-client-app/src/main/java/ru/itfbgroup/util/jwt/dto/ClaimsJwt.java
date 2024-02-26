package ru.itfbgroup.util.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class ClaimsJwt {
    private String jti;
    private Integer exp;
    private Integer nbf;
    private Integer iat;
    private String iss;
    private List<String> aud;
    private String sub;
    private String typ;
    private String azp;

    @JsonProperty(value = "auth_time")
    private Integer authTime;

    @JsonProperty(value = "session_state")
    private String sessionState;
    private String acr;

    @JsonProperty(value = "allowed-origins")
    private List<String> allowedOrigins;

    @JsonProperty(value = "realm_access")
    private RealmAccess realmAccess;

    @JsonProperty(value = "resource_access")
    private ResourceAccess resourceAccess;
    private String scope;
    private String clientId;

    @JsonProperty(value = "email_verified")
    private Boolean emailVerified;
    private String name;
    private List<String> groups;

    @JsonProperty(value = "preferred_username")
    private String preferredUsername;

    @JsonProperty(value = "given_name")
    private String givenName;

    @JsonProperty(value = "family_name")
    private String familyName;
    private String patronymic;
    private String email;
}

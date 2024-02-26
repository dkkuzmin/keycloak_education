package ru.itfbgroup;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itfbgroup.util.MapperUtil;

import java.util.Optional;

import static ru.itfbgroup.util.MapperUtil.getSignedJWT;
import static ru.itfbgroup.util.MapperUtil.jwtFromSignedJwtString;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    // Строка AccessToken полученная сервисом для авторизации доступа к какому-либо ресурсу. Токен внешнего сервиса
    private final String ANOTHER_JWT = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJKSEJ5S1A5UlFCWUpOUHQtb1l6WjlpVTJBRGNKUGE3WlJLV050Q1htZ01BIn0.eyJleHAiOjE3MDgzNDA2NjcsImlhdCI6MTcwODM0MDM2NywianRpIjoiYTQzZmViYTEtOWUzNS00YzFlLTg3YjUtYzhlMTNkMjkyYmEwIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL3NiaW4iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiMmYyZTZmNTYtZDJlYS00NWE4LThhZjktMWQ1ODA0Yjg0MzgwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoia2V5Y2xvYWstYWRtaW4tY2xpZW50LWFwcCIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJiYW5rX2FkbWlucyIsImJhbmtfbWljcm9zZXJ2aWNlIiwiZGVmYXVsdC1yb2xlcy1zYmluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJtYW5hZ2UtdXNlcnMiLCJ2aWV3LXVzZXJzIiwicXVlcnktZ3JvdXBzIiwicXVlcnktdXNlcnMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImNsaWVudElkIjoia2V5Y2xvYWstYWRtaW4tY2xpZW50LWFwcCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjE3Mi4xOC4wLjEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQta2V5Y2xvYWstYWRtaW4tY2xpZW50LWFwcCIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTguMC4xIn0.SiR4w3eoCy6Qzns0Z-ZIgVT0FEhZIKfjLe3TpyT3RqPKo98EOw0cIllJFwtOrap3B-h3drqtT9WB4oLpSP_RlXYFTkBPGJdeXuqhCE6yphVlAhJY1KzVirZAq6l1ydWF7K-ucpEGcpQfDhX8Gpu0wsTJ1b5JXhFQ6YqfCEdvjFoBc3AeLmKA4fY8i8CyyyTkhtDe7awHZNQ1ET9YHARYn7ZcGzGRPAC_vJSab_3hwvz16T8bMWEUY_8oJCoXXRjof2fh8Wchr_dAoB9rfbLJy3R9Lhw1AyY13IzibEUBx2RKeanoBTeDRGA7h0hfjuA_TEVrVx6u2v7faPc_36gfVg";
    private final String BANK_MICROSERVICE = "bank_microservice";
    private final String RESPONSE_TEXT = "Сторонний сервис %s обладает ролью '%s'";

    @Autowired
    private Keycloak keycloak;

    @Test
    @DisplayName("Список ролей нашего микросервиса")
    void listRolesTest() {
        var accessTokenResponse = keycloak.tokenManager().getAccessToken();
        var optSignedJWT = getSignedJWT(accessTokenResponse.getToken());

        optSignedJWT.ifPresent(signedJWT -> {
            System.out.println("Роли сервиса из Payload: " + jwtFromSignedJwtString(signedJWT).realmAccess().roles());
        });
        Assertions.assertNotNull(accessTokenResponse);
        Assertions.assertTrue(optSignedJWT.isPresent());
    }

    @Test
    @DisplayName("Список ролей стороннего микросервиса")
    void manualAccessTokenVerifyTest() {
       var optClaimsJwt = Optional.of(ANOTHER_JWT).map(MapperUtil::jwtFromSignedJwtString);
        optClaimsJwt.ifPresent(claimsJwt -> {
            var roles = claimsJwt.realmAccess().roles();
            System.out.println("Роли из внешнего AccessToken: " + claimsJwt.realmAccess().roles());
            var sign = roles.contains(BANK_MICROSERVICE);
            if (roles.contains(BANK_MICROSERVICE)) {
                System.out.printf((RESPONSE_TEXT) + "%n", Strings.EMPTY, BANK_MICROSERVICE);
            } else {
                System.out.printf((RESPONSE_TEXT) + "%n","НЕ", BANK_MICROSERVICE);
            }
        });
        Assertions.assertTrue(optClaimsJwt.isPresent());
    }



}

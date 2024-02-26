package ru.itfbgroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static ru.itfbgroup.util.MapperUtil.getSignedJWT;
import static ru.itfbgroup.util.MapperUtil.jwtFromSignedJwtString;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Keycloak keycloak;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            String delimiter = "-------------------------------------------------------------------------------------------------";
            log.info(delimiter);
            log.info("Аутентификационные данные приложения на KeyCloak сервере");
            log.info(delimiter);
            var accessTokenResponse = keycloak.tokenManager().getAccessToken();
            log.info("AccessToken:");
            log.info("  JWT String: {}", accessTokenResponse.getToken());
            var optSignedJWT = getSignedJWT(accessTokenResponse.getToken());
            optSignedJWT.ifPresent(signedJWT -> {
                log.info("  Содержимое JWT:");
                log.info("    Header: {}", signedJWT.getHeader());
                log.info("    Payload: {}", signedJWT.getPayload());
                log.info("    Signature: {}", signedJWT.getSignature());
                log.info("      Роли Client из поля Payload AccessToken-а:");
                log.info("        {}", jwtFromSignedJwtString(signedJWT).realmAccess().roles());
            });
            log.info(delimiter);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}

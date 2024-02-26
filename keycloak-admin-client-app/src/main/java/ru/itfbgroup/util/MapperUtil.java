package ru.itfbgroup.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.ObjectUtils;
import ru.itfbgroup.util.jwt.dto.ClaimsJwt;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Optional;

@Slf4j
@UtilityClass
public class MapperUtil {

    public static final ObjectMapper OBJECT_MAPPER = makeObjectMapper();

    private static ObjectMapper makeObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static String toJsonString(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return Strings.EMPTY;
        }

        var result = Strings.EMPTY;
        try {
            result = OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("Error convert to string object {0}", value), e);
        }

        return result;
    }

    public static Optional<SignedJWT> getSignedJWT(String jwtString) {
        try {
            SignedJWT claims = (SignedJWT) JWTParser.parse(jwtString);
            return Optional.of(claims);
        } catch (ParseException e) {
            log.error(MessageFormat.format("Error convert JWT String {0}", jwtString), e);
        }
        return Optional.empty();
    }

    public static Optional<JSONObject> getPayloadFromSignedJwtString(String jwtString) {
        try {
            SignedJWT claims = (SignedJWT) JWTParser.parse(jwtString);
            return Optional.of(claims.getPayload().toJSONObject());
        } catch (ParseException e) {
            log.error(MessageFormat.format("Error convert JWT String {0}", jwtString), e);
        }
        return Optional.empty();
    }

    public static ClaimsJwt jwtFromSignedJwtString(String jwtString) {
        try {
            SignedJWT claims = (SignedJWT) JWTParser.parse(jwtString);
            return jwtFromSignedJwtString(claims);
        } catch (ParseException e) {
            log.info(MessageFormat.format("Error convert to Jwt String {0}", jwtString), e);
        }
        return null;
    }

    public static ClaimsJwt jwtFromSignedJwtString(SignedJWT claims) {
        return MapperUtil.toObject(claims.getPayload().toString(), ClaimsJwt.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> T toObject(String value, Class<?> clazz) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        T result = null;
        try {
            result = (T) OBJECT_MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error(MessageFormat.format("Error convert to object string {0}", value), e);
        }
        return result;
    }
}

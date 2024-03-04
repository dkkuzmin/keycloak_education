package ru.itfbgroup.service;

/**
 * Получение и проверка AccessToken
 */
public interface AuthClientService {
    /**
     * Выполняет client аутенцификацию посредством секретного ключа.
     *
     * @return AccessToken (JWT) в случае успеха
     */
    String authenticate();

    /**
     * Выполняет аутенцификацию посредством пары логин/пароль.
     *
     * @param username логин пользователя
     * @param password паррль пользователя
     * @return AccessToken (JWT) в случае успеха
     */
    String authenticate(String username, String password);

    /**
     * Выполняет проверку AccessToken (JWT)
     *
     * @return true если токен валидный, false в обратном случае
     */
    boolean verifyToken(String accessToken);
}

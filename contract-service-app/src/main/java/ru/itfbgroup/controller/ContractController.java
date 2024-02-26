package ru.itfbgroup.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itfbgroup.model.ContractDto;
import ru.itfbgroup.service.ContractService;

import java.util.UUID;

/**
 * REST контроллер contracts.
 * Энпоинты контроллера закрыты {@link ru.itfbgroup.config.WebSecurityConfig} для всех кроме роли "bank_service"
 */
@RestController
@RequestMapping(value = "/api/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ContractController {

    private final ContractService service;

    @GetMapping()
    @ResponseBody
    public ResponseEntity<ContractDto> get(@RequestParam UUID id) {
        log.info("Request contract with id = {}", id);
        try {
            var response = service.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            log.error("Error process http request: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public ResponseEntity<ContractDto> uuid(@PathVariable("uuid") UUID uuid) {
        log.info("Request contract with id = {}", uuid);
        try {
            var response = service.getById(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            log.error("Error process http request: " + ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

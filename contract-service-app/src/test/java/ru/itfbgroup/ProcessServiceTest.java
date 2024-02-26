package ru.itfbgroup;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProcessServiceTest {

    private ProcessService service;

    private static final UUID PROCESS_ID = UUID.randomUUID();
    private static final String NAME = "name";

    @BeforeEach
    void init() {
        service = new ProcessServiceImpl();
    }

    @Test
    void startProcessTest() {
        var request = StartProcessRequest
                .builder()
                .id(PROCESS_ID)
                .name(NAME)
                .build();

        var response = service.start(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(PROCESS_ID, response.getId());
        Assertions.assertEquals(NAME, response.getName());
        Assertions.assertNotNull(response.getStartedDateTime());
    }

    @Test
    @SneakyThrows
    void emptyTest() {
       // TODO

    }
}

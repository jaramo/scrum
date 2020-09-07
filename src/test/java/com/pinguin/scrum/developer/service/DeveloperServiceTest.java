package com.pinguin.scrum.developer.service;

import com.pinguin.scrum.developer.repository.DeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class DeveloperServiceTest {

//    @Test
    public void test(@Mock DeveloperRepository repoMock) {
        DeveloperService service = new DeveloperService(repoMock);

        when(repoMock.findAll()).thenReturn(Collections.emptyList());

    }
}

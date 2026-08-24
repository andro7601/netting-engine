package com.engine.controller;

import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.service.OptimizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OptimizationController.class)
@AutoConfigureRestTestClient
public class OptimizationControllerTest {
    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private OptimizationService optimizationService;


}

package com.hardcore.accounting.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class HelloControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    void testSayHello() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/v1.0/greeting").param("name", "World"))
               .andExpect(status().isOk())
               .andExpect(content().string("{\"id\":1,\"name\":\"Hello, World\"}"));

    }
}

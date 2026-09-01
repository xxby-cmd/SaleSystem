package com.xxby;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestHttpGet {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testHttpGetFailure() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/product-codes/{code}","123456"));
        result.andExpect(status().isBadRequest());
    }
    @Test
    public void testHttpGetFailure2() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/product-codes/{code}", "   "));
        result.andExpect(status().isBadRequest());
    }
    @Test
    public void testHttpGetFailure3() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/product-codes/{code}","SAu-3X-10"));
        result.andExpect(status().isBadRequest());
    }
    @Test
    public void testHttpGetSuccess() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/product-codes/{code}","SAU32X100S"));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.productCode").value("SAU32X100S"));
        result.andExpect(jsonPath("$.series").value("SAU"));
        result.andExpect(jsonPath("$.boreDiameter").value(32));
        result.andExpect(jsonPath("$.strokeLength").value(100));
        result.andExpect(jsonPath("$.suffix").value("S"));
    }
    @Test
    public void testHttpGetSuccess1() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/product-codes/{code}","SAU32X100"));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.productCode").value("SAU32X100"));
        result.andExpect(jsonPath("$.series").value("SAU"));
        result.andExpect(jsonPath("$.boreDiameter").value(32));
        result.andExpect(jsonPath("$.strokeLength").value(100));
        result.andExpect(jsonPath("$.suffix").hasJsonPath());
        result.andExpect(jsonPath("$.suffix").doesNotExist());
    }




}

package com.youhogeon.icou.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AccountTest {
    
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("회원가입 통합 테스트")
    void createAccount() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":randomtest@test.com\", \"password\":\"test\", \"nickname\":\"test\"}");

        mvc.perform(builder)
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

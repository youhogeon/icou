package com.youhogeon.icou.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.youhogeon.icou.repository.AccountRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    AccountRepository accountRepository;

    @Test
    void 회원가입_성공() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"test@test.com\", \"password\":\"test\", \"nickname\":\"test\"}");

        mvc.perform(builder)
        .andExpect(status().isOk());
    }

    @Test
    void 회원가입_실패_이메일형식() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"testtest.com\", \"password\":\"test\", \"nickname\":\"안녕안녕반가워반가워\"}");

        mvc.perform(builder)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("ILLEGAL_ARGUMENT"));
    }

    @Test
    void 회원가입_실패_비밀번호길이() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"test@test.com\", \"password\":\"t\", \"nickname\":\"test\"}");

        mvc.perform(builder)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("ILLEGAL_ARGUMENT"));
    }

    @Test
    void 회원가입_실패_닉네임길이() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"test@test.com\", \"password\":\"test\", \"nickname\":\"안녕안녕반가워진짜반가워\"}");

        mvc.perform(builder)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("ILLEGAL_ARGUMENT"));
    }

}

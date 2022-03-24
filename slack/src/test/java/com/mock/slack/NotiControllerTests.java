package com.mock.slack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NotiControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void 알림_성공_앤드포인트() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/chat.postMessage")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"channel\":\"myChannel\",\"text\":\"[normal] normal\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"result\":\"ok\"}")));
    }
    @Test
    void 알림_실패_앤드포인트() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/chat.postMessage.fail")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"channel\":\"myChannel\",\"text\":\"[normal] normal\"}")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(equalTo("{\"result\":\"fail\"}")));
    }
    @Test
    void 알림_지연_앤드포인트() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/chat.postMessage.busy")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"channel\":\"myChannel\",\"text\":\"[normal] normal\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"result\":\"ok\"}")));
    }
}

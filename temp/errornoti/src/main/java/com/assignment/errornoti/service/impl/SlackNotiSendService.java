package com.assignment.errornoti.service.impl;

import com.assignment.errornoti.service.NotiSendService;
import com.assignment.errornoti.vo.NotiMessage;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Service
public class SlackNotiSendService implements NotiSendService {
    @Value("${mockslack.ip}")
    private String notiServerIp;

    @Value("${mockslack.port}")
    private String notiServerPort;

    @Value("http://")
    private String protocol;

    @Value("${mockslack.type}")
    private String mockSlackType;

    private Queue<NotiMessage> queue = new LinkedList();

    private void sendFallback(NotiMessage message, Exception e){
        log.error("noti send fallback executed : " + e.toString());
        queue.add(message);
        log.warn("queue size : "+queue.size());
    }
    @Override
    @CircuitBreaker(name = "errornoti", fallbackMethod = "sendFallback")
    public void send(NotiMessage message) throws IOException {
        String apiPath = "";
        String baseUri = protocol + notiServerIp + ":" + notiServerPort;
        switch (mockSlackType) {
            case "fail":
                apiPath = baseUri + "/api/chat.postMessage.fail";
                break;
            case "busy":
                apiPath = baseUri + "/api/chat.postMessage.busy";
                break;
            case "normal":
                apiPath = baseUri + "/api/chat.postMessage";
                break;
            default:
                apiPath = baseUri + "/api/chat.postMessage";
        }
        HttpPost post = new HttpPost(
                URI.create(apiPath)
        );
        post.setEntity(new StringEntity(message.toJsonString()));
        post.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        post.setHeader(HttpHeaders.AUTHORIZATION, "Bear " + message.getToken());
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            log.info(readHttpResponse(httpResponse));
        }
    }

    private String readHttpResponse(CloseableHttpResponse httpResponse) throws IOException {
        StringBuffer result = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                result.append(inputLine);
            }
        }

        return result.toString();
    }
}

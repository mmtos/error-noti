package com.mock.slack.noti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NotiController {

    private int maxBusyResponseCount = 7;

    @PostMapping(path = "/api/chat.postMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotiResponseDTO> postMessage(@RequestBody NotiRequestDTO payload) {
        log.debug(payload.toString());
        return ResponseEntity.ok(new NotiResponseDTO("ok"));
    }

    @PostMapping(path = "/api/chat.postMessage.fail", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotiResponseDTO> postMessageFail(@RequestBody NotiRequestDTO payload) {
        log.debug(payload.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new NotiResponseDTO("fail"));
    }

    @PostMapping(path = "/api/chat.postMessage.busy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotiResponseDTO> postMessageBusy(@RequestBody NotiRequestDTO payload) {
        log.debug(payload.toString());
        int throttlingSecond = 5;
        try {
            if (maxBusyResponseCount > 0) {
                Thread.sleep(throttlingSecond * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(maxBusyResponseCount > 0) {
            maxBusyResponseCount--;
            log.info("im busy.. remaining.." + maxBusyResponseCount);
        }
        return ResponseEntity.ok(new NotiResponseDTO("ok"));
    }
}

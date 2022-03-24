package com.assignment.errornoti.web;

import com.assignment.errornoti.entity.NotiGroup;
import com.assignment.errornoti.entity.NotiUser;
import com.assignment.errornoti.service.NotiSendService;
import com.assignment.errornoti.service.NotiTargetSelector;
import com.assignment.errornoti.service.NotiUserService;
import com.assignment.errornoti.vo.NotiMessage;
import com.assignment.errornoti.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class NotiController {

    private final NotiTargetSelector notiTargetSelector;
    private final NotiSendService notiSendService;
    private final NotiUserService notiUserService;

    @PostMapping("/alert")
    public ResponseEntity<AlertResponseDTO> alert(@RequestBody AlertRequestDTO payload){
        //1. 대상 유저를 선별
        List<NotiUser> targetUserList = notiTargetSelector.selectNotiTargetUser(payload.getTarget());

        //2. 알림 메시지 생성
        String text = String.format("[%s] %s",payload.getSeverity(),payload.getMessage());

        //3. 알림 보내기
        for(NotiUser target : targetUserList){
            try {
                notiSendService.send(new NotiMessage(target.getNotiUserToken(),target.getNotiUserChannel(),text));
            } catch (IOException e) {
                log.error("{}에게 알림 전송중 에러 발생 : {}",target.toString(), e.getMessage());
            }
        }
        return ResponseEntity.accepted().body(new AlertResponseDTO(targetUserList.size()));
    }

    @GetMapping("/group/{notiGroupId}")
    public ResponseEntity<GetGroupResponseDTO> getGroup(@PathVariable(name = "notiGroupId") String notiGroupId){
        NotiGroup group = notiUserService.findGroupByGroupId(notiGroupId);
        GetGroupResponseDTO responseDTO = new GetGroupResponseDTO(group.getNotiGroupId(),group.getNotiGroupName());
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/group")
    public ResponseEntity<Void> addGroup(@RequestBody AddGroupRequestDTO payload){
        String newGroupName = payload.getNotiGroupName();
        NotiGroup group = notiUserService.addGroup(newGroupName);
        return ResponseEntity.created(URI.create("/group/"+group.getNotiGroupId())).build();
    }

    @PutMapping("/enroll")
    public ResponseEntity<String> enrollUserFromGroup(@RequestBody EnrollUserRequestDTO payload){

        notiUserService.enrollUserToGroup(payload.getNotiUserName(), payload.getNotiGroupName());

        return ResponseEntity.ok("Enrolled");
    }

    @DeleteMapping("/withdraw/user/{notiUserName}/group/{notiGroupName}")
    public ResponseEntity<String> withdrawUserFromGroup(
            @PathVariable(name = "notiUserName") String notiUserName
            , @PathVariable(name = "notiGroupName") String notiGroupName
    ){
        notiUserService.withdrawUserFromGroup(notiUserName, notiGroupName);
        return ResponseEntity.ok("Withdrew");
    }
}

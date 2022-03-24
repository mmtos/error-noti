package com.assignment.errornoti;

import com.assignment.errornoti.entity.NotiUser;
import com.assignment.errornoti.service.NotiTargetSelector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class NotiTargetSelectorTest {
    @Autowired
    private NotiTargetSelector notiTargetSelector;

    @Test
    public void all이_들어간_경우() throws Exception {
        List<String> targetList = List.of("@all","@user1","@user2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user1","user2","user3");
    }

    @Test
    public void 유저명만_있는_경우() throws Exception {
        List<String> targetList = List.of("@user1","@user2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user1","user2");
    }
    @Test
    public void 없는_유저명이_포함된_경우() throws Exception {
        //없는 유저명이 전달된 경우, 무시된다.
        List<String> targetList = List.of("@user45","@user2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user2");
    }
    @Test
    public void 그룹명만_있는_경우() throws Exception {
        List<String> targetList = List.of("@@group1","@@group2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user1","user2","user3");
    }
    @Test
    public void 없는_그룹명이_포함된_경우() throws Exception {
        //없는 그룹명이 전달된 경우, 무시된다.
        List<String> targetList = List.of("@@group8","@@group2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user1","user2");
    }
    @Test
    public void all이_포함되지_않은_경우() throws Exception {
        //없는 그룹명이 전달된 경우, 무시된다.
        List<String> targetList = List.of("@@group3","@@group2","@user2");
        List<NotiUser> targetUsers = notiTargetSelector.selectNotiTargetUser(targetList);
        List<String> targetUserNames = targetUsers.stream().map((user) -> user.getNotiUserName()).collect(Collectors.toList());
        Assertions.assertThat(targetUserNames).containsOnly("user1","user2");
    }

}

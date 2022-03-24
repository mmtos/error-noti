package com.assignment.errornoti.service;

import com.assignment.errornoti.dao.NotiDAO;
import com.assignment.errornoti.entity.NotiUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotiTargetSelector {
    private final NotiDAO notiDao;

    private static final String USER_TAG = "@";
    private static final String ALL_USER_TAG = "@all";
    private static final String GROUP_TAG = "@@";

    public List<NotiUser> selectNotiTargetUser(List<String> targetList){
        if(targetList.contains(ALL_USER_TAG)){
            return getAllUser();
        }

        List<NotiUser> userList = new ArrayList<>();

        for(String target : targetList){
            if(target.contains(GROUP_TAG)){
                userList.addAll(getUsersByGroupName(target.replaceAll(GROUP_TAG,"")));
            }else if(target.contains(USER_TAG)){
                NotiUser user = getUserByUserName(target.replaceAll(USER_TAG,""));
                if(user != null){
                    userList.add(user);
                }
            }
        }
        return userList
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<NotiUser> getAllUser(){
        return notiDao.findAllUser();
    }

    private List<NotiUser> getUsersByGroupName(String groupName){
        return notiDao.findUsersByGroupName(groupName);
    }

    private NotiUser getUserByUserName(String userName){
        return notiDao.findUserByUserName(userName);
    }

}

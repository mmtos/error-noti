package com.assignment.errornoti.service;

import com.assignment.errornoti.dao.NotiDAO;
import com.assignment.errornoti.entity.NotiGroup;
import com.assignment.errornoti.entity.NotiGroupUserMapping;
import com.assignment.errornoti.entity.NotiUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotiUserService {

    private final NotiDAO notiDao;
    public NotiGroup findGroupByGroupId(String notiGroupId) {
        return notiDao.findGroupByGroupId(notiGroupId);
    }

    public NotiGroup addGroup(String newGroupName) {
        NotiGroup newGroup = new NotiGroup();
        newGroup.setNotiGroupName(newGroupName);
        notiDao.createGroup(newGroup);
        return newGroup;
    }

    public void enrollUserToGroup(String notiUserName, String notiGroupName) {
        NotiUser user = notiDao.findUserByUserName(notiUserName);
        NotiGroup group = notiDao.findGroupByGroupName(notiGroupName);
        NotiGroupUserMapping mapping = new NotiGroupUserMapping(group.getNotiGroupId(), user.getNotiUserId());
        notiDao.enrollUserToGroup(mapping,notiGroupName);
    }

    public void withdrawUserFromGroup(String notiUserName, String notiGroupName) {
        NotiUser user = notiDao.findUserByUserName(notiUserName);
        NotiGroup group = notiDao.findGroupByGroupName(notiGroupName);
        NotiGroupUserMapping mapping = new NotiGroupUserMapping(group.getNotiGroupId(), user.getNotiUserId());

        notiDao.withdrawUserFromGroup(mapping,notiGroupName);
    }
}

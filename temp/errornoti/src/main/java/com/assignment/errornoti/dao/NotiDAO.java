package com.assignment.errornoti.dao;

import com.assignment.errornoti.entity.NotiGroup;
import com.assignment.errornoti.entity.NotiGroupUserMapping;
import com.assignment.errornoti.entity.NotiUser;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotiDAO {
    private final SqlSession sqlSession;

    @Cacheable(value = "notiAllUserCache", key = "#root.method")
    public List<NotiUser> findAllUser(){
        return sqlSession.selectList("NotiUser.selectAllUser");
    }

    @Cacheable(value = "notiUserCache", key = "#groupName")
    public List<NotiUser> findUsersByGroupName(String groupName) {
        return sqlSession.selectList("NotiGroupUserMapping.findUsersByGroupName", groupName);
    }

    @Cacheable(value = "notiUserCache", key = "#userName")
    public NotiUser findUserByUserName(String userName) {
        return sqlSession.selectOne("NotiUser.findUserByUserName", userName);
    }

    @Cacheable(value = "notiGroupCache", key = "#notiGroupName")
    public NotiGroup findGroupByGroupName(String notiGroupName) {
        return sqlSession.selectOne("NotiGroup.findGroupByGroupName", notiGroupName);
    }

    @Cacheable(value = "notiGroupCache", key = "#notiGroupId")
    public NotiGroup findGroupByGroupId(String notiGroupId) {
        return sqlSession.selectOne("NotiGroup.findGroupByGroupId", notiGroupId);
    }

    @CacheEvict(cacheNames = "notiUserCache", key="#notiGroupName")
    public void withdrawUserFromGroup(NotiGroupUserMapping mapping, String notiGroupName) {
        sqlSession.delete("NotiGroupUserMapping.withdrawUserFromGroup",mapping);
    }

    @CacheEvict(cacheNames = "notiUserCache", key="#notiGroupName")
    public void enrollUserToGroup(NotiGroupUserMapping mapping, String notiGroupName) {
        sqlSession.insert("NotiGroupUserMapping.enrollUserToGroup", mapping);
    }

    public void createGroup(NotiGroup newGroup) {
        sqlSession.insert("NotiGroup.createGroup", newGroup);
    }


}

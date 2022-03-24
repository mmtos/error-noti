package com.assignment.errornoti.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"notiUserId","notiUserName"})
@ToString
public class NotiUser implements Serializable {
    private String notiUserId;
    private String notiUserName;
    private String notiUserChannel;
    private String notiUserToken;
}

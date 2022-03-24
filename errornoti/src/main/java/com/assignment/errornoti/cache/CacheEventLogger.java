package com.assignment.errornoti.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, Object> {
    public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        log.debug("{} ::: getKey: {} / getOldValue: {} / getNewValue:{}",cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
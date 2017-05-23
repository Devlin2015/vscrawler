package com.virjar.vscrawler.core.event.systemevent;

import java.util.Properties;

import com.virjar.vscrawler.core.event.support.AutoEvent;

/**
 * Created by virjar on 17/5/2.
 * @author virjar
 * @since 0.0.1
 */
public interface CrawlerConfigChangeEvent {
    /**
     * 属性文件变化事件
     * 
     * @param oldProperties 旧配置文件内容
     * @param newProperties 新配置文件内容
     */
    @AutoEvent(sync = true)
    void configChange(Properties oldProperties, Properties newProperties);
}
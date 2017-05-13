package com.virjar.vscrawler.processor;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Created by virjar on 17/4/16.
 * 
 * @author virjar
 * @since 0.0.1
 */
public class CrawlResult {
    /**
     * 一个种子可能产生多个结果
     */
    private List<String> result = Lists.newLinkedList();
    private List<String> newSeed = Lists.newLinkedList();
    private boolean retry = false;
    private boolean sessionEnable = true;

    public boolean isSessionEnable() {
        return sessionEnable;
    }

    public void setSessionEnable(boolean sessionEnable) {
        this.sessionEnable = sessionEnable;
    }

    public List<String> getNewSeed() {
        return newSeed;
    }

    public void addNewSeed(List<String> newSeed) {
        this.newSeed.addAll(newSeed);
    }

    public List<String> getResult() {
        return result;
    }

    public void addResult(List<String> result) {
        this.result.addAll(result);
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }
}

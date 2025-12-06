package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Delay {
    String constructionUid;
    String scheduleUid;
    String title;
    String reason;
    boolean isExcusable;
    boolean isCompensable;
    boolean isConcurrent;
    boolean isCritical;
    String aditionalInfo;
    int days;
    @Exclude
    String delayUid;

    public Delay() {}

    public Delay(String constructionUid, String scheduleUid, String title, String reason, boolean isExcusable, boolean isCompensable, boolean isConcurrent, boolean isCritical, int days, String aditionalInfo){
        this.constructionUid  = constructionUid;
        this.scheduleUid = scheduleUid;
        this.title = title;
        this.reason = reason;
        this.isExcusable = isExcusable;
        this.isCompensable = isCompensable;
        this.isConcurrent = isConcurrent;
        this.isCritical = isCritical;
        this.days = days;
        this.aditionalInfo = aditionalInfo;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getConstructionUid() {
        return constructionUid;
    }

    public void setConstructionUid(String constructionUid) {
        this.constructionUid = constructionUid;
    }

    public boolean isConcurrent() {
        return isConcurrent;
    }

    public void setConcurrent(boolean isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getAditionalInfo() {
        return aditionalInfo;
    }

    public void setAditionalInfo(String aditionalInfo) {
        this.aditionalInfo = aditionalInfo;
    }

    public String getScheduleUid() {
        return scheduleUid;
    }

    public void setScheduleUid(String scheduleUid) {
        this.scheduleUid = scheduleUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isExcusable() {
        return isExcusable;
    }

    public void setExcusable(boolean excusable) {
        isExcusable = excusable;
    }

    public boolean isCompensable() {
        return isCompensable;
    }

    public void setCompensable(boolean compensable) {
        isCompensable = compensable;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    @Exclude
    public String getDelayUid() {
        return delayUid;
    }

    @Exclude
    public void setDelayUid(String delayUid) {
        this.delayUid = delayUid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("constructionUid", constructionUid);
        result.put("scheduleUid", scheduleUid);
        result.put("title", title);
        result.put("reason", reason);
        result.put("isExcusable", isExcusable);
        result.put("isCompensable", isCompensable);
        result.put("isConcurrent", isConcurrent);
        result.put("isCritical", isCritical);
        result.put("days", days);
        result.put("aditionalInfo", aditionalInfo);
        return result;
    }

}

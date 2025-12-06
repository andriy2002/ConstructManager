package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Schedule {
    String constructionUid;
    String title;
    String deadline;
    String finishDate;
    String state;
    @Exclude
    List<Delay> delays;
    @Exclude
    String scheduleUid;

    public Schedule() {
        // Default constructor required for calls to DataSnapshot.getValue(Schedule.class)
    }

    public Schedule(String constructionUid, String title, String deadline, String state) {
        this.constructionUid = constructionUid;
        this.title = title;
        this.deadline = deadline;
        this.state = state;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("constructionUid", constructionUid);
        result.put("title", title);
        result.put("deadline", deadline);
        result.put("state", state);
        result.put("finishDate", finishDate);

        return result;
    }

    public String getConstructionUid() {
        return constructionUid;
    }

    public void setConstructionUid(String constructionUid) {
        this.constructionUid = constructionUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Exclude
    public List<Delay> getDelays() {
        return delays;
    }

    @Exclude
    public void setDelays(List<Delay> delays) {
        this.delays = delays;
    }

    @Exclude
    public String getScheduleUid() {
        return scheduleUid;
    }

    @Exclude
    public void setScheduleUid(String scheduleUid) {
        this.scheduleUid = scheduleUid;
    }


}

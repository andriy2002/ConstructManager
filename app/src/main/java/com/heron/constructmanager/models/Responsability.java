package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Responsability {
    public String constructionUid;
    public String responsibleEmail;
    public String title;
    public String desc;
    public String deadline;
    public String state;
    public String responsabilityUid;

    public Responsability() {
        // Default constructor required for calls to DataSnapshot.getValue(Responsability.class)
    }

    public Responsability(String constructionUid, String title, String desc, String deadline, String state,  String responsibleEmail) {
        this.constructionUid = constructionUid;
        this.responsibleEmail = responsibleEmail;
        this.title = title;
        this.desc = desc;
        this.deadline = deadline;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getResponsabilityUid() {
        return responsabilityUid;
    }

    public void setResponsabilityUid(String responsabilityUid) {
        this.responsabilityUid = responsabilityUid;
    }

    public String getConstructionUid() {
        return constructionUid;
    }

    public void setConstructionUid(String constructionUid) {
        this.constructionUid = constructionUid;
    }

    public String getResponsibleEmail() {
        return responsibleEmail;
    }

    public void setResponsibleEmail(String responsibleEmail) {
        this.responsibleEmail = responsibleEmail;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("constructionUid", constructionUid);
        result.put("responsibleEmail", responsibleEmail);
        result.put("title", title);
        result.put("desc", desc);
        result.put("deadline", deadline);
        result.put("state", state);

        return result;
    }

}

package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Budget {
    public String constructionUid;
    public String title;
    public String desc;
    public float value;
    @Exclude
    public String budgetUid;

    @Exclude
    public String getBudgetUid() {
        return budgetUid;
    }

    @Exclude
    public void setBudgetUid(String budgetUid) {
        this.budgetUid = budgetUid;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Budget() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("constructionUid", constructionUid);
        result.put("title", title);
        result.put("desc", desc);
        result.put("value", value);
        return result;
    }

    public Budget(String constructionUid, String title, String desc, float value) {
        this.constructionUid = constructionUid;
        this.title = title;
        this.desc = desc;
        this.value = value;
    }
}

package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Construction {

    Information information;
    @Exclude
    List<Responsability> responsabilities;
    @Exclude
    List<Schedule> schedules;
    @Exclude
    List<Budget> budgets;
    @Exclude
    String constructionUid;

    @Exclude
    public List<Budget> getBudgets() {
        return budgets;
    }

    @Exclude
    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public Information getInformation(){ return information; }

    public void setInformation(Information information){ this.information = information; }

    @Exclude
    public List<Responsability> getResponsabilities(){ return responsabilities; }

    @Exclude
    public void setResponsabilities(List<Responsability> responsabilities){ this.responsabilities = responsabilities; }

    @Exclude
    public List<Schedule> getSchedules(){ return schedules; }

    @Exclude
    public void setSchedules(List<Schedule> schedules){ this.schedules = schedules; }

    @Exclude
    public String getUid() {
        return constructionUid;
    }

    @Exclude
    public void setUid(String uid) {
        this.constructionUid = uid;
    }


    public Construction() {
        // Default constructor required for calls to DataSnapshot.getValue(Construction.class)
    }

    public Construction(Information information) {
        this.information = information;
    }

}
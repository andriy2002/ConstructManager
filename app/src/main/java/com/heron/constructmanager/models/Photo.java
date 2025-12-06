package com.heron.constructmanager.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Photo {
    String constructionUid;
    String title;
    String desc;
    String url;          // <-- додаємо URL фото для Glide
    @Exclude
    String photoUid;

    public Photo() { }

    // Конструктор для Firebase Storage
    public Photo(String constructionUid, String url, String desc) {
        this.constructionUid = constructionUid;
        this.url = url;        // посилання на фото
        this.desc = desc;      // опис
    }

    // Геттери/Сеттери
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return desc; }
    public void setDescription(String desc) { this.desc = desc; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getConstructionUid() { return constructionUid; }
    public void setConstructionUid(String constructionUid) { this.constructionUid = constructionUid; }

    @Exclude
    public String getPhotoUid() { return photoUid; }
    @Exclude
    public void setPhotoUid(String photoUid) { this.photoUid = photoUid; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("constructionUid", constructionUid);
        result.put("url", url);
        result.put("desc", desc);
        return result;
    }
}

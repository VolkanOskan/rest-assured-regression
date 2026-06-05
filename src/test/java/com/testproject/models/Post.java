package com.testproject.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @JsonProperty("id")     private Integer id;
    @JsonProperty("userId") private Integer userId;
    @JsonProperty("title")  private String title;
    @JsonProperty("body")   private String body;

    public Post() {}

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public Integer getId()               { return id; }
    public Integer getUserId()           { return userId; }
    public String  getTitle()            { return title; }
    public String  getBody()             { return body; }
    public void    setId(Integer id)     { this.id = id; }
    public void    setUserId(Integer u)  { this.userId = u; }
    public void    setTitle(String t)    { this.title = t; }
    public void    setBody(String b)     { this.body = b; }
}
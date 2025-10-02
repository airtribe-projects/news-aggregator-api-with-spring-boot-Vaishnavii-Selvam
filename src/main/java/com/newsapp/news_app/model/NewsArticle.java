package com.newsapp.news_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NewsArticle {
    private String id;
    private String title;
    private String description;
    private String url;
    private String content;

    @JsonProperty("urlToImage")
    private String imageUrl;

    private String source;
    private String author;
    private LocalDateTime publishedAt;
    private String category;

    public NewsArticle() {
    }
    public NewsArticle(String id, String title, String description, String url, String content, String imageUrl, String source, String author, LocalDateTime publishedAt, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.content = content;
        this.imageUrl = imageUrl;
        this.source = source;
        this.author = author;
        this.publishedAt = publishedAt;
        this.category = category;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(final LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }
}

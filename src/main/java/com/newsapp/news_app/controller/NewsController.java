package com.newsapp.news_app.controller;

import com.newsapp.news_app.model.NewsArticle;
import com.newsapp.news_app.service.NewsService;
import com.newsapp.news_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<NewsArticle>> getNews() {
        Set<String> preferences = userService.getUserPreferences();
        List<NewsArticle> articles = newsService.getNewsByCategoriesAndKeywords(List.copyOf(preferences),null);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<NewsArticle>> searchNews(@PathVariable String keyword) {
        Set<String> preferences = userService.getUserPreferences();
        List<NewsArticle> articles = newsService.getNewsByCategoriesAndKeywords(List.copyOf(preferences),keyword);
        return ResponseEntity.ok(articles);

    }

}

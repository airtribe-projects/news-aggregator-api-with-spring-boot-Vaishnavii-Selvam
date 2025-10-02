package com.newsapp.news_app.service;

import com.newsapp.news_app.model.NewsArticle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Value("${app.news.apis.newsapi.key}")
    private String newsApiKey;

    @Value("${app.news.apis.newsapi.url}")
    private String newApiUrl;

    @Value("${app.news.apis.gnews.key}")
    private String gnewsApiKey;

    @Value("${app.news.apis.gnews.url}")
    private String gnewsApiUrl;

    private final WebClient webClient;

    public NewsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Cacheable(value = "news", key = "#categories.toString() + #keywords")
    public List<NewsArticle> getNewsByCategoriesAndKeywords(List<String> categories, String keywords) {
        List<NewsArticle> newsApiArticles = fetchFromNewsApi(categories, keywords);
        List<NewsArticle> gnewsArticles = fetchFromGNews(categories, keywords);
        return combineArticles(newsApiArticles, gnewsArticles);
    }

    private List<NewsArticle> fetchFromNewsApi(List<String> categories, String keywords) {
        try{
            return categories.stream().flatMap(category -> {
                String url = String.format("%s?category=%s&apiKey=%s&pageSize=10",newApiUrl,category,newsApiKey);
                if(keywords != null && !keywords.isEmpty()){
                    url+="&q=" +keywords;
                }

                return webClient.get().uri(url).retrieve().bodyToMono(NewsApiResponse.class)
                        .onErrorResume(e-> Mono.just(new NewsApiResponse()))
                        .blockOptional()
                        .orElse(new NewsApiResponse())
                        .getArticles()
                        .stream()
                        .map(this::convertToNewsArticle);
            }).collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }

    private List<NewsArticle> fetchFromGNews(List<String> categories, String keywords) {
        try {
            return categories.stream()
                    .flatMap(category -> {
                        String url = String.format("%s?category=%s&apikey=%s&max=10",
                                gnewsApiUrl, category, gnewsApiKey);

                        if (keywords != null && !keywords.isEmpty()) {
                            url += "&q=" + keywords;
                        }

                        return webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(GNewsResponse.class)
                                .onErrorResume(e -> Mono.just(new GNewsResponse()))
                                .blockOptional()
                                .orElse(new GNewsResponse())
                                .getArticles()
                                .stream()
                                .map(this::convertToNewsArticle);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    private NewsArticle convertToNewsArticle(NewsApiResponse.Article article) {
        return new NewsArticle(
                generateId(article.getTitle()),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getUrl(),
                article.getUrlToImage(),
                article.getSource().getName(),
                article.getAuthor(),
                article.getPublishedAt(),
                null
        );
    }

    private NewsArticle convertToNewsArticle(GNewsResponse.Article article) {
        return new NewsArticle(
                generateId(article.getTitle()),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getUrl(),
                article.getImage(),
                article.getSource().getName(),
                null,
                article.getPublishedAt(),
                null
        );
    }

    private String generateId(String title) {
        return String.valueOf(Math.abs(title.hashCode()));
    }

    private List<NewsArticle> combineArticles(List<NewsArticle>... articleLists) {
        return Arrays.stream(articleLists)
                .flatMap(List::stream)
                .distinct()
                .limit(50) // Limit total articles
                .collect(Collectors.toList());
    }

    private static class NewsApiResponse {
        private List<Article> articles;

        public List<Article> getArticles() { return articles != null ? articles : List.of(); }
        public void setArticles(List<Article> articles) { this.articles = articles; }

        public static class Article {
            private Source source;
            private String author;
            private String title;
            private String description;
            private String url;
            private String urlToImage;
            private String content;
            private LocalDateTime publishedAt;

            // Getters and Setters
            public Source getSource() { return source; }
            public void setSource(Source source) { this.source = source; }
            public String getAuthor() { return author; }
            public void setAuthor(String author) { this.author = author; }
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
            public String getUrl() { return url; }
            public void setUrl(String url) { this.url = url; }
            public String getUrlToImage() { return urlToImage; }
            public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }
            public String getContent() { return content; }
            public void setContent(String content) { this.content = content; }
            public LocalDateTime getPublishedAt() { return publishedAt; }
            public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
        }

        public static class Source {
            private String name;
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
        }
    }

    private static class GNewsResponse {
        private List<Article> articles;

        public List<Article> getArticles() { return articles != null ? articles : List.of(); }
        public void setArticles(List<Article> articles) { this.articles = articles; }

        public static class Article {
            private String title;
            private String description;
            private String content;
            private String url;
            private String image;
            private Source source;
            private LocalDateTime publishedAt;

            // Getters and Setters
            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
            public String getContent() { return content; }
            public void setContent(String content) { this.content = content; }
            public String getUrl() { return url; }
            public void setUrl(String url) { this.url = url; }
            public String getImage() { return image; }
            public void setImage(String image) { this.image = image; }
            public Source getSource() { return source; }
            public void setSource(Source source) { this.source = source; }
            public LocalDateTime getPublishedAt() { return publishedAt; }
            public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
        }

        public static class Source {
            private String name;
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
        }
    }
}

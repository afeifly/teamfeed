package com.selffeed.controller;

import com.selffeed.model.dao.ArticleDao;
import com.selffeed.model.dao.FeedDao;
import com.selffeed.model.dao.UserDao;
import com.selffeed.pojo.AURelative;
import com.selffeed.pojo.Article;
import com.selffeed.pojo.ArticleComment;
import com.selffeed.pojo.Feed;
import com.selffeed.util.FeedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@Slf4j
@ComponentScan("com.selffeed.model.dao")
public class ArticleController {

    @Autowired
    ArticleDao articleDao;
    @Autowired
    UserDao userDao;

    @PostMapping("/api/fetch_history_articles")
    public List<Article> getHistoryArticlesForUser(@RequestBody AURelative relative){

        int result = 0;
        int userId = relative.getU_id();
        List<Article> list = null;
        try {
            list = articleDao.getHistoryArticles(userId);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("Query article size = "+ list.size());
        return list;
    }

    @GetMapping("/api/article/{id}")
    public Article getArticle(@PathVariable(value = "id") int articleId){
        Article item = null;
        //TODO implement DAO
        try {
            item = articleDao.getArticle((int) articleId);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return item;
    }
    @PostMapping("/api/fetch_articles")
    public List<Article> getArticlesForUser(@RequestBody AURelative relative){

        int result = 0;
        int feedId = relative.getF_id();
        int userId = relative.getU_id();
        List<Article> list = null;
        try {
            list = articleDao.getArticles(userId,feedId);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        log.info("Query article size = "+ list.size());
        return list;
    }
    @PostMapping("/api/like_article")
    public int getLikeArticles(@RequestBody AURelative relative){

        int result = 0;
        int articleId = relative.getA_id();
        int userId = relative.getU_id();
        try {
            result = articleDao.likeArticle(articleId,userId);
            userDao.increaseAttack(userId,20);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return result;
    }
    @PostMapping("/api/comment_article")
    public int postCommentArticles(@RequestBody ArticleComment comment){
        int result = 0;
        try {
            result = articleDao.commentArticle(comment);
            userDao.increaseAttack(comment.getFk_u_id(),20);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return result;
    }
    @PostMapping("/api/comment_articles")
    public List<ArticleComment> getCommentArticles(@RequestBody ArticleComment relative){
        int articleId = relative.getFk_a_id();
        int uid = relative.getFk_u_id();
        List<ArticleComment> list = null;
        try {
            list = articleDao.getArticleComments(articleId);
            articleDao.readArticle(articleId,uid);
            userDao.consumeAttack(uid);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

}

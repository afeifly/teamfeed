package com.selffeed.model.dao;

import com.selffeed.pojo.Article;
import com.selffeed.pojo.ArticleComment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public interface ArticleDao {

    public int add(List<Article> articleList) throws SQLException;
    public void delete(int id)
            throws SQLException;
    public Article getArticle(int id)
            throws SQLException;

    public int readArticle(int articleId,int userId) throws SQLException;

    public int likeArticle(int articleId,int userId) throws SQLException;

    public int commentArticle(ArticleComment comment) throws SQLException;
    public List<Article> getArticles(int userId, int feedId) throws SQLException;
    public List<Article> getHistoryArticles(int userId) throws SQLException;

    public List<ArticleComment> getArticleComments(int articleId) throws SQLException;
}

package com.selffeed.model.dao;

import com.selffeed.pojo.Article;
import com.selffeed.pojo.ArticleComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class ArticleDaoImp implements ArticleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int add(List<Article> articleList) throws SQLException {
        log.debug("Add Article");
        String sql = "INSERT INTO articles (title, auther, url,content,ts,fk_f_id) " +
                "VALUES (:title, :auther, :url, :content, :ts , :f_id)";

        namedParameterJdbcTemplate.batchUpdate(sql,
                SqlParameterSourceUtils.createBatch(articleList));
        return 0;
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Article getArticle(int id) throws SQLException {
        return null;
    }

    @Override
    public int likeArticle(int articleId, int userId) throws SQLException {

        log.debug("Like Article");
        String sql = "INSERT INTO articles_like(fk_a_id,fk_u_id)  " +
                "VALUES (? , ?) on conflict (fk_a_id,fk_u_id) do nothing";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setInt(1, articleId);
            ps.setInt(2, userId);
            return ps;

        });
    }

    @Override
    public int readArticle(int articleId, int userId) throws SQLException {

        log.debug("Read Article");
        String sql = "INSERT INTO articles_users_relative (fk_a_id,fk_u_id) " +
                "VALUES (? , ?) on conflict (fk_a_id,fk_u_id) do nothing";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setInt(1, articleId);
            ps.setInt(2, userId);
            return ps;

        });
    }

    public int commentArticle(ArticleComment comment) throws SQLException {
        log.debug("Comment Article");
        String sql = "INSERT INTO article_comments(content,fk_a_id,fk_u_id,ts)  " +
                "VALUES (? , ?, ?,now())";
        return jdbcTemplate.update(connection -> {

            var ps = connection.prepareStatement(sql);
            ps.setString(1, comment.getContent());
            ps.setInt(2, comment.getFk_a_id());
            ps.setInt(3, comment.getFk_u_id());
            return ps;
        });
    }

    @Override
    public List<Article> getHistoryArticles(int userId) throws SQLException {
        List<Article> list = jdbcTemplate.query(
                "select a.id,a.title,a.auther,a.url,a.content,a.ts,count(l.fk_u_id) " +
                        "from articles a " +
                        "left join articles_users_relative r on a.id = r.fk_a_id " +
                        "left join articles_like l on a.id = l.fk_a_id " +
                        "where (r.fk_u_id = ?) " +
                        "group by a.id " +
                        "order by a.id desc limit 200",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setInt(1, userId);
                    }
                },
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return Article.builder()
                                .a_id(rs.getInt(1))
                                .title(rs.getString(2).trim())
                                .auther(rs.getString(3).trim())
                                .url(rs.getString(4).trim())
                                .content(rs.getString(5).trim())
                                .ts(rs.getTimestamp(6))
                                .likes(rs.getInt(7))
                                .build();
                    }
                });
        log.debug("Get article count = " + list.size());
        return list;
    }

    @Override
    public List<Article> getArticles(int userId, int feedId) throws SQLException {
        List<Article> list = jdbcTemplate.query(
                "select a.id,a.title,a.auther,a.url,a.content,a.ts,count(l.fk_u_id) " +
                        "from articles a " +
                        "left join articles_users_relative r on a.id = r.fk_a_id " +
                        "left join articles_like l on a.id = l.fk_a_id " +
                        "where (r.fk_u_id <> ? or r.fk_u_id is null) " +
                        "and a.fk_f_id = ? " +
                        "group by a.id " +
                        "order by a.id desc limit 20",
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setInt(1, userId);
                        preparedStatement.setInt(2, feedId);
                    }
                },
                new RowMapper<Article>() {
                    @Override
                    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return Article.builder()
                                .a_id(rs.getInt(1))
                                .title(rs.getString(2).trim())
                                .auther(rs.getString(3).trim())
                                .url(rs.getString(4).trim())
                                .content(rs.getString(5).trim())
                                .ts(rs.getTimestamp(6))
                                .likes(rs.getInt(7))
                                .build();
                    }
                });
        log.debug("Get article count = " + list.size());
        return list;
    }


    @Override
    public List<ArticleComment> getArticleComments(int articleId) throws SQLException {

        String sql = "select c.content,c.ts, u.icon_index,u.nickname " +
                "from article_comments c left join users u " +
                "on c.fk_u_id = u.id " +
                "where c.fk_a_id = ?";
        System.out.println(sql);
        System.out.println(articleId);
        List<ArticleComment> list = jdbcTemplate.query(
                sql,
                new PreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setInt(1, articleId);
                    }
                },
                new RowMapper<ArticleComment>() {
                    @Override
                    public ArticleComment mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return ArticleComment.builder()
                                .content(rs.getString(1).trim())
                                .ts(rs.getTimestamp(2))
                                .iconIndex(rs.getInt(3))
                                .nickname(rs.getString(4))
                                .build();
                    }
                });
        log.debug("Get article comments count = " + list.size());
        return list;
    }


}

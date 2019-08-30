package com.yifeng.springboot.sprintbootdemo.service.impl;

import com.yifeng.springboot.sprintbootdemo.dao.ArticleDao;
import com.yifeng.springboot.sprintbootdemo.entity.Article;
import com.yifeng.springboot.sprintbootdemo.service.ArticleService;
import com.yifeng.springboot.sprintbootdemo.utils.PageResult;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleDao articleDao;

    @Override
    public PageResult getArticlePage(PageUtil pageUtil) {
        List<Article> articleList = articleDao.findArticles(pageUtil);
        int total = articleDao.getTotalArticles(pageUtil);
        PageResult pageResult = new PageResult(articleList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Article queryObject(Integer id) {
        Article article = articleDao.getArticleById(id);
        if (article != null) {
            return article;
        }
        return null;
    }

    @Override
    public List<Article> queryList(Map<String, Object> map) {
        List<Article> articles = articleDao.findArticles(map);
        return articles;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return articleDao.getTotalArticles(map);
    }

    @Override
    public int save(Article article) {
        return articleDao.insertArticle(article);
    }

    @Override
    public int update(Article article) {
        article.setUpdateTime(new Date());
        return articleDao.updArticle(article);
    }

    @Override
    public int delete(Integer id) {
        return articleDao.delArticle(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return articleDao.deleteBatch(ids);
    }
}

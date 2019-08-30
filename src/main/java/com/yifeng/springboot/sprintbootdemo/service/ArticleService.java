package com.yifeng.springboot.sprintbootdemo.service;


import com.yifeng.springboot.sprintbootdemo.entity.Article;
import com.yifeng.springboot.sprintbootdemo.utils.PageResult;
import com.yifeng.springboot.sprintbootdemo.utils.PageUtil;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    PageResult getArticlePage(PageUtil pageUtil);

    Article queryObject(Integer id);

    List<Article> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    int save(Article article);

    int update(Article article);

    int delete(Integer id);

    int deleteBatch(Integer[] ids);
}

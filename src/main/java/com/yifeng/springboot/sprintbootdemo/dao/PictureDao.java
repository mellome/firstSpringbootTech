package com.yifeng.springboot.sprintbootdemo.dao;

import com.yifeng.springboot.sprintbootdemo.entity.Picture;

import java.util.List;
import java.util.Map;


public interface PictureDao {
    /**
     * 返回数据列表
     *
     * @param map
     * @return
     */
    List<Picture> findPictures(Map<String, Object> map);

    /**
     * 数据数目
     *
     * @param map
     * @return
     */
    int getTotalPictures(Map<String, Object> map);

    /**
     * 添加图片
     *
     * @param picture
     * @return
     */
    int insertPicture(Picture picture);

    /**
     * 修改图片
     *
     * @param picture
     * @return
     */
    int updPicture(Picture picture);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delPicture(Integer id);

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    Picture findPictureById(Integer id);

    /**
     * 批量删除
     *
     * @param id
     * @return
     */
    int deleteBatch(Object[] id);
}


package com.example.chatuser.service.impl;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Tags;
import com.example.chatuser.mapper.TagsMapper;
import com.example.chatuser.service.ITagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-31
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    /**
     * 获取标签
     *
     * @return {@link Object}
     */

    public String getTags() {
        return JSON.toJSONString(list());
    }

    /**
     * 按 ID 设置标签
     *
     * @param tagId 标记 ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setTagsById(Integer tagId) {
        UpdateWrapper<Tags> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("count=count+1").eq("tagId", tagId);
        return update(updateWrapper);
    }


    public String getTagsById(Integer tagId) {
        QueryWrapper<Tags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tagId", tagId);
        return JSON.toJSONString(getOne(queryWrapper));
    }


}

package com.example.chatuser.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chatuser.entity.Information;
import com.example.chatuser.entity.Privatemessage;
import com.example.chatuser.mapper.PrivatemessageMapper;
import com.example.chatuser.service.IPrivatemessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-10-22
 */
@Service
public class PrivatemessageServiceImpl extends ServiceImpl<PrivatemessageMapper, Privatemessage> implements IPrivatemessageService {
    /**
     * 添加私信
     *
     * @param privatemessage 私信
     * @return {@link Object}
     */
    @Transactional(rollbackFor = Exception.class)
    public Object addPrivateMessage(Privatemessage privatemessage) {
        privatemessage.setSendTime(LocalDateTime.now());
        boolean save = this.save(privatemessage);
        return save ? Information.toString("successful", "添加成功") : Information.toString("error", "添加失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public Object getPrivateMessageById(IPage<Privatemessage> page, Privatemessage privatemessage) {
        QueryWrapper<Privatemessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(
                wrapper -> wrapper.eq("senderId", privatemessage.getSenderId())
                        .eq("receiverId", privatemessage.getReceiverId())
        ).or(
                wrapper -> wrapper.eq("senderId", privatemessage.getReceiverId())
                        .eq("receiverId", privatemessage.getSenderId())
        ).orderByDesc("sendTime");
        IPage<Privatemessage> iPage = this.page(page, queryWrapper);
        if (iPage == null) {
            return null;
        }

        iPage.getRecords().forEach(item -> {
            if (item.getSenderId().equals(privatemessage.getSenderId())) {
                item.setSenderId(0);
            }
        });
        iPage.getRecords().sort(new TimeComparator());
        return JSON.toJSONString(iPage);
    }

    static class TimeComparator implements Comparator<Privatemessage> {
        @Override
        public int compare(Privatemessage o1, Privatemessage o2) {
            return o1.getSendTime().compareTo(o2.getSendTime());
        }
    }
}

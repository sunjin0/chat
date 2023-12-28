package com.example.chatadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 孙进
 * @since 2023-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("groupchatmessage")
@ApiModel(value = "Groupchatmessage对象")
public class Groupchatmessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "信息ID")
    @TableId(value = "messageId", type = IdType.AUTO)
    private Integer messageId;

    @ApiModelProperty(value = "群聊ID")
    private Integer groupChatId;

    @ApiModelProperty(value = "发送人ID")
    private Integer senderId;

    @ApiModelProperty(value = "信息")
    private String content;

    @ApiModelProperty(value = "时间")
    private LocalDateTime sendTime;


}

package com.example.chatadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("privatemessage")
@ApiModel(value = "Privatemessage对象", description = "")
public class Privatemessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "信息ID")
    @TableId(value = "messageId", type = IdType.AUTO)
    private Integer messageId;

    @TableField("senderId")
    @ApiModelProperty(value = "发送人ID")
    private Integer senderId;

    @TableField("receiverId")
    @ApiModelProperty(value = "收件人ID")
    private Integer receiverId;

    @TableField("content")
    @ApiModelProperty(value = "内容")
    private String content;

    @TableField("sendTime")
    @ApiModelProperty(value = "时间")
    private LocalDateTime sendTime;


}

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
@TableName("groupchatmember")
@ApiModel(value = "Groupchatmember对象", description = "")
public class Groupchatmember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群聊成员ID")
    @TableId(value = "memberId", type = IdType.AUTO)
    private Integer memberId;

    @TableField("groupChatId")
    @ApiModelProperty(value = "群聊ID")
    private Integer groupChatId;

    @TableField("userId")
    @ApiModelProperty(value = "用户ID")
    private Integer userId;


}

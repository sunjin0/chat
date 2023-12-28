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
 * @since 2023-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("groupchat")
@ApiModel(value = "Groupchat对象", description = "")
public class Groupchat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群聊ID")
    @TableId(value = "groupChatId", type = IdType.AUTO)
    private Integer groupChatId;

    @TableField("groupName")
    @ApiModelProperty(value = "群聊名称")
    private String groupName;

    @TableField("creatorId")
    @ApiModelProperty(value = "创建人ID")
    private Integer creatorId;

    @TableField("createTime")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}

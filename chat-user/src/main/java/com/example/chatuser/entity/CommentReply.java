package com.example.chatuser.entity;

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
 * 评论回复表
 * </p>
 *
 * @author 孙进
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment_reply")
@ApiModel(value = "CommentReply对象", description = "评论回复表")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "所属评论ID")
    @TableField("commentId")
    private Integer commentId;

    @ApiModelProperty(value = "回复人ID")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty(value = "被回复人ID")
    @TableField("toId")
    private Integer toId;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @TableField("createdTime")
    private LocalDateTime createdTime;


}

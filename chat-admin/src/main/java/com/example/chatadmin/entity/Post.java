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
 * 发布
 * </p>
 *
 * @author 孙进
 * @since 2023-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("post")
@ApiModel(value = "Post对象", description = "")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    @TableId(value = "postId", type = IdType.AUTO)
    private Integer postId;

    @ApiModelProperty(value = "发布人ID")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "发布时间")
    @TableField("publishTime")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "帖子状态")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "帖子图片URL")
    @TableField("img")
    private String img;

    @ApiModelProperty(value = "发帖人选择的地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "标签")
    @TableField("tagId")
    private Integer tagId;

    @TableField(exist = false)
    private String tag;


}

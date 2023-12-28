package com.example.chatadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
 * @since 2023-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("postLike")
@ApiModel(value = "Like对象")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "likeId", type = IdType.AUTO)
    private Integer likeId;
    @TableField("postId")
    private Integer postId;
    @TableField("userId")
    private Integer userId;
    @TableField("likeTime")
    private LocalDateTime likeTime;


}

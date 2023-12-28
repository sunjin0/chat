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
 *
 * </p>
 *
 * @author 孙进
 * @since 2023-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("follow")
@ApiModel(value = "Follow对象", description = "")
public class
Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关注ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty(value = "被关注者的ID")
    @TableField("followingId")
    private Integer followingId;

    @ApiModelProperty(value = "关注时间")
    @TableField("createTime")
    private LocalDateTime createTime;


}

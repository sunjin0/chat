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
 * @since 2023-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("browsinghistory")
@ApiModel(value = "Browsinghistory对象", description = "")
public class Browsinghistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "浏览历史ID")
    @TableId(value = "historyId", type = IdType.AUTO)
    private Integer historyId;

    @ApiModelProperty(value = "用户ID")
    @TableField("userId")
    private Integer userId;

    @TableField("postId")
    @ApiModelProperty(value = "帖子ID")
    private Integer postId;

    @TableField("browsingTime")
    @ApiModelProperty(value = "浏览时间")
    private LocalDateTime browsingTime;


}

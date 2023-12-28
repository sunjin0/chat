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
 * @since 2023-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("userrelationship")
@ApiModel(value = "Userrelationship对象")
public class Userrelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关系ID")
    @TableId(value = "relationShipId", type = IdType.AUTO)
    private Integer relation_shipId;

    @ApiModelProperty(value = "自己ID")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty(value = "好友ID")
    @TableField("toId")
    private Integer toId;

    @ApiModelProperty(value = "关系类型")
    @TableField("relationShipType")
    private String relationShipType;

    @ApiModelProperty(value = "关系状态")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value = "修改时间")
    @TableField("time")
    private LocalDateTime time;

    @ApiModelProperty(value = "用户给我的备注")
    @TableField("notes")
    private String notes;

    @ApiModelProperty(value = "用户备注")
    @TableField("toNotes")
    private String toNotes;


}

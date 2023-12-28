package com.example.chatuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("recommendationrecord")
@ApiModel(value = "Recommendationrecord对象", description = "")
public class Recommendationrecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "RecordID", type = IdType.AUTO)
    private Integer RecordID;

    private Integer UserID;

    private Integer PostID;

    private LocalDateTime RecommendationTime;


}

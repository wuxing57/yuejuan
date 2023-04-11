package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 知识点
 * </p>
 *
 * @author 
 * @since 2023-03-24
 */
@Getter
@Setter
@ApiModel(value = "Knowledge对象", description = "知识点")
public class Knowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("知识点名称")
    private String name;

    @ApiModelProperty("课程id")
    private Integer courseId;

    @ApiModelProperty("父级id")
    private Integer parentId;

    @ApiModelProperty("创建时间")
    private String createTime;

}

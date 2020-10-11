package com.jfly.api.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    /**删除标志 */
    @TableLogic
    @JsonIgnore
    private Integer deleted;

    /** 创建者 */
    @JsonIgnore
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    private Long createdBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_time",fill = FieldFill.INSERT)
    private Date createdTime;

    /** 更新者 */
    @JsonIgnore
    @TableField(value = "updated_by",fill = FieldFill.UPDATE)
    private Long updatedBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updated_time",fill = FieldFill.UPDATE)
    private Date updatedTime;

    /** 备注 */
    @JsonIgnore
    private String remark;
}

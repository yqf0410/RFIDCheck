package com.project.rfidCheck.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author frank.yang
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TaskLog extends Model<TaskLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 
名称
     */
    private String name;

    /**
     * 状态（0失败1成功）
     */
    private Integer flag;

    /**
     * 数据
     */
    private String data;

    /**
     * 结果
     */
    private String message;

    /**
     * 请求时间
     */
    private Date requestDate;

    /**
     * 返回时间
     */
    private Date responseDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

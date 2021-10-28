package com.project.rfidCheck.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TaskBind extends Model<TaskBind> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 任务表主键
     */
    private String taskId;

    /**
     * RFID主键
     */
    private String rfidUid;

    /**
     * RFID数据
     */
    private String rfidData;

    /**
     * 过点状态
     */
    private Integer checkState;

    /**
     * 过点异常原因
     */
    private String checkMessage;

    /**
     * 过点时间
     */
    private Date checkDate;

    /**
     * 创建时间
     */
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

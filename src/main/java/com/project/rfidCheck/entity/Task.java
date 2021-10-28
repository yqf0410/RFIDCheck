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
public class Task extends Model<Task> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 绑定类型（0手工1扫码）
     */
    private Integer bindType;


    /**
     * 二维码数据
     */
    private String barCode;

    /**
     * 机床编号
     */
    private String equipCode;

    /**
     * 零件批次号
     */
    private String produLotCode;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 总数量
     */
    private Integer qty;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

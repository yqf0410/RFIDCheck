package com.project.rfidCheck.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

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
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProduCheck extends Model<ProduCheck> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 绑定主键
     */
    private String produBindId;

    /**
     * 校验状态（0未校验1已校验）
     */
    private Integer checkState;

    /**
     * 效验结果（0不通过1合格）
     */
    private Integer checkResult;

    /**
     * 校验时间
     */
    private Date checkDate;

    /**
     * 读取零件编码
     */
    private String checkRfidData;

    /**
     * 读取RFIDUID
     */
    private String checkRfidUid;

    /**
     * 校验工位
     */
    private String checkWorkCell;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

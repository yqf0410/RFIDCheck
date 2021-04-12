package com.project.rfidCheck.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;
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
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProduBind extends Model<ProduBind> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 上料顺序
     */
    private String seq;

    /**
     * 单号NO
     */
    private String noCode;

    /**
     * 零件代号
     */
    private String produCode;

    /**
     * 工位编码
     */
    private String workCellCode;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 二维码数据
     */
    private String barCode;

    /**
     * RFID主键
     */
    private String rfidUid;

    /**
     * RFID数据
     */
    private String rfidData;

    /**
     * 绑定状态（0未绑定1已绑定）
     */
    private Integer bindState;

    /**
     * 绑定类型（0手工1扫码）
     */
    private Integer bindType;

    /**
     * 绑定时间
     */
    private Date bindDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

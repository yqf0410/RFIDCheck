package com.project.rfidCheck.service;

import com.project.rfidCheck.entity.ProduBind;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
public interface ProduBindService extends IService<ProduBind> {

    String selectProdu(Map<String, String> map);

    String saveProdu(Map<String, String> map);

    String daleteProdu(Map<String, String> map);

    String importProdu(List<Map<String, String>> list);

    String selectUnBindProdu();

    String selectBindRecord(Map<String, String> map);

    String saveBindProdu(Map<String, String> map);

}

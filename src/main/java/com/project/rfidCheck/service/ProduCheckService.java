package com.project.rfidCheck.service;

import com.project.rfidCheck.entity.ProduCheck;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
public interface ProduCheckService extends IService<ProduCheck> {

    String selectCheckRecord(Map<String, String> map);

}

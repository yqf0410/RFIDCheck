package com.project.rfidCheck.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.rfidCheck.entity.ProduBind;
import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.mapper.ProduBindMapper;
import com.project.rfidCheck.mapper.ProduCheckMapper;
import com.project.rfidCheck.mapper.ProduCheckMapper;
import com.project.rfidCheck.service.ProduCheckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@Service
public class ProduCheckServiceImpl extends ServiceImpl<ProduCheckMapper, ProduCheck> implements ProduCheckService {


    @Autowired
    private ProduCheckMapper produCheckMapper;
    @Autowired
    private ProduBindMapper produBindMapper;


    @Override
    public String selectCheckRecord(Map<String, String> map) {
        QueryWrapper<ProduCheck> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(map.get("noCode"))){
            queryWrapper.eq("no_code",map.get("noCode"));
        }
        if(StringUtils.isNotEmpty(map.get("produCode"))){
            queryWrapper.eq("produ_code",map.get("produCode"));
        }
        if(StringUtils.isNotEmpty(map.get("bindState"))){
            queryWrapper.eq("bind_state",map.get("bindState"));
        }
        if(StringUtils.isNotEmpty(map.get("checkDateS"))){
            queryWrapper.gt("check_date", DateUtil.parse(map.get("checkDateS")));
        }
        if(StringUtils.isNotEmpty(map.get("checkDateE"))){
            queryWrapper.le("check_date",DateUtil.parse(map.get("checkDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true,sortStr.subSequence(0,1).equals("+"),sortStr.substring(1,sortStr.length()));
        IPage<ProduCheck> result = produCheckMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString())),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for(ProduCheck pc:result.getRecords()){
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id",pc.getId());
            ProduBind bind = produBindMapper.selectById(pc.getProduBindId());
            rowData.put("seq",pc.getProduBindId());
            rowData.put("checkDate",DateUtil.format(pc.getCheckDate(),"yyyy/MM/dd hh:mm:ss"));
            rowData.put("checkWorkCell",pc.getCheckWorkCell());
            rowData.put("checkRfidUid",pc.getCheckRfidUid());
            rowData.put("checkState",pc.getCheckState().toString());
            rowData.put("checkResult",pc.getCheckResult().toString());
            ProduBind pb = produBindMapper.selectById(pc.getProduBindId());
            if(pb != null){
                rowData.put("seq",pb.getSeq());
                rowData.put("bindType",pb.getBindType().toString());
                rowData.put("produCode",pb.getProduCode());
                rowData.put("rfidUid",pb.getRfidUid());
                rowData.put("bindDate",DateUtil.format(pb.getBindDate(),"yyyy/MM/dd hh:mm:ss"));
            }
            gridData.add(rowData);
        }
        returnMap.put("items",gridData);
        returnMap.put("total",result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }
}

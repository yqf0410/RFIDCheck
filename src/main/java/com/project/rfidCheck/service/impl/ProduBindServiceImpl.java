package com.project.rfidCheck.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.xdevapi.JsonArray;
import com.project.rfidCheck.entity.ProduBind;
import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.mapper.ProduBindMapper;
import com.project.rfidCheck.mapper.ProduCheckMapper;
import com.project.rfidCheck.service.ProduBindService;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author frank.yang
 * @since 2021-03-31
 */
@Service
public class ProduBindServiceImpl extends ServiceImpl<ProduBindMapper, ProduBind> implements ProduBindService {


    @Autowired
    private ProduBindMapper produBindMapper;
    @Autowired
    private ProduCheckMapper produCheckMapper;

    @Override
    public String selectProdu(Map<String, String> map) {
        QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(map.get("noCode"))) {
            queryWrapper.eq("no_code", map.get("noCode"));
        }
        if (StringUtils.isNotEmpty(map.get("produCode"))) {
            queryWrapper.eq("produ_code", map.get("produCode"));
        }
        if (StringUtils.isNotEmpty(map.get("bindState"))) {
            queryWrapper.eq("bind_state", map.get("bindState"));
        }
        if (StringUtils.isNotEmpty(map.get("createDateS"))) {
            queryWrapper.gt("create_date", DateUtil.parse(map.get("createDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("createDateE"))) {
            queryWrapper.le("create_date", DateUtil.parse(map.get("createDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true, sortStr.subSequence(0, 1).equals("+"), sortStr.substring(1, sortStr.length()));
        IPage<ProduBind> result = produBindMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString())),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for (ProduBind pb : result.getRecords()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", pb.getId());
            rowData.put("seq", pb.getSeq());
            rowData.put("noCode", pb.getNoCode());
            rowData.put("produCode", pb.getProduCode());
            rowData.put("barCode", pb.getBarCode());
            rowData.put("createDate", DateUtil.format(pb.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
            rowData.put("bindState", pb.getBindState().toString());
            gridData.add(rowData);
        }
        returnMap.put("items", gridData);
        returnMap.put("total", result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String saveProdu(Map<String, String> map) {
        ProduBind produBind = new ProduBind();
        produBind.setSeq(map.get("seq").toString());
        produBind.setNoCode(map.get("noCode").toString());
        produBind.setProduCode(map.get("produCode").toString());
        produBind.setCreateDate(new Date());
        if (map.containsKey("id") && map.get("id") != null && map.get("id").toString() != "") {
            produBind.setId(map.get("id"));
            produBindMapper.updateById(produBind);
        } else {
            //校验
            QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("no_code",map.get("noCode"));
            ProduBind hisProduBind = produBindMapper.selectOne(queryWrapper);
            if(hisProduBind != null){
                return "单号已存在！";
            }
            produBind.setId(IdUtil.fastUUID().replace("-", ""));
            produBindMapper.insert(produBind);
        }
        return "success";
    }

    @Override
    public String daleteProdu(Map<String, String> map) {
        produBindMapper.deleteById(map.get("id").toString());
        return "删除成功";
    }

    @Override
    public String importProdu(List<Map<String, String>> list) {
        for (Map<String, String> map : list) {
            QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("no_code", map.get("单号").toString());
            ProduBind produBind = produBindMapper.selectOne(queryWrapper);
            if (produBind == null) {
                produBind = new ProduBind();
                produBind.setId(IdUtil.fastUUID().replace("-", ""));
                produBind.setSeq(map.get("上料顺序").toString());
                produBind.setNoCode(map.get("单号").toString());
                produBind.setProduCode(map.get("零件代号").toString());
                produBind.setCreateDate(new Date());
                produBindMapper.insert(produBind);
            } else {
                produBind.setSeq(map.get("上料顺序").toString());
                produBind.setNoCode(map.get("单号").toString());
                produBind.setProduCode(map.get("零件代号").toString());
                produBindMapper.updateById(produBind);
            }
        }
        return "保存成功！";
    }


    @Override
    public String selectUnBindProdu() {
        QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bind_state", 0);
        queryWrapper.orderBy(true, true, "create_date");
        List<ProduBind> list = produBindMapper.selectList(queryWrapper);
        List<Map<String, String>> returnList = new ArrayList<>();
        for (ProduBind pb : list) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", pb.getId());
            rowData.put("seq", pb.getSeq());
            rowData.put("noCode", pb.getNoCode());
            rowData.put("produCode", pb.getProduCode());
            rowData.put("barCode", pb.getBarCode());
            rowData.put("createDate", DateUtil.format(pb.getCreateDate(), "yyyy/MM/dd HH:mm:ss"));
            rowData.put("bindState", pb.getBindState().toString());
            returnList.add(rowData);
        }
        return JSONUtils.toJSONString(returnList);
    }

    @Override
    public String selectBindRecord(Map<String, String> map) {
        QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bind_state", 1);
        if (StringUtils.isNotEmpty(map.get("noCode"))) {
            queryWrapper.eq("no_code", map.get("noCode"));
        }
        if (StringUtils.isNotEmpty(map.get("produCode"))) {
            queryWrapper.eq("produ_code", map.get("produCode"));
        }
        if (StringUtils.isNotEmpty(map.get("bindType"))) {
            queryWrapper.eq("bind_type", map.get("bindType"));
        }
        if (StringUtils.isNotEmpty(map.get("bindDateS"))) {
            queryWrapper.gt("bind_date", DateUtil.parse(map.get("bindDateS")));
        }
        if (StringUtils.isNotEmpty(map.get("bindDateE"))) {
            queryWrapper.le("bind_date", DateUtil.parse(map.get("bindDateE")));
        }
        String sortStr = map.get("sort");
        queryWrapper.orderBy(true, sortStr.subSequence(0, 1).equals("+"), sortStr.substring(1, sortStr.length()));
        IPage<ProduBind> result = produBindMapper.selectPage(
                new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString())),
                queryWrapper
        );
        Map returnMap = new HashMap();
        List<Map<String, String>> gridData = new ArrayList<>();
        for (ProduBind pb : result.getRecords()) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("id", pb.getId());
            rowData.put("seq", pb.getSeq());
            rowData.put("bindType", pb.getBindType().toString());
            rowData.put("noCode", pb.getNoCode());
            rowData.put("workCellCode", pb.getWorkCellCode());
            rowData.put("produCode", pb.getProduCode());
            rowData.put("barCode", pb.getBarCode());
            rowData.put("rfidUid", pb.getRfidUid());
            rowData.put("bindDate", DateUtil.format(pb.getBindDate(), "yyyy/MM/dd HH:mm:ss"));
            List<ProduCheck> produChecks = produCheckMapper.selectList(new QueryWrapper<ProduCheck>().eq("produ_bind_id",pb.getId()));
            if(produChecks.size() > 0){
                rowData.put("checkState", produChecks.get(0).getCheckState().toString());
            }else{
                rowData.put("checkState", "0");
            }
            if (StringUtils.isNotEmpty(map.get("checkState")) && !map.get("checkState").equals(rowData.get("checkState"))) {
                continue;
            }
            gridData.add(rowData);
        }
        returnMap.put("items", gridData);
        returnMap.put("total", result.getTotal());
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String saveBindProdu(Map<String, String> map) {
        Map<String, String> returnMap = new HashMap<>();
        QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_cell_code",map.get("workCellCode"));
        List<ProduBind> produBinds = produBindMapper.selectList(queryWrapper);
        for(ProduBind produBind:produBinds){
            QueryWrapper<ProduCheck> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("produ_bind_id",produBind.getId());
            ProduCheck produCheck = produCheckMapper.selectOne(queryWrapper1);
            if(produCheck == null || produCheck.getCheckState().equals(0)){
                returnMap.put("flag","false");
                returnMap.put("data","此工位已绑定且未校验！");
                return JSONUtils.toJSONString(returnMap);
            }
        }
        ProduBind produBind = new ProduBind();
        if(StringUtils.isNotEmpty(map.get("selectId"))){
            produBind = produBindMapper.selectById(map.get("selectId"));
            produBind.setWorkCellCode(map.get("workCellCode"));
            produBind.setRfidUid(map.get("rfidUid").replace(":",""));
            produBind.setRfidData(map.get("rfidData"));
            produBind.setBindState(1);
            produBind.setBindDate(new Date());
            produBind.setBindType(Integer.parseInt(map.get("bindType")));
            produBindMapper.updateById(produBind);
        }else{
            produBind.setId(IdUtil.fastUUID().replace("-", ""));
            produBind.setNoCode(map.get("noCode"));
            produBind.setBarCode(map.get("barCode"));
            produBind.setProduCode(map.get("produCode"));
            produBind.setWorkCellCode(map.get("workCellCode"));
            produBind.setRfidUid(map.get("rfidUid").replace(":",""));
            produBind.setRfidData(map.get("rfidData"));
            produBind.setBindState(1);
            produBind.setBindDate(new Date());
            produBind.setBindType(Integer.parseInt(map.get("bindType")));
            produBind.setCreateDate(new Date());
            produBindMapper.insert(produBind);
        }

        returnMap.put("flag","true");
        returnMap.put("data","绑定成功！");
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String checkBindRfid(Map<String, String> map) {
        QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bar_code",map.get("barCode"));
        ProduBind produBind = produBindMapper.selectOne(queryWrapper);
        String result;
        if(produBind == null){
            result = "0";
        }else{
            result = produBind.getBindState().toString();
        }
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("data",result);
        return JSONUtils.toJSONString(returnMap);
    }

    @Override
    public String resetBind(Map<String, String> map) {
        ProduBind produBind = produBindMapper.selectById(map.get("id").toString());
        produBind.setBindDate(null);
        produBind.setBindType(null);
        produBind.setBindState(0);;
        produBind.setWorkCellCode(null);
        produBindMapper.updateById(produBind);
        return "重置成功";
    }

}

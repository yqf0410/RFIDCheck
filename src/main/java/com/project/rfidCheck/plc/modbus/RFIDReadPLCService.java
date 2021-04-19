package com.project.rfidCheck.plc.modbus;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.util.StringUtils;
import com.project.rfidCheck.entity.ProduBind;
import com.project.rfidCheck.entity.ProduCheck;
import com.project.rfidCheck.mapper.ProduBindMapper;
import com.project.rfidCheck.mapper.ProduCheckMapper;
import com.project.rfidCheck.service.ProduBindService;
import com.project.rfidCheck.service.ProduCheckService;
import com.project.rfidCheck.service.TaskLogService;
import com.serotonin.util.queue.ByteQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class RFIDReadPLCService {

    @Autowired
    private ProduBindService produBindService;

    @Autowired
    private ProduCheckService produCheckService;

    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private TCPReader tcpReader;

    @Autowired
    private TCPWriter tcpWriter;

    @Autowired
    private ProduBindMapper produBindMapper;

    //3.添加定时任务
    @Scheduled(fixedRate = 1000)
    private void configureTasks() {
        Integer flag = 2;
        String message = "校验通过";
        String returnResult = "OK";
        String workCell = "";
        Date sDate = new Date();
        try {
            System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
            //读取高电平
            //[2,3,2,0,0]
            ByteQueue flagByte = tcpReader.modbusReadWithTCP(CommandConstant.CHECK_FLAG, 1);
            if (flagByte.peek(4) == 1) {
                //读取RFID信息校验
                //[2,3,2,0,11]
                ByteQueue cellByte = tcpReader.modbusReadWithTCP(CommandConstant.CHECK_WORK_CELL, 1);
                //[2,3,28,0,1,0,2,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                ByteQueue uidByte = tcpReader.modbusReadWithTCP(CommandConstant.CHECK_UID, 20);
                //[2,3,c8,0,1,0,2,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
                ByteQueue rfidByte = tcpReader.modbusReadWithTCP(CommandConstant.CHECK_RFID, 100);

                workCell = String.valueOf(cellByte.peek(3, 2)[1]);
                String uid = hexToString(uidByte.peek(3, 32));
                String rfid = toString(rfidByte.peek(3, 200));

                QueryWrapper<ProduBind> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("work_cell_code", workCell);
                queryWrapper.orderBy(true, true, "create_date");
                List<ProduBind> list = produBindMapper.selectList(queryWrapper);
                if (list.size() == 0) {
                    throw new Exception("工位【" + workCell + "未绑定！");
                }
                ProduBind produBind = list.get(0);
                ProduCheck produCheck = new ProduCheck();
                produCheck.setId(IdUtil.fastUUID().replace("-", ""));
                produCheck.setCheckDate(new Date());
                produCheck.setCheckState(1);
                produCheck.setProduBindId(produBind.getId());
                int checkResult = 1;
                flag = 1;
                if (produBind == null) {
                    checkResult = 0;
                    returnResult = "NG";
                    message = "绑定记录不存在";
                    flag = 0;
                }
                if(!produBind.getRfidUid().equals(uid)){
                    checkResult = 0;
                    returnResult = "NG";
                    message = "RFID UID不匹配";
                    flag = 0;
                }
                if(!produBind.getRfidData().equals(rfid)){
                    checkResult = 0;
                    returnResult = "NG";
                    message = "RFID 数据不匹配";
                    flag = 0;
                }
                produCheck.setCheckRfidData(rfid);
                produCheck.setCheckRfidUid(uid);
                produCheck.setCheckWorkCell(workCell);
                produCheck.setCheckResult(checkResult);
                produCheckService.save(produCheck);
                //写入结果到PLC
                byte[] cc = returnResult.getBytes();
                int v1 = cc[0] >= 0 ? ((int) (cc[0])) : cc[0] + 256;
                int v2 = cc[1] >= 0 ? ((int) (cc[1])) : cc[1] + 256;
                tcpWriter.modbusWriteWithTCP(CommandConstant.CHECK_RESULT, new short[]{(short) (v1 * 256 + v2)});
            }

        } catch (Exception e) {
            message = e.getMessage();
        } finally {
            if (flag != 2) {
                taskLogService.saveLog("校验RFID信息", flag, workCell, message, sDate);
            }
        }
    }

    public static String toString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if (!"0".equals(String.valueOf(byteArray[i]))) {
                if (String.valueOf(byteArray[i]).length() == 1) {
                    hexString.append(String.valueOf(byteArray[i]));
                } else {
                    hexString.append(StringUtils.toString(new byte[]{byteArray[i]}));
                }
            }
        }
        return hexString.toString();
    }

    public static String hexToString(byte[] byteArray) {
        char[] chars = HexUtil.encodeHex(byteArray);
        final StringBuilder hexString = new StringBuilder();
        char[] nullchar = {0, 0, 0, 0};
        //23,67,89
        for (int i = 0; i < byteArray.length; i++) {
            if (i % 4 == 2 || i % 4 == 3) {
                hexString.append(chars[i]);
            }
        }
        return hexString.toString().toUpperCase();
    }
}

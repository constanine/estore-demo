package com.bokesoft.controller;



import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.MIssionMessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/*
 * @author:gushiming
 * @date:2019/12/30
 * @description:使用说明
 * */
@Controller
public class IndicateinfoController {

    @RequestMapping("/indicateinfo")
    public String indicateinfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userCode = (String) session.getAttribute(SerachFileController.USERCODE);
        String isauthorizer = (String) session.getAttribute(SerachFileController.ISAUTHORIZER);
        List<MessageBean> messages = new ArrayList<MessageBean>();
        //判断是否为审批人员如果是审批员，则加入list中
        if(isauthorizer.equals("true")){
            messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        }
        request.setAttribute(SerachFileController.MESSAGES, messages);
        //内网ip
        String inDiskIp = EnvUtils.getInDiskIp();
        //外网ip
        String outDiskIp = EnvUtils.getOutDiskIp();
        String o2ISourceDir = EnvUtils.getO2ISourceDir(userCode); // 外网 to-内网
        String i2OSourceDir = EnvUtils.getI2OSourceDir(userCode); //内网  to-外网
        String i2ODestDir = EnvUtils.getI2ODestDir(userCode); //外网 from-内网
        String o2IDestDir = EnvUtils.getO2IDestDir(userCode); //内网 from-外网

        request.setAttribute("o2ISourceDir", outDiskIp+"/"+o2ISourceDir); //外网 to-内网
        request.setAttribute("i2OSourceDir", inDiskIp+"/"+i2OSourceDir);  //内网 to-外网
        request.setAttribute("i2ODestDir", outDiskIp+"/"+i2ODestDir);     //外网  from-内网
        request.setAttribute("o2IDestDir", inDiskIp+"/"+o2IDestDir);      //内网  from-外网

        return "indicateinfo";
    }
}

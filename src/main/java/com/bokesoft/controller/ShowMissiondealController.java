package com.bokesoft.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ShowMissiondealService;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.ImportMissionBean;
import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2019/12/31
 * @description:进入日志详细
 * */
@Controller
public class ShowMissiondealController {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ShowMissiondealService missiondealService;

    @RequestMapping("/showmissiondeal")
    public String showMissiondeal(HttpServletRequest request) {
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);
        String showwhat = request.getParameter("show");
        String strnum = request.getParameter("startnum");
        int startnum = 0;
        String limnum = request.getParameter("pagesize");
        int limitnum = 25;
        int pages = 1;

        if (strnum != null) {
            startnum = Integer.valueOf(strnum);
        }
        if (limnum != null) {
            limitnum = Integer.valueOf(limnum);
        }
        logger.info(">>>>>>>>>>startnum:" + startnum + " || >>>>>>>>>>limitnum:" + limitnum);
        request.setAttribute("showwhat", showwhat);
        List<Map<String, Object>> pageslist = new ArrayList<Map<String, Object>>();

        List<MessageBean> messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute(SerachFileController.MESSAGES, messages);

        // 收索导入任务，导出任务集合
        if ("Import".equals(showwhat)) {

            List<ImportMissionBean> immissions = missiondealService.findIm(userCode, startnum, limitnum);

            pageslist = missiondealService.findImPage(userCode);

            pages = Integer.valueOf(pageslist.get(0).get("pages").toString());

            request.setAttribute("immissions", immissions);

        } else if ("Export".equals(showwhat)) {
            List<ExportMissionBean> exmissions = missiondealService.findEx(userCode, startnum, limitnum);

            pageslist = missiondealService.finExPage(userCode);

            pages = Integer.valueOf(pageslist.get(0).get("pages").toString());
            request.setAttribute("exmissions", exmissions);

        }
        logger.info(">>>>>>>>>>limitnum:" + limitnum);
        logger.info("pages:" + pages + ";pagesize:" + limitnum);
        request.setAttribute("pages", pages);
        request.setAttribute("pagesize", limitnum);
        request.setAttribute("startnum", startnum + 1);
        return "showMission";
    }
}

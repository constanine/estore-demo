package com.bokesoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bokesoft.utils.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.service.ToAuthorizePageService;
import com.bokesoft.struc.ExportMissionBean;

/*
 * @author:gushiming
 * @date:2019/12/30
 * @description:审批
 * */
@Controller
public class ToAuthorizePageController {

    @Autowired
    private ToAuthorizePageService authorizePageService;

    @RequestMapping("/toauthorizepage")
    public String toAuthorizePage(HttpServletRequest request){
        
        //判断是否是审批人员
        if(EnvUtils.isAuthorizer(request)){
            List<ExportMissionBean> missions = authorizePageService.find("wait_auth","wait_auth");
            request.setAttribute(SerachFileController.MISSIONS,missions);
            return "authorizedPage";
        }else {
            request.setAttribute("noright","noright");
            return "redirect:/authority";
        }
    }
}

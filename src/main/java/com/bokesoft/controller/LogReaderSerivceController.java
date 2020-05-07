package com.bokesoft.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2020/1/10
 * @description:日志阅读
 * */
@Controller
public class LogReaderSerivceController {

    @RequestMapping("/logread")
    public String logReaderService(HttpServletRequest request) throws UnsupportedEncodingException {

        String filename=new String(request.getParameter(SerachFileController.FILENAME).getBytes("ISO-8859-1"),"utf-8");
        List<MessageBean> messages = new ArrayList<MessageBean>();
        String loadtype = request.getParameter("type");
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);
  
        String showtitle ="";
        BufferedReader in =null;
        String content="";

        //根据type输出文件类型名
        if("runlog".equals(loadtype)){
            showtitle="运行日志";
        }else if("filelist".equals(loadtype)){
            showtitle="文件列表";
        }else if("runlog2".equals(loadtype)){
            showtitle="第二步的运行日志";
        }

        File file=new File(filename);
        //读取日志内容
        try {
            if("filelist".equals(loadtype)){
                in = new BufferedReader( new InputStreamReader( new FileInputStream(file),"utf-8"));
            }else{
                in = new BufferedReader( new InputStreamReader( new FileInputStream(file)));
            }
            String s=new String();
            while((s=in.readLine())!=null){
                content +=s;
                content +="???";
            }

            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute("typetitle", showtitle);
        request.setAttribute("content", content);
        request.setAttribute(SerachFileController.MESSAGES, messages);
        return "logContent";
    }

}



package com.bokesoft.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.NetEnvUtils;


/*
 * 文件上传（跳转到上传页面）/文件下载
 * */
@RestController
public class FileController {

    //文件上传
    @RequestMapping("/fileUpload/{modle}")
    public ModelAndView doUploadFile(@RequestParam("fileName") MultipartFile file, HttpServletRequest request, @PathVariable(value = "modle") String modle) {
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);
        ModelAndView mv = new ModelAndView();
        String fileName = file.getOriginalFilename();        
        if ("import".equals(modle)) {
        	if(!NetEnvUtils.isOutterNetReq(request)) {
        		throw new RuntimeException("非外网环境不得使用内网上传");
        	}
            String path = EnvUtils.getO2ISourceDir(userCode);

            File dest = new File(path + "/" + fileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            try {
                //保存文件
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mv.setViewName("redirect:/seraching?towhich=Import");
            return mv;
        } else if ("export".equals(modle)) {
        	if(!NetEnvUtils.isInnerNetReq(request)) {
        		throw new RuntimeException("非内网环境不得使用内网上传");
        	}
            String path = EnvUtils.getI2OSourceDir(userCode);

            File dest = new File(path + "/" + fileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            try {
                //保存文件
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mv.setViewName("redirect:/seraching?towhich=Export");
            return mv;
        }
        return null;
    }


    //下载文件列表
    @RequestMapping("/filedownloadlist")
    public ModelAndView fileDownloadList(HttpServletRequest request, HttpServletResponse response) {
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);
        //从configure中获取路径
        String path4Export = EnvUtils.getI2ODestDir(userCode);//外网 from-内网
        String path4Import = EnvUtils.getO2IDestDir(userCode); //内网 from-外网

        if(NetEnvUtils.isInnerNetReq(request)) {
        	 //获取内网中的所有文件
            File destImportFolder = new File(path4Import);
            File[] destImportFlies = destImportFolder.listFiles();
            request.setAttribute("destImportFlies", destImportFlies);
    	}
       

        if(NetEnvUtils.isOutterNetReq(request)) {
	        //获取外网中所有的文件
	        File destExportFolder = new File(path4Export);
	        File[] destExportFiles = destExportFolder.listFiles();
	        request.setAttribute("destExportFiles", destExportFiles);
        }

        ModelAndView mv = new ModelAndView("filedownloadlist");
        return mv;
    }


    //文件下载
    @RequestMapping("/filedownload")
    public ResponseEntity<InputStreamResource> filedownLoad(HttpServletRequest request, @RequestParam(value = "path") String filePath) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        filePath = filePath.replace("\\\\", "/").replace("\\", "/");
        String[] filePathData = filePath.split("/");
        String showFilename = filePathData[filePathData.length - 1];

        FileInputStream fis = new FileInputStream(new File(filePath));
        byte[] fileData = IOUtils.toByteArray(fis);
        ByteArrayInputStream bis = new ByteArrayInputStream(fileData);

        try {

            String header = request.getHeader("User-Agent").toUpperCase();
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                showFilename = URLEncoder.encode(showFilename, "utf-8");
                showFilename = showFilename.replace("+", "%20"); // IE下载文件名空格变+号问题
            } else {
                showFilename = new String(showFilename.getBytes(), "ISO8859-1");
            }

            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            String mimeType = "application/octet-stream";
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", showFilename));

            return ResponseEntity.ok().headers(headers).contentLength(fileData.length)
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(new InputStreamResource(bis));

        } finally {
            //关闭流，不关闭会导致删除失败
            fis.close();
            bis.close();
        }

    }


    //文件删除
    @RequestMapping("/filedelete")
    public ModelAndView deleteFile(@RequestParam(value = "path") String filePath) {

        File file = new File(filePath);

        if (file.isFile()) { //如果删除失败，强制删除
            file.delete();
        }

        ModelAndView mv = new ModelAndView("redirect:/filedownloadlist");
        return mv;
    }

}



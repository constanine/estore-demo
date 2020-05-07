package com.bokesoft.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bokesoft.threadPackge.JobContext;
import com.bokesoft.threadPackge.JobUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bokesoft.struc.FileBean;
import com.bokesoft.struc.MessageBean;
import com.bokesoft.utils.EnvUtils;
import com.bokesoft.utils.MIssionMessageUtils;

/*
 * @author:gushiming
 * @date:2019/12/30
 * @description:复制到内网/外网(查询并取出文件)
 * */
@Controller
public class SerachFileController {

    private Logger logger = Logger.getLogger(this.getClass());
    private List<FileBean> fileStateList = new ArrayList<FileBean>();
    private List<FileBean> newfilestatelist = new ArrayList<FileBean>();

    //将session中的getAttribute()和setAttribute()方法中的变量名常量化(其他类的常量也定义在本类中，需要的话直接调用)
    public static final String MISSIONID = "missionid";
    public static final String MISSIONTYPE = "missionType";
    public static final String FILESTATELIST = "fileStateList";
    public static final String SUMFILESIZE = "sumfilesize";
    public static final String CANNOTEXECUTE = "cannotExecute";
    public static final String FILENUMBER = "fileNumber";
    public static final String MISFINISH = "misfinish";
    public static final String ISAUTHORIZER = "isauthorizer";
    public static final String USERCODE = "userCode";
    public static final String MESSAGES = "messages";
    public static final String COPYFILELIST = "copyfilelist";
    public static final String FILENAME = "filename";
    public static final String MISSIONS = "missions";



    @RequestMapping("/seraching")
    public String serachFile(HttpServletRequest request) {
        //获取用户
        HttpSession session = request.getSession();
        String userCode = (String) request.getSession().getAttribute(SerachFileController.USERCODE);

        //准备空集合
        newfilestatelist = new ArrayList<FileBean>();
        fileStateList = new ArrayList<FileBean>();

        //获取消息
        List<MessageBean> messages = MIssionMessageUtils.getMissionMessage(userCode,request);
        request.setAttribute(MESSAGES, messages);

        //获取分页标向
        String missionType = request.getParameter("towhich");

        logger.info("MissionType:" + missionType + " to ready");
        logger.info("serlvert starting write......");

        String missionid = null;

        if ("Import".equals(missionType)) {
            //查询线程是否存在，如果存在，线程正在执行导入操作，显示剩余未导入的文件

            missionid = JobUtil.findUserImpMissionID(userCode);
            Boolean cannotExecute = Boolean.valueOf(missionid != null);

            if (cannotExecute) {
                session.setAttribute(MISSIONID, missionid);
                session.setAttribute(MISSIONTYPE, missionType);
                request.setAttribute(CANNOTEXECUTE, String.valueOf(cannotExecute));
                return "perpareForImport";
            } else {
                request.setAttribute(CANNOTEXECUTE,String.valueOf(cannotExecute));
                // 获取配置信息
                String getSourcePath = EnvUtils.getO2ISourceDir(userCode);
                logger.info(">>>>>>getsourceurl:" + getSourcePath);
                // 获取文件列表
                File file = new File(getSourcePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                logger.info("To import File which it's source url is:"
                        + getSourcePath);
                fileStateList = new ArrayList<FileBean>();
                //存储文件的集合
                fileStateList = ReadImportFileList(file, getSourcePath, missionType);
                int pages = fileStateList.size() / 10 + 1;
                request.setAttribute("pages", pages);
                // 检查是否有当前类型的任务执行


                request.setAttribute(FILESTATELIST, fileStateList);
                request.getSession().setAttribute(MISSIONID, missionid);
                request.setAttribute("sourceurl", getSourcePath);
                request.setAttribute("targeturl",
                        EnvUtils.getO2IDestDir(userCode));
                session.setAttribute(MISSIONTYPE, missionType);
                int size = fileStateList.size();
                request.setAttribute("size", size);

                int limitline = 0;
                long sum = 0;
                for (int j = 0; j < pages; j++) {
                    if ((j * 10 + 10) < size) {
                        limitline = j * 10 + 10;
                    } else {
                        limitline = size;
                    }

                    for (int i = j * 10; i < limitline; i++) {
                        sum += fileStateList.get(i).getSize();
                    }
                }
                session.setAttribute(SUMFILESIZE, sum);
                session.setAttribute(FILESTATELIST, fileStateList);

                return "perpareForImport";
            }
        } else if ("Export".equals(missionType)) {
            // 检查是否有当前类型的任务执行
            missionid = JobUtil.findUserTransportMissionID(userCode);

            Boolean cannotExecute = Boolean.valueOf(missionid != null);
            if (cannotExecute) {
                request.setAttribute(CANNOTEXECUTE, String.valueOf(cannotExecute));
                session.setAttribute(MISSIONID, missionid);
                session.setAttribute(MISSIONTYPE, missionType);
                return "perpareForExport";
            } else {
                request.setAttribute(CANNOTEXECUTE,String.valueOf(cannotExecute));
                String getSourceUrl = EnvUtils.getI2OSourceDir(userCode);
                logger.info(">>>>>>getsourceurl:" + getSourceUrl);
                // 获取文件列表
                File file = new File(getSourceUrl);
                logger.info("To Export File which it's source url is:"
                        + getSourceUrl + " to transportdisk");
                newfilestatelist = new ArrayList<FileBean>();
                newfilestatelist = ReadExportFileList(file, getSourceUrl, missionType);
                // 检查是否有当前类型的任务执行
                session.setAttribute(MISSIONTYPE, missionType);
                request.setAttribute(FILESTATELIST, newfilestatelist);
                int pages = newfilestatelist.size() / 25 + 1;
                request.setAttribute("pages", pages);
                int size = newfilestatelist.size();
                request.setAttribute("size", size);

                int limitline = 0;
                long sum = 0;
                List<String> filenames = new ArrayList<>();
                List<String> uris = new ArrayList<>();
                for (int j = 0; j < pages; j++) {
                    if ((j * 25 + 25) < size) {
                        limitline = j * 25 + 25;
                    } else {
                        limitline = size;
                    }

                    for (int i = j * 25; i < limitline; i++) {
                        sum += fileStateList.get(i).getSize();
                        filenames.add(fileStateList.get(i).getFilename());
                        uris.add(fileStateList.get(i).getPath());
                    }

                }
                session.setAttribute("filenames", filenames);
                session.setAttribute("uris", uris);
                session.setAttribute(SUMFILESIZE, sum);

                return "perpareForExport";
            }

        }
        return null;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
		/*//设置编码。防止乱码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 获取用户
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String userCode = user.getUserCode();
		//准备空集合
		List<FileBean> newfilestatelist = new ArrayList<FileBean>();

		// 获取分页标向
		String missionType = (String) request.getParameter("towhich");

		if ("Export".equals(missionType)) {
			String getSourceUrl = EnvUtils.getI2OSourceDir(userCode);
			logger.info(">>>>>>getsourceurl:" + getSourceUrl);
			// 获取文件列表
			File file = new File(getSourceUrl);
			logger.info("To Export File which it's source url is:"
					+ getSourceUrl + " to transportdisk");
			newfilestatelist = new ArrayList<FileBean>();
			PrintWriter out=response.getWriter();
			newfilestatelist = ReadExportFileList(file, getSourceUrl, missionType);
			JSONArray jsonArray=JSONArray.fromObject(newfilestatelist);
			out.print(jsonArray);
		}*/

    }

    /**
     * @param file
     * @return list 一个带有file-state 文件状态的集合。 file-state 文件状态的集合是文件的名字,大小,路径的集合
     * @method 根据file获取file中所有文件的状态
     */
    private List<FileBean> ReadImportFileList(File file, String getSourceUrl,
                                              String missionType) {

        if (file.isDirectory()) {
            try {
                File[] files = file.listFiles();
                ArrayList<File> fileList = new ArrayList<File>();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        FileBean filestate = new FileBean();
                        filestate.setFilename(files[i].getName());
                        filestate.setSize(files[i].length());
                        filestate.setPath(files[i].getPath());
                        filestate.setType("folder");
                        filestate.setRoot(getSourceUrl);
                        fileStateList.add(filestate);
                    } else {
                        fileList.add(files[i]);
                    }
                }
                for (File f : fileList) {
                    ReadImportFileList(f, getSourceUrl, missionType);
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException(e);
            }
        } else if (file.isFile()) {
            FileBean filestate = new FileBean();
            filestate.setFilename(file.getName());
            filestate.setSize(file.length());
            filestate.setPath(file.getPath());
            filestate.setType("file");
            filestate.setRoot(getSourceUrl);
            fileStateList.add(filestate);
        }

        return fileStateList;
    }

    /**
     * @param file
     * @return list 一个带有file-state 文件状态的集合。 file-state 文件状态的集合是文件的名字,大小,路径的集合
     * @method 根据file获取file中所有文件的状态
     */
    private List<FileBean> ReadExportFileList(File file, String getSourceUrl,
                                              String missionType) {

        if (file.isDirectory()) {
            try {
                File[] files = file.listFiles();
                ArrayList<File> fileList = new ArrayList<File>();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        ReadExportFileList(files[i], getSourceUrl, missionType);
                    } else {
                        fileList.add(files[i]);
                    }
                }
                for (File f : fileList) {
                    ReadExportFileList(f, getSourceUrl, missionType);
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (file.isFile()) {
            FileBean filestate = new FileBean();
            filestate.setFilename(file.getName());
            filestate.setSize(file.length());
            filestate.setPath(file.getPath());
            filestate.setRoot(getSourceUrl);
            fileStateList.add(filestate);
        }

        return fileStateList;
    }

}







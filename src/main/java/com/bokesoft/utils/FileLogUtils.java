package com.bokesoft.utils;

import com.bokesoft.StartApplication;
import com.bokesoft.struc.ExportMissionBean;
import com.bokesoft.struc.FileBean;
import com.bokesoft.struc.ImportMissionBean;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLogUtils {

    private static Logger logger = Logger.getLogger(FileLogUtils.class);

    private static JdbcTemplate getJdbcTemplate() {
        return StartApplication.getBean(JdbcTemplate.class);
    }

    /**
     * @param userCode
     * @param fileStateList userCode,fileStateList将文件列表日志写入对应的存放文件夹内
     * @method addFileList()
     */

    public static boolean addImFileList(String userCode, String missionid,
                                        List<FileBean> fileStateList) {
        String[] datastrs = missionid.split("-");
        String pathname = EnvUtils.getTaskDisk() + "/" + datastrs[2] + "/" + datastrs[3] + "/" + userCode;
        File txtpath = new File(pathname);
        if (!txtpath.exists()) {
            txtpath.mkdirs();
            logger.info("make a folder:" + txtpath);
        }

        String listtxt = missionid + "-im-filelist.txt";
        File txtfile = new File(txtpath, listtxt);
        if (!txtfile.exists()) {
            try {
                txtfile.createNewFile();
                logger.info("make a file:" + txtfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            OutputStream fos = new FileOutputStream(txtfile);
            for (int i = 0; i < fileStateList.size(); i++) {
                String text = fileStateList.get(i).getPath() + "\n";
                byte[] buffer = text.getBytes("UTF-8");
                fos.write(buffer);
                fos.flush();
            }
            fos.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    //通过userCode找到对应的导入任务存放文件列表日志，提取出文件名集合
    public static List<String> serachImFileListFromLog(String userCode, String missionid) {
        String sql = "select filelogUrl from immissiontable where userCode=? and missionid=?";
        Object[] params = {userCode, missionid};
        List<ImportMissionBean> immissions = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(ImportMissionBean.class),params);
        String url = immissions.get(0).getFilelogUrl();
        List<String> fileList = new ArrayList<String>();
        File file = new File(url);

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String s = new String();
            while ((s = in.readLine()) != null) {
                fileList.add(s);
            }

            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return fileList;
    }

    //使用pdf添加了文件列表
    public static boolean addExportFileListMethod(String userCode, String missionid,
                                                  List<String> fileinfo) {
        String[] datastrs = missionid.split("-");
        String pathname = EnvUtils.getTaskDisk() + "/" + datastrs[2] + "/" + datastrs[3] + "/" + userCode;
        File txtpath = new File(pathname);
        if (!txtpath.exists()) {
            txtpath.mkdirs();
            logger.info("make a folder:" + txtpath);
        }

        String listtxt = missionid + "-ex-filelist.txt";
        File txtfile = new File(txtpath, listtxt);
        if (!txtfile.exists()) {
            try {
                txtfile.createNewFile();
                logger.info("make a file:" + txtfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            OutputStream fos = new FileOutputStream(txtfile);
            for (int i = 0; i < fileinfo.size(); i++) {
                String text = fileinfo.get(i) + "\n";
                byte[] buffer = text.getBytes("utf-8");
                fos.write(buffer);
                fos.flush();
            }
            fos.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    //userCode 找到对应的导出任务至中转区存放文件列表日志，提取出文件名集合
    public static List<String> serachExportFileListFromLog(String userCode, String missionid) {

        String sql = "select exportfilelogurl from exmissiontable where userCode=? and missionid=? ";

        List<ExportMissionBean> exmissions = getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(ExportMissionBean.class), userCode, missionid);

        String url = exmissions.get(0).getExportfilelogUrl();
        List<String> fileList = new ArrayList<String>();
        File file = new File(url);
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String s;
            while ((s = in.readLine()) != null) {
                fileList.add(s);
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileList;
    }
}

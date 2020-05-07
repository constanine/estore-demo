package com.bokesoft.utils;

import com.bokesoft.StartApplication;
import com.bokesoft.config.ApplicationConfiguer;
import com.bokesoft.controller.SerachFileController;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EnvUtils {

    private static ApplicationConfiguer getConfiguer() {
        return StartApplication.getBean(ApplicationConfiguer.class);
    }
    /*
     * ================================以下是数据库的配置==========================
     */

    /**
     * @method getJDNIDataSourceByName() 由用户名获取配置对应的文件数据数据库
     */
    public static DataSource getJDNIDataSource() {
        DataSource ds = null;
        try {
            InitialContext context = new InitialContext();
            ds = (DataSource) context
                    .lookup("jdbc/xxx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ;
        return ds;
    }

    /**
     * @method getAuthorizorIds() 获取权限集合
     */
    public static List<String> getAuthorizorIds() {
        String names = getConfiguer().getSupervisors();
        List<String> AuthorizorIds = new ArrayList<String>();
        String[] namearray = names.split(",");
        AuthorizorIds = Arrays.asList(namearray);
        return AuthorizorIds;
    }
    /*
     * ================================以下是获取properites文件的配置==========================
     */

    /**
     * @method getOutDiskIp() 外网文件目标路径
     */

    public static String getOutDiskIp() {
        return getConfiguer().getOutter_net_disk_ip();
    }

    /**
     * @method getInDiskIp() 内网文件目标路径
     */
    public static String getInDiskIp() {
        return getConfiguer().getInner_net_disk_ip();
    }

    /**
     * @method 外网访问服务器ip
     */

    public static String getOutIp() {
        return getConfiguer().getOutter_net_ip();
    }

    /**
     * @method 内网访问服务器ip
     */
    public static String getInIp() {
        return getConfiguer().getInner_net_ip();
    }


    /**
     * @method getO2ISourceDir() 由用户名获取配置对应的导入任务文件源路径
     */
    public static String getO2ISourceDir(String userCode) {
        return getConfiguer().getOutter_net_disk_path() + "/" + userCode + "/to-内网";
    }

    /**
     * @method getI2OSourceDir() 由用户名获取配置对应的导出任务源文件目标路径
     */
    public static String getI2OSourceDir(String userCode) {
        return getConfiguer().getInner_net_disk_path() + "/" + userCode + "/to-外网";
    }


    /**
     * @method getI2ODestDir() 由用户名获取配置对应的导出任务源文件路径
     */
    public static String getI2ODestDir(String userCode) {
        return getConfiguer().getOutter_net_disk_path() + "/" + userCode + "/from-内网";
    }

    /**
     * @method getO2IDestDir() 由用户名获取配置对应的导入任务目标文件目标路径
     */
    public static String getO2IDestDir(String userCode) {
        return getConfiguer().getInner_net_disk_path() + "/" + userCode + "/from-外网";
    }


    /**
     * @method getTransportDiskUrl()  由用户名获取配置对应的导出任务目中转区目标路径
     */
    public static String getTransferDisk() {
        return getConfiguer().getTransfer_disk_path();
    }

    /**
     * @method getStep1RunlogUrl() 获取配置的内部区至中转区或中转区之外部储存区任务运行日志路径
     */
    public static String getTaskDisk() {
        return getConfiguer().getTask_data_disk();
    }

    public static String getADAddres() {
        return getConfiguer().getAdAddress();
    }

    //判断是否是审批人员
    public static boolean isAuthorizer(HttpServletRequest request) {
        String userCode = String.valueOf(request.getSession().getAttribute(SerachFileController.USERCODE));

        return getAuthorizorIds().contains(userCode);

    }



}

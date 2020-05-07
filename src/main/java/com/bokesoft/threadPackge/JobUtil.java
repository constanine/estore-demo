package com.bokesoft.threadPackge;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobUtil {

    static final ExecutorService EXSERVICE_2OUT = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    static final ExecutorService EXSERVICE_TBT = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    static final ExecutorService EXSERVICE_IMP = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    static final ExecutorService EXSERVICE_TRANSPORT = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    static volatile Map<String, JobContext> jobCache_2out = new HashMap<String, JobContext>();
    static volatile Map<String, JobContext> jobCache_TBT = new HashMap<String, JobContext>();
    static volatile Map<String, JobContext> jobCache_IMP = new HashMap<String, JobContext>();
    static volatile Map<String, JobContext> jobCache_TRANSPORT = new HashMap<String, JobContext>();

    public static final DateFormat DFY = new SimpleDateFormat("YYYY");
    public static final DateFormat DFM = new SimpleDateFormat("MM");
    public static final DateFormat DF = new SimpleDateFormat("YYYY-MM-dd.HH-mm-ss.SS");
    public static final DateFormat DFYMD = new SimpleDateFormat("YYYY-MM-dd");


    public static boolean isContainSameImpJob(String missionid) {
        return jobCache_IMP.keySet().contains(missionid);
    }

    public static boolean isContainSameTransportJob(String missionid) {
        return jobCache_TRANSPORT.keySet().contains(missionid);
    }

    public static boolean isContainSameTBTJob(String missionid) {
        return jobCache_TBT.keySet().contains(missionid);
    }

    public static boolean isContainSame2outJob(String missionid) {
        return jobCache_2out.keySet().contains(missionid);
    }


    public static String findUserImpMissionID(String userCode) {
        for (String runMissionid : JobUtil.jobCache_IMP.keySet()) {
            if (JobUtil.jobCache_IMP.get(runMissionid).getUserCode().equals(userCode)) {
                return runMissionid;
            }
        }

        return null;
    }

    public static String findUserTransportMissionID(String userCode) {
        for (String runMissionid : JobUtil.jobCache_TRANSPORT.keySet()) {
            if (JobUtil.jobCache_TRANSPORT.get(runMissionid).getUserCode().equals(userCode)) {
                return runMissionid;
            }
        }

        return null;
    }

    public static void createImpJob(JobContext jobContext) {
        //创建一个线程任务对象
        ImportThread ct = new ImportThread(jobContext);
        JobUtil.EXSERVICE_IMP.execute(ct);
        JobUtil.jobCache_IMP.put(jobContext.getMissionid(), jobContext);
    }


    public static void createTransportJob(JobContext jobContext) {
        //创建一个线程任务对象
        TransportThread ct = new TransportThread(jobContext);
        JobUtil.EXSERVICE_TRANSPORT.execute(ct);
        JobUtil.jobCache_TRANSPORT.put(jobContext.getMissionid(), jobContext);
    }

    public static void createTBTJob(JobContext jobContext) {
        Tran_BackThread tbt = new Tran_BackThread(jobContext);
        JobUtil.EXSERVICE_TBT.execute(tbt);
        JobUtil.jobCache_TBT.put(jobContext.getMissionid(), jobContext);
    }


    public static void create2outJob(JobContext jobContext) {
        // 创建线程任务对象
        ToOutExportThread toet = new ToOutExportThread(jobContext);
        // 启动线程任务
        JobUtil.EXSERVICE_2OUT.execute(toet);
        JobUtil.jobCache_2out.put(jobContext.getMissionid(), jobContext);
    }

    public static void setStopImp(String missionid) {
        if (jobCache_IMP.get(missionid) != null) {
            jobCache_IMP.get(missionid).setStop(true);
        }
    }

    public static void setStopTransport(String missionid) {
        if (jobCache_TRANSPORT.get(missionid) != null) {
            jobCache_TRANSPORT.get(missionid).setStop(true);
        }
    }

    public static Map<String, Object> getDataImp(String missionid) {
        if (jobCache_IMP.get(missionid) != null) {
            return jobCache_IMP.get(missionid).getData();
        }
        return null;
    }

    public static Map<String, Object> getDataTrasport(String missionid) {
        if (null != jobCache_TRANSPORT.get(missionid)) {
            return jobCache_TRANSPORT.get(missionid).getData();
        }
        return null;
    }


}

package com.tdtf.gwj8.myDataBase;

/**
 * Created by a on 2017/4/13.
 */

public class Diary {
    public static final String mainActivity_start = "开机";
    public static final String mainActivity_sampleOk = "/取样器/启动正常";
    public static final String mainActivity_sampleError = "/取样器/启动异常";
    public static final String mainActivity_sensorOk = "/传感器/启动正常";
    public static final String mainActivity_sensorError = "/传感器/启动异常";
    public static final String mainActivity_pressOk = "/压力舱/正常";
    public static final String mainActivity_pressError = "/压力舱/异常";
    public static final String login_start = "进入/登录/界面";
    public static final String ANTIFIVE = "/任务列表/加载失败";
    public static final String SIX = "共有？任务";
    public static final String SEVEN = "所有任务名字？";
    public static final String EIGHT = "所有任务id？";
    public static final String NINE = "选择的任务名字？与id？";
    public static final String mainMenu_start = "进入主界面";

    public static String login_userPower(String string_user, String string_power) {
        return "登录账户为/" + string_user + "/,权限等级为/" + string_power + "/。";
    }
    ////////////////////////////////检测设置
}

package com.tdtf.gwj8;

import android.util.Log;

import com.tdtf.gwj8.myDataBase.DataSort;
import com.tdtf.gwj8.myDataBase.MyDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by a on 2017/10/12.
 */

public class SolveFunction {
    public static String[] upJianCeSheZhi(String string) {
        String[] strings = new String[7];
        if (string.startsWith("aa0004") && string.endsWith("cc33c33c")) {
            String strCiShu = string.substring(6, 8);
            String strFangShi = string.substring(8, 10);
            String strYuZouLiang = string.substring(10, 12);
            String strQuYangLiang = string.substring(12, 14);
            String strWeiZhi = string.substring(14, 16);
            String strShengJiangFangShi = string.substring(16, 18);
            String strDanWei = string.substring(18, 20);

            strings[0] = "检测次数设置为：" + hex2dec(strCiShu);
            Myutils.setCiShu(hex2dec(strCiShu));
            if (strFangShi.equals("00")) {
                Myutils.setJiance("自动");
                strings[1] = "检测方式设置为：自动";
            } else {
                Myutils.setJiance("手动");
                strings[1] = "检测方式设置为：手动";
            }
            strings[2] = "预走量设置为：" + hex2fla(strYuZouLiang) + "ml";
            Myutils.setYuzouliang(hex2fla(strYuZouLiang));
            strings[3] = "取样量设置为：" + hex2fla(strQuYangLiang) + "ml";
            Myutils.setQuyangliang(hex2fla(strQuYangLiang));
            strings[4] = "取样头位置设置为：" + hex2dec(strWeiZhi);
            Myutils.setQuyangtou(hex2dec(strWeiZhi));
            if (strShengJiangFangShi.equals("00")) {
                Myutils.setShengjiang("自动");
                strings[5] = "升降方式设置为：自动";
            } else {
                Myutils.setShengjiang("手动");
                strings[5] = "升降方式设置为：手动";
            }
            if (hex2dec(strDanWei).equals("0")) {
                strings[6] = "计数单位设置为：XX/ml";
                Myutils.setDanWei("XX/ml");
            } else {
                strings[6] = "计数单位设置为：XX";
                Myutils.setDanWei("XX");
            }
        }
        return strings;
    }

    public static String[] upQingXiSheZhi(String string) {
        String[] strings = new String[2];
        if (string.startsWith("aa0005") && string.endsWith("cc33c33c")) {
            String strKaiJi = string.substring(6, 8);
            String strGuanJi = string.substring(8, 10);

            strings[0] = "开机清洗次数设置为：" + hex2dec(strKaiJi);
            strings[1] = "关机清洗次数设置为：" + hex2dec(strGuanJi);
        }
        return strings;
    }

    public static String[] upJiaoBanSuDu(String string) {
        String[] strings = new String[1];
        if (string.startsWith("aa0006") && string.endsWith("cc33c33c")) {
            String strJiaoBan = string.substring(6, 8);

            strings[0] = "搅拌速度设置为：" + hex2dec(strJiaoBan);
        }
        return strings;
    }

    public static String[] upTongDaoSheZhi(String string) {
        String[] strings = new String[5];
        if (string.startsWith("aa0007") && string.endsWith("cc33c33c")) {
            String strTongDao = string.substring(6, 8);
            String[] strZiDingYi_1 = new String[8];
            for (int i = 0; i < strZiDingYi_1.length; i++) {
                strZiDingYi_1[i] = hex2fla(string.substring(8 + 4 * i, 12 + 4 * i));
            }
            String[] strZiDingYi_2 = new String[8];
            for (int i = 0; i < strZiDingYi_2.length; i++) {
                strZiDingYi_2[i] = hex2fla(string.substring(40 + 4 * i, 44 + 4 * i));
            }
            String[] strZiDingYiSelect_1 = new String[8];
            for (int i = 0; i < strZiDingYiSelect_1.length; i++) {
                if (hex2dec(string.substring(72 + 2 * i, 74 + 2 * i)).equals("0")) {
                    strZiDingYiSelect_1[i] = "无效";
                } else {
                    strZiDingYiSelect_1[i] = "有效";
                }
            }
            String[] strZiDingYiSelect_2 = new String[8];
            for (int i = 0; i < strZiDingYiSelect_2.length; i++) {
                if (hex2dec(string.substring(88 + 2 * i, 90 + 2 * i)).equals("0")) {
                    strZiDingYiSelect_2[i] = "无效";
                } else {
                    strZiDingYiSelect_2[i] = "有效";
                }
            }
            switch (Integer.valueOf(strTongDao)) {
                case 0:
                    strings[0] = "当前通道设置为：药典";
                    break;
                case 1:
                    strings[0] = "当前通道设置为：输液器具";
                    break;
                case 2:
                    strings[0] = "当前通道设置为：麻醉器具";
                    break;
                case 3:
                    strings[0] = "当前通道设置为：自定义一";
                    break;
                case 4:
                    strings[0] = "当前通道设置为：自定义二";
                    break;
            }

            Myutils.setStandard(Integer.valueOf(strTongDao));
            ArrayList<String> arrayList_1 = new ArrayList<>();
            ArrayList<String> arrayList_2 = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                if (strZiDingYiSelect_1[i].equals("有效")) {
                    arrayList_1.add(strZiDingYi_1[i]);
                }
                if (strZiDingYiSelect_2[i].equals("有效")) {
                    arrayList_2.add(strZiDingYi_2[i]);
                }
            }


            Myutils.setArrayList_1(arrayList_1);
            Myutils.setArrayList_2(arrayList_2);

            strings[1] = "自定义一的粒径依次设置为：" + strZiDingYi_1[0] + "、" + strZiDingYi_1[1] + "、"
                    + strZiDingYi_1[2] + "、" + strZiDingYi_1[3] + "、" + strZiDingYi_1[4] + "、"
                    + strZiDingYi_1[5] + "、" + strZiDingYi_1[6] + "、" + strZiDingYi_1[7];
            strings[2] = "自定义二的粒径依次设置为：" + strZiDingYi_2[0] + "、" + strZiDingYi_2[1] + "、"
                    + strZiDingYi_2[2] + "、" + strZiDingYi_2[3] + "、" + strZiDingYi_2[4] + "、"
                    + strZiDingYi_2[5] + "、" + strZiDingYi_2[6] + "、" + strZiDingYi_2[7];
            strings[3] = "自定义一的通道有效性依次设置为：" + strZiDingYiSelect_1[0] + "、" + strZiDingYiSelect_1[1] + "、"
                    + strZiDingYiSelect_1[2] + "、" + strZiDingYiSelect_1[3] + "、" + strZiDingYiSelect_1[4] + "、"
                    + strZiDingYiSelect_1[5] + "、" + strZiDingYiSelect_1[6] + "、" + strZiDingYiSelect_1[7];
            strings[4] = "自定义二的通道有效性依次设置为：" + strZiDingYiSelect_2[0] + "、" + strZiDingYiSelect_2[1] + "、"
                    + strZiDingYiSelect_2[2] + "、" + strZiDingYiSelect_2[3] + "、" + strZiDingYiSelect_2[4] + "、"
                    + strZiDingYiSelect_2[5] + "、" + strZiDingYiSelect_2[6] + "、" + strZiDingYiSelect_2[7];
        }
        return strings;
    }

    public static String[] upZiJianXinXi(String string) {
        String[] strings = new String[5];
        if (string.startsWith("aa0008") && string.endsWith("cc33c33c")) {
            String strChuanGanQi = string.substring(6, 8);
            String strFa = string.substring(8, 10);
            String strQuYangQi = string.substring(10, 12);
            String strShengJiangZhuangZhi = string.substring(12, 14);
            String strBiaoDingZhi = string.substring(14, 16);

            if (hex2dec(strChuanGanQi).equals("0")) {
                strings[0] = "传感器状态：正常";
            } else {
                strings[0] = "传感器状态：异常";
            }
            if (hex2dec(strFa).equals("0")) {
                strings[1] = "阀状态：正常";
            } else {
                strings[1] = "阀状态：异常";
            }
            if (hex2dec(strQuYangQi).equals("0")) {
                strings[2] = "取样器状态：正常";
            } else {
                strings[2] = "取样器状态：异常";
            }
            if (hex2dec(strShengJiangZhuangZhi).equals("0")) {
                strings[3] = "升降装置状态：正常";
            } else {
                strings[3] = "升降装置状态：异常";
            }
            if (hex2dec(strBiaoDingZhi).equals("0")) {
                strings[4] = "标定值：正常";
            } else {
                strings[4] = "标定值：异常";
            }
        }
        return strings;
    }

    public static String[] upDaYinSheZhi(String string) {
        String[] strings = new String[4];
        if (string.startsWith("aa0009") && string.endsWith("cc33c33c")) {
            String strMingChe = string.substring(6, 8);
            String strJunZhi = string.substring(8, 10);
            String strShuJu = string.substring(10, 12);
            String strFenBu = string.substring(12, 14);

            if (hex2dec(strMingChe).equals("0")) {
                strings[0] = "名称：不打印";
            } else {
                strings[0] = "名称：打印";
            }
            if (hex2dec(strJunZhi).equals("0")) {
                strings[1] = "均值：不打印";
            } else {
                strings[1] = "均值：打印";
            }
            if (hex2dec(strShuJu).equals("0")) {
                strings[2] = "数据：不打印";
            } else {
                strings[2] = "数据：打印";
            }
            if (hex2dec(strFenBu).equals("0")) {
                strings[3] = "分布：不打印";
            } else {
                strings[3] = "分布：打印";
            }
        }
        return strings;
    }

    public static String[] upBiaoChi(String string) {
        String[] strings = new String[3];
        if (string.startsWith("aa000a") && string.endsWith("cc33c33c")) {
            String strBiaoDingDian = string.substring(6, 8);
            int n = Integer.valueOf(strBiaoDingDian, 10);
            String[] strLingJing = new String[n];
            for (int i = 0; i < strLingJing.length; i++) {
                strLingJing[i] = hex2fla(string.substring(8 + 4 * i, 12 + 4 * i));
            }
            String[] strBiaochi = new String[n];
            for (int m = 0; m < strBiaochi.length; m++) {
                strBiaochi[m] = hex2fla(string.substring(12 + 4 * (n - 1) + 4 * m, 16 + 4 * (n - 1) + 4 * m));
            }

            strings[0] = "标定点数量设置为：" + hex2dec(strBiaoDingDian);
            strings[1] = "标定点的粒径依次设置为：" + strLingJing[0] + "、" + strLingJing[1] + "、"
                    + strLingJing[2] + "、" + strLingJing[3] + "、" + strLingJing[4] + "、"
                    + strLingJing[5] + "、" + strLingJing[6] + "、" + strLingJing[7];
            strings[2] = "标定点的标尺依次设置为：" + strBiaochi[0] + "、" + strBiaochi[1] + "、"
                    + strBiaochi[2] + "、" + strBiaochi[3] + "、" + strBiaochi[4] + "、"
                    + strBiaochi[5] + "、" + strBiaochi[6] + "、" + strBiaochi[7];

        }
        return strings;
    }

    public static String[] upSuDuSheZhi(String string) {
        String[] strings = new String[3];
        if (string.startsWith("aa000b") && string.endsWith("cc33c33c")) {
            String strJianCeSuDu = string.substring(6, 8);
            String strHuiTuiSuDu = string.substring(8, 10);
            String strQingXiSuDu = string.substring(10, 12);

            strings[0] = "检测速度设置为：" + hex2dec(strJianCeSuDu);
            strings[1] = "回推速度设置为：" + hex2dec(strHuiTuiSuDu);
            strings[2] = "清洗速度设置为：" + hex2dec(strQingXiSuDu);
        }
        return strings;
    }

    public static String[] upZaoShengCeDing(String string) {
        String[] strings = new String[1];
        if (string.startsWith("aa000c") && string.endsWith("cc33c33c")) {
            strings[0] = "噪声测定";
        }
        return strings;
    }

    public static String[] upXiuZhengZhi(String string) {
        String[] strings = new String[2];
        if (string.startsWith("aa000d") && string.endsWith("cc33c33c")) {
            String strXiuZhengZhi_1 = string.substring(6, 10);
            String strXiuZhengZhi_2 = string.substring(10, 14);

            strings[0] = "修正值1设置为：" + hex2fla(strXiuZhengZhi_1);
            strings[1] = "修正值2设置为：" + hex2fla(strXiuZhengZhi_2);

        }
        return strings;
    }

    public static String[] upYangPinXinXi(String string) {
        String[] strings = new String[3];
        if (string.startsWith("aa000e") && string.endsWith("cc33c33c")) {

            char[] charYangPinMingCheng = new char[17];
            for (int i = 0; i < charYangPinMingCheng.length; i++) {
                int x = Integer.valueOf(string.substring(6 + 2 * i, 8 + 2 * i), 16);
                Log.d("ttt", "upYangPinXinXi: " + x);
                charYangPinMingCheng[i] = (char) x;
            }
            String strYangPinMingCheng = String.valueOf(charYangPinMingCheng);

            char[] charYangPinPiHao = new char[17];
            for (int i = 0; i < charYangPinPiHao.length; i++) {
                int x = Integer.valueOf(string.substring(40 + 2 * i, 42 + 2 * i), 16);
                charYangPinPiHao[i] = (char) x;
            }
            String strYangPinPiHao = String.valueOf(charYangPinPiHao);

            char[] charYangPinGuiGe = new char[17];
            for (int i = 0; i < charYangPinGuiGe.length; i++) {
                int x = Integer.valueOf(string.substring(74 + 2 * i, 76 + 2 * i), 16);
                charYangPinGuiGe[i] = (char) x;
            }
            String strYangPinGuiGe = String.valueOf(charYangPinGuiGe).trim();

            Myutils.setSampleName(strYangPinMingCheng);
            Myutils.setSampleNumbere(strYangPinPiHao);
            Myutils.setSampleType(strYangPinGuiGe);

            strings[0] = "样品名称设置为：" + strYangPinMingCheng;
            strings[1] = "样品批号设置为：" + strYangPinPiHao;
            strings[2] = "样品规格设置为：" + strYangPinGuiGe;
        }
        return strings;
    }

    public static void upJianCeShuJu(MyDatabaseHelper myDatabaseHelper, ArrayList<String> biaozhi, String string) {
        if (string.startsWith("aa000f") && string.endsWith("cc33c33c")) {
            String strJianCeCiShu = string.substring(6, 8);
            String strJianCeTongDao = string.substring(8, 10);
            int m = Integer.valueOf(strJianCeCiShu, 10);
            int n = Integer.valueOf(strJianCeTongDao, 10);
            String[] strJiFenShuJu = new String[n * m];
            for (int q = 0; q < strJiFenShuJu.length; q++) {
                strJiFenShuJu[q] = hex2fla(string.substring(10 + 8 * q, 18 + 8 * q));
            }
            String[] strWeiFenShuJu = new String[n * m];
            for (int q = 0; q < strWeiFenShuJu.length; q++) {
                strWeiFenShuJu[q] = hex2fla(string.substring(18 + 8 * (n * m - 1) + 8 * q,
                        26 + 8 * (n * m - 1) + 8 * q));
            }
            String[] strJiFenPingJun = new String[n];
            for (int q = 0; q < strJiFenPingJun.length; q++) {
                strJiFenPingJun[q] = hex2fla(string.substring(26 + 16 * (m * n - 1) + 8 * q,
                        34 + 16 * (m * n - 1) + 8 * q));
            }
            String[] strWeiFenPingJun = new String[n];
            for (int q = 0; q < strWeiFenPingJun.length; q++) {
                strWeiFenPingJun[q] = hex2fla(string.substring(34 + 16 * (n * m - 1) + 8 * (n - 1) + 8 * q,
                        42 + 16 * (n * m - 1) + 8 * (n - 1) + 8 * q));
            }
            Log.d("pppp", "upJianCeShuJu: " + Myutils.getStandard());
            switch (Myutils.getStandard()) {
                case 0:
                    DataSort.dataYaoDian(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strJiFenPingJun);
                    break;
                case 1:
                    switch (n) {
                        case 1:
                            DataSort.dataLvChu(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strJiFenPingJun);
                            break;
                        case 3:
                            DataSort.dataShuYe(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strJiFenPingJun);
                            break;
                    }
                    break;
                case 2:
                    DataSort.dataMaZui(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strJiFenPingJun);
                    break;
                case 3:
                    DataSort.dataZiDingYi(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strWeiFenShuJu, strJiFenPingJun, strWeiFenPingJun, "自定义一");
                    break;
                case 4:
                    DataSort.dataZiDingYi(myDatabaseHelper, biaozhi, m, n, strJiFenShuJu, strWeiFenShuJu, strJiFenPingJun, strWeiFenPingJun, "自定义二");
                    break;
            }
        }
    }

    private static String hex2dec(String string) {
        Integer dec = Integer.valueOf(string, 16);
        Log.d("QWER", "hex2dec: " + string);
        Log.d("QWER", "hex2dec: " + String.valueOf(dec));
        return String.valueOf(dec);
    }

    private static String hex2fla(String string) {
        Integer dec = 10;
        try {
            dec = Integer.parseInt(string, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("QWER", "hex2fla: " + string);
        Log.d("QWER", "hex2fla: " + String.valueOf(dec));
        Float fla = (float) dec / 10;
        return String.valueOf(fla);
    }
}

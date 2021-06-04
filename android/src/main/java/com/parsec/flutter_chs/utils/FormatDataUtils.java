package com.parsec.flutter_chs.utils;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 数据格式化工具
 */
public class FormatDataUtils {
    /**
     * 格式化身份证信息
     * <p>
     * Bundle 中字段说明:
     * errorCode 0x90表示成功，其他表示出错
     * name 姓名
     * sex 性别
     * nation 国籍
     * id_number 身份证号码
     * useful_start_year 证件签发日期-年
     * useful_start_moth 证件签发日期-月
     * useful_start_day 证件签发日期-日
     * useful_end_year  证件终止日期-年
     * useful_end_moth  证件终止日期-月
     * useful_end_day 证件终止日期-日
     * birth_year 出生日期-年
     * birth_moth 出生日期-月
     * birth_day 出生日期-日
     * address 地址
     * sign_office 签发机关
     * version 证件版本号
     * flag 证件类型标志
     * reserved 预留项
     * photo 相片,  byte[]类型
     * fingerprint指纹信息， byte[]类型，为空时表示无指纹信息
     *
     * @param bundle 读取的身份证信息
     * @return
     * @throws JSONException
     */
    public static JSONObject idCardData(Bundle bundle) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("name", bundle.get("name"));
        data.put("sex", bundle.get("sex"));
        data.put("nation", bundle.get("nation"));
        data.put("id_number", bundle.get("id_number"));
        data.put("useful_start_year", bundle.get("useful_start_year"));
        data.put("useful_start_moth", bundle.get("useful_start_moth"));
        data.put("useful_start_day", bundle.get("useful_start_day"));
        data.put("useful_end_year", bundle.get("useful_end_year"));
        data.put("useful_end_moth", bundle.get("useful_end_moth"));
        data.put("useful_end_day", bundle.get("useful_end_day"));
        data.put("birth_year", bundle.get("birth_year"));
        data.put("birth_moth", bundle.get("birth_moth"));
        data.put("birth_day", bundle.get("birth_day"));
        data.put("address", bundle.get("address"));
        data.put("sign_office", bundle.get("sign_office"));
        data.put("version", bundle.get("version"));
        data.put("flag", bundle.get("flag"));
        data.put("reserved", bundle.get("reserved"));
        data.put("photo", bundle.get("photo"));
        data.put("fingerprint", bundle.get("fingerprint"));
        return data;
    }

    /**
     * 格式化医保卡数据
     * <p>
     * Bundle 中字段说明:
     * errorCode 返回码，整型，0x90是成功
     * name 姓名
     * cardNo 卡号
     * sex 性别
     * idCardNo 身份证号
     * districtCode 地区码
     *
     * @param bundle 读取到的医保卡数据
     * @return
     * @throws JSONException
     */
    public static JSONObject healthCardData(Bundle bundle) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("name", bundle.get("name"));
        data.put("cardNo", bundle.get("cardNo"));
        data.put("sex", bundle.get("sex"));
        data.put("idCardNo", bundle.get("idCardNo"));
        data.put("districtCode", bundle.get("districtCode"));
        return data;
    }
}

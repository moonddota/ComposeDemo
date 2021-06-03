package com.example.composedemo.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.example.composedemo.bean.UserInfo;
import com.example.composedemo.constant.C;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MMkvHelper {
    private final Gson mGson = new Gson();

    private static MMKV mmkv;

    private MMkvHelper() {
        mmkv = MMKV.defaultMMKV();
    }

    public static MMkvHelper getInstance() {
        return MMkvHolder.INSTANCE;
    }

    private static class MMkvHolder {
        private static final MMkvHelper INSTANCE = new MMkvHelper();
    }


    /**
     * 保存用户信息
     */
    public void saveUserInfo(UserInfo userInfo) {
        mmkv.encode(C.USER_INFO, userInfo);
    }

    public UserInfo getUserInfo() {
        return mmkv.decodeParcelable(C.USER_INFO, UserInfo.class);
    }

    public void saveLanguage(Locale locale) {
        mmkv.encode(C.LANGUAGE, new Gson().toJson(locale));
    }

    public Locale getLanguage() {
        String s = mmkv.decodeString(C.LANGUAGE);
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        try {
            return new Gson().fromJson(s, Locale.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void saveProjectTabs(List<T> dataList) {
        saveList(C.PROJECT_TABS, dataList);
    }

    public <T> List<T> getProjectTabs(Class<T> cls) {
        return getTList(C.PROJECT_TABS, cls);
    }


    /**
     * 保存list
     */
    private <T> void saveList(String tag, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;
        //转换成json数据，再保存
        String strJson = mGson.toJson(dataList);
        mmkv.encode(tag, strJson);
    }

    private <T> List<T> getTList(String tag, Class<T> cls) {
        List<T> dataList = new ArrayList<>();
        String strJson = mmkv.decodeString(tag, null);
        if (null == strJson) {
            return dataList;
        }
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                dataList.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void logout() {
        mmkv.remove(C.USER_INFO);
    }

}

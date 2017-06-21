/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.gz.hkjs.app.api;

public class ApiConstants {
    public static final String NETEAST_HOST = "http://tapi.jianshen.1122.com/";
    public static final String ENDDETAIL_URL = "?service=";

    // 发现列表
    public static final String API_VERSION = ENDDETAIL_URL + "Version.Index";
    public static final String API_FIND_LIST = ENDDETAIL_URL + "Information.Infolist";
    public static final String API_RECIPES_LIST = ENDDETAIL_URL + "Foods.Get";
    public static final String API_RECIPES_TAB = ENDDETAIL_URL + "Foods.Tags";
    public static final String API_FIND_DETAIL = ENDDETAIL_URL + "Information.Detail";
    public static final String API_RECIPES_DETAIL = ENDDETAIL_URL + "Foods.Detail";
    public static final String API_VEDIO_LIST = ENDDETAIL_URL + "Lists.Get";
    public static final String API_Choose_LIST = ENDDETAIL_URL + "Lists.Screen";
    public static final String API_TRAIN_VEDIO_DETAIL_LIST = ENDDETAIL_URL + "Lists.Detail";
    public static final String API_USER_HOME_DATA = ENDDETAIL_URL + "Homes.Get";
    public static final String API_Homes_Delete = ENDDETAIL_URL + "Homes.Delete";
    public static final String API_HOME_ADD_TRAIN = ENDDETAIL_URL + "Homes.Add";

    public static final String API_USER_HOME_RECORD = ENDDETAIL_URL + "Homes.Record";

    public static final String API_USER_HOME_ADDRECORD = ENDDETAIL_URL + "Homes.Addrecord";

    //意见反馈
    public static final String API_USER_ADVICE_CREATE = ENDDETAIL_URL + "Advice.Create";
    //收藏
    public static final String API_USER_COLLECTION_RECIPE_LIST = ENDDETAIL_URL + "Collection.Lists";
    public static final String API_USER_COLLECTION_Find_LIST = ENDDETAIL_URL + "Collection.Lists";
    public static final String API_USER_COLLECTION_DELETE = ENDDETAIL_URL + "Collection.Delete";
    public static final String API_USER_COLLECTION_ADD = ENDDETAIL_URL + "Collection.Add";
    public static final String API_USER_CHECKLOGIN = NETEAST_HOST + ENDDETAIL_URL + "Default.CheckLogin";
    public static final String API_USER_LOGIN_OUT = NETEAST_HOST + ENDDETAIL_URL + "Default.LoginOut";


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NORMAL_HOST_TYPE:
                host = NETEAST_HOST;
                break;
            case HostType.VIDEO_NEWS_HOST_TYPE:
                host = "http://c.m.163.com/";
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}

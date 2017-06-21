package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * Created by xxq on 2017/5/17 0017.
 * 参数工具
 */

public class ParameterUtil {

    /**
     * 添加训练项目
     *
     * @param trainId 项目ID
     * @return
     */
    public static HashMap<String, String> getAddTrainMap(String trainId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", trainId);
        return map;
    }


}

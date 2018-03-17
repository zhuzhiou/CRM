package com.crm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wlrllr on 2018/3/14.
 */
public class CommonConstant {

    /**
     * 分组状态，可用
     */
    public static final String GROUP_STATUS_Y = "Y";
    /**
     * 分组状态，已满不可用
     */
    public static final String GROUP_STATUS_N = "N";

    public static List<Map<Integer, Integer>> deMergeGroup;

    static {
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 2);
        map1.put(2, 4);
        map1.put(3, 5);
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(1, 3);
        map2.put(2, 6);
        map2.put(3, 7);
        deMergeGroup = new ArrayList<>();
        deMergeGroup.add(map1);
        deMergeGroup.add(map2);
    }


    public static final Integer pageSize = 20;

    public static final Integer pageNum = 0;
}

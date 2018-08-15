package com.zz.map.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USER_NAME = "username";

    public static final Double RADIUS = 0.06;

    public interface RedisCacheExTime{
        int REDIS_SESSION_TIME = 60*30; //30分钟
        int REDIS_EVENT_TIME = 60*60*24; //一天
    }

    public interface REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";
    }

    public interface EVENT_STATUS{
        int OPEN = 0;
        int CLOSE = 1;
    }

    public interface Category{
        int SPORTS = 0;
        int CARD_GAME = 1;
        int INTERESTS = 2; //社团活动
        int GO_OUT = 3;
        int NEED_HELP =4;
    }

}
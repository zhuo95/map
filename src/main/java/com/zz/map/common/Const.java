package com.zz.map.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USER_NAME = "username";

    public static final Double RADIUS = 0.15;

    public interface RedisCacheExTime{
        int REDIS_SESSION_TIME = 60*30; //30分钟
    }

    public interface REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";
    }

    public interface Category{
        int SPORTS = 0;
        int CARD_GAME = 1;
        int COMPUTER_GAME = 2;
        int STUDENT_ASSOCIATION =3; //社团活动
        int NEED_HELP = 4;
    }

}
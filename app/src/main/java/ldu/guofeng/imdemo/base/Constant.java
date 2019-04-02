package ldu.guofeng.imdemo.base;

/**
 * 常量类
 *
 * @author 周明
 */
public class Constant {

//    //服务器地址、服务器名称、端口
//    public static final String IM_HOST = "112.74.61.166";
//    public static final String IM_SERVER = "112.74.61.166";

    //服务器地址、服务器名称、端口
   /* public static final String IM_HOST = "192.168.3.105";
    public static final String IM_SERVER = "192.168.3.105";*/

    public static final String IM_HOST = "192.168.43.143";
    public static final String IM_SERVER = "http://134.175.105.227:8080/HelloWeb";
    public static final int IM_PORT = 5222;

    //消息分隔符
    public static final String SPLIT = "卍";

    //消息类型
    public static final int MSG_TYPE_TEXT = 1;//文本消息
    public static final int MSG_TYPE_LOC = 2;//位置
    public static final int MSG_TYPE_SERVICER=3;//来自图灵机器人接口的数据
    public static final int MSG_TYPE_MySERVICER=4;//来自后台数据库的数据
}

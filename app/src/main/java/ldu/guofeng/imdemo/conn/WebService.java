package ldu.guofeng.imdemo.conn;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService {
    // IP地址
    //private static String IP = "192.168.1.103:8081";
    //private static String IP = "10.200.210.45:8081";
    private static String IP = "134.175.105.227";

    /**
     * 通过Get方式获取HTTP服务器数据
     *
     * @return
     */
    public static String executeHttpGet(String username, String password) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/LogLet";
            path = path + "?username=" + username + "&password=" + password;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public static String executeHttpGetForReg(String username, String password, int userflag, String checkcode) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/RegLet";
            path = path + "?username=" + username + "&password=" + password + "&userflag=" + userflag + "&check_code=" + checkcode;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
            Log.i("*********", "code:" + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return "服务器连接超时...";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "服务器连接超时...";
    }

    public static String executeHttpGetForInsertAuto(String username, String autograph) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/AutoLet";
            path = path + "?username=" + username + "&autograph=" + autograph;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
            int code = conn.getResponseCode();
            Log.i("code", "" + code);
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return "服务器连接超时...";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "服务器连接超时...";
    }

    public static String executeHttpGetForInsertNickname(String username, String nickname) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/Nickname";
            path = path + "?username=" + username + "&nickname=" + nickname;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
            int code = conn.getResponseCode();
            Log.i("code", "" + code);
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return "服务器连接超时...";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "服务器连接超时...";
    }


    // 将输入流转化为 String 型
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

    /*public static String SendGet(String ServletName, String user, String username, String row, String rowName) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try{
        String path = "http://" + IP + "/HelloWeb/"+ServletName+"";
        path = path + "?" + user + "=" + username + "&" + row + "=" + rowName;
        conn = (HttpURLConnection) new URL(path).openConnection();
        conn.setConnectTimeout(3000); // 设置超时时间
        conn.setReadTimeout(3000);
        conn.setDoInput(true);
        conn.setRequestMethod("GET"); // 设置获取信息方式
        conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
        int code = conn.getResponseCode();
        Log.i("code", "" + code);
        if (conn.getResponseCode() == 200) {
            is = conn.getInputStream();
            return parseInfo(is);
        }
        return "服务器连接超时...";

    }catch(
    Exception e)

    {
        e.printStackTrace();
    } finally

    {
        // 意外退出时进行连接关闭保护
        if (conn != null) {
            conn.disconnect();
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    return"服务器连接超时...";
}*/
    public static String executeHttpGetForDateAndSex(String username, String birth,String sex) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/SexLet";
            path = path + "?username=" + username + "&sex=" + sex+"&birth=" + birth;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
            int code = conn.getResponseCode();
            Log.i("code", "" + code);
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return "服务器连接超时...";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "服务器连接超时...";

    }
    public static String executeQueryAll(String username) {

        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            // 用户名 密码
            // URL 地址
            String path = "http://" + IP + "/HelloWeb/user_info";
            path = path + "?username=" + username;
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式
            int code = conn.getResponseCode();
            Log.i("code", "" + code);
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            return "服务器连接超时...";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "服务器连接超时...";

    }

}

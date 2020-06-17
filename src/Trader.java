import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.security.MessageDigest;

public class Trader {

    public static void main(String[] args) {
        // 账户ID
        String accountId = "168";
        // 账户KEY
        String apiKey = "267013233c62edd1255fdc7342f314df";
        // 查询持仓信息
        String url = "https://xt.dzd.com:8060/sa/postions";
        int seconds = (int) (System.currentTimeMillis() / 1000);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountId", accountId);
        jsonObject.put("timestramp", String.valueOf(seconds));
        jsonObject.put("token", getSign(accountId, apiKey, String.valueOf(seconds)));
        String  toJson = jsonObject.toString();
        String res = Trader.urlPostMethod(url,toJson);
        System.out.println(res);
    }


    /**
     * 获取post请求响应
     * @param url
     * @param params
     * @return
     */
    public static String urlPostMethod(String url,String params) {
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        try {
            if(params != null && !params.trim().equals("")) {
                RequestEntity requestEntity = new StringRequestEntity(params,"application/json","UTF-8");
                method.setRequestEntity(requestEntity);
            }
            method.releaseConnection();
            httpClient.executeMethod(method);
            String responses= method.getResponseBodyAsString();
            return responses;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字段拼接
     * @param account
     * @param key
     * @param timeStamp
     * @return
     */
    private static String getSign(String account, String key, String timeStamp) {
        return string2MD5(account + timeStamp + key);
    }

    /**
     * md5加密
     * @param inStr
     * @return
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception var8) {
            System.out.println(var8.toString());
            var8.printStackTrace();
            return "";
        }

        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int i = 0; i < charArray.length; ++i) {
            byteArray[i] = (byte)charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for(int i = 0; i < md5Bytes.length; ++i) {
            int val = md5Bytes[i] & 255;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }
}

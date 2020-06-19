import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.security.MessageDigest;

public class TraderBuy {
    public static void main(String[] args) {
        // 账户ID
        String accountId = "168";
        // 账户KEY
        String apiKey = "267013233c62edd1255fdc7342f314df";
        // 买入股票
        String url = "https://xt.dzd.com:8060/sa/buy";
        // 当前时间戳（秒为单位）
        int seconds = (int) (System.currentTimeMillis() / 1000);

        // 股票代码
        String code = "000756";
        // 股票价格
        String price = "7.0";
        // 买入数量（必需是100的倍数，交易所规定）
        String amount = "100";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountId", accountId);
        jsonObject.put("timestramp", String.valueOf(seconds));
        jsonObject.put("token", getSign(accountId,apiKey,String.valueOf(seconds),code,price,amount));
        jsonObject.put("code",code);
        jsonObject.put("price",price);
        jsonObject.put("amount",amount);
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
     * MD5(股票帐户ID+当前时间戳+KEY)
     * @param account
     * @param key
     * @param timeStamp
     * @return
     */
    private static String getSign(String account, String key, String timeStamp) {
        return string2MD5(account + timeStamp + key);
    }

    /**
     * MD5(股票帐户ID+当前时间戳+KEY+股票代码+价格+数量)
     * @param accountId
     * @param key
     * @param timeStamp
     * @param code
     * @param price
     * @param amount
     * @return
     */
    private static String getSign(String accountId,String key, String timeStamp,String code,String price ,String amount) {
        return string2MD5(accountId + timeStamp + key+code+price+amount);
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

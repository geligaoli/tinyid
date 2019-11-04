package com.xiaoju.uemc.tinyid.client.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class TinyIdHttpUtils {

    private static final Logger logger = Logger.getLogger(TinyIdHttpUtils.class.getName());


    private TinyIdHttpUtils() {

    }

    public static String post(String url, Integer readTimeout, Integer connectTimeout) {
        return post(url, null, readTimeout, connectTimeout);
    }

    public static String post(String url, Map<String, String> form, Integer readTimeout, Integer connectTimeout) {
        HttpURLConnection conn = null;
        StringBuilder param = new StringBuilder();

        if (form != null) {
            for (Map.Entry<String, String> entry : form.entrySet()) {
                if (param.length() != 0) param.append("&");
                param.append(urlencode(entry.getKey()))
                        .append("=").append(urlencode(entry.getValue()));
            }
        }

        try {
            byte[] postDataBytes = param.toString().getBytes("UTF-8");

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connectTimeout);
            conn.setUseCaches(false);
            conn.connect();

            OutputStream out = conn.getOutputStream();
            out.write(postDataBytes);
            out.flush();
            out.close();

            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while ((line = in.readLine()) != null)
                sb.append(line);
            in.close();

            return sb.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error post url:" + url + param, e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    private static String urlencode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

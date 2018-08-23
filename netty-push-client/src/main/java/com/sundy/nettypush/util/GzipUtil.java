package com.sundy.nettypush.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author plus.wang
 * @description 字符串压缩/解压工具，gzip，压缩后的字符串使用base64转码
 * @date 2018/8/22
 */
public class GzipUtil {

    /**
     * 使用gzip进行压缩
     *
     * @param primStr
     * @return
     * @throws IOException
     */
    public static String gzip(String primStr) throws IOException {
        String str = Base64.getEncoder().encodeToString(primStr.getBytes());
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString(StandardCharsets.ISO_8859_1.toString());
    }

    /**
     * 使用gzip进行解压缩
     *
     * @param str
     * @return 解压后的字符串
     * @throws IOException
     */
    public static String gunzip(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return new String(Base64.getDecoder().decode(out.toString()), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(gzip("大神给发的撒范德萨广东省广东省高"));
        System.out.println(gunzip(gzip("大神给发的撒范德萨广东省广东省高")));
    }
}
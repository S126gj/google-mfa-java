package com.sgj.util;

import com.sgj.auth.GoogleAuthenticator;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Guoji Shen
 * @Date: 2022/9/3 22:08
 * @Desc  身份认证工具类
 */
public class GoogleAuthUtil {
    private static final GoogleAuthenticator ga = new GoogleAuthenticator();

    public static Map<String,String> genAuthQrCode(String account) {// 生成密钥
        String secret = GoogleAuthenticator.generateSecretKey();

        // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
        String qrcode = GoogleAuthenticator.getQRBarcode(account, secret);

        Map<String,String> map = new HashMap<String,String>() ;
        map.put("qrcode", qrcode) ;
        map.put("secret", secret) ;
        map.put("account", account) ;
        //将account--secret 以键值对形式保存到数据库中

        System.out.println("qrcode:" + qrcode + ",secret:" + secret);
        return map ;
    }

    /**
     * 对app的随机生成的code,输入并验证
     * 根据account在数据库中将secret取出来
     */
    public static boolean verify(String account ,String secret , String scode) {
        long code = Long.parseLong(scode);
        long t = System.currentTimeMillis();

        ga.setWindowSize(2);//设置允许延迟时间   n * 30 秒   即（n+1)*30秒内有效
        boolean r = ga.check_code(secret, code, t);
        System.out.println("检查code是否正确？" + r);

        return r ;
    }
}
package com.sgj.controller;

import com.sgj.auth.GoogleAuthenticator;
import com.sgj.util.QrCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Guoji Shen
 * @Date: 2022/9/3 22:31
 */
@Slf4j
@RestController
public class Test {

    @GetMapping("/genSecretTest")
    public void genSecretTest(String username, HttpServletResponse response) {
        String secret = GoogleAuthenticator.generateSecretKey();
        // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
        String qrcode = GoogleAuthenticator.getQRBarcode(username, secret);
        QrCodeUtil.createQrCodeByUrl(qrcode, response);
        log.info("qrcode:[{}],secret:[{}]", qrcode, secret);
    }

    @GetMapping("/verifyTest")
    public String verifyTest(long code, String secret) {
        long t = System.currentTimeMillis();
        System.out.println(t);
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(1); //设置允许延迟时间
        // 此处 secret 需要与 genSecretTest 接口返回的 secret一致
        boolean r = ga.check_code(secret, code, t);
        return r ? "验证成功" : "验证失败";
    }
}
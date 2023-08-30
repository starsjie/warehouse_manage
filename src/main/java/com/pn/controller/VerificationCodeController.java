package com.pn.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author ljj
 * @date 2023/8/5 13:27
 */
@RestController
public class VerificationCodeController {
    //注入id引用名为captchaProducer的Producer接口的实现类DefaultKaptcha的bean对象
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成验证码图片的url接口/captcha/captchaImage
     */
    @GetMapping("/captcha/captchaImage")
    public void getKaptchaImage(HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            //生成验证码文本
            String code = captchaProducer.createText();
            //生成验证码图片
            BufferedImage image = captchaProducer.createImage(code);

            //将验证码文本存储到redis
            stringRedisTemplate.opsForValue().set(code, "", 60 * 15, TimeUnit.SECONDS);

            /*
              将验证码图片响应给前端
             */
            //设置响应正文image/jpg
            response.setContentType("image/jpg");
            //将验证码图片写给前端
            out = response.getOutputStream();
            //使用响应对象的字节输出流写入验证码图片，自然是给前端写入
            ImageIO.write(image, "jpg", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭字节输出流
            if (out != null) {

                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}

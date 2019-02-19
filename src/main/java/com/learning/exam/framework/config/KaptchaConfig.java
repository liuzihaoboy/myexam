package com.learning.exam.framework.config;

import com.google.code.kaptcha.BackgroundProducer;
import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.text.WordRenderer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @author liuzihao
 * @date 2019-01-25  11:21
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha defaultKaptcha(){
        MyDefaultKaptcha kaptcha = new MyDefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        properties.setProperty("kaptcha.image.width", "100");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
    class MyDefaultKaptcha extends DefaultKaptcha{
        private int width = 100;
        private int height = 40;
        @Override
        public BufferedImage createImage(String text) {
            GimpyEngine gimpyEngine = this.getConfig().getObscurificatorImpl();
            BackgroundProducer backgroundProducer = this.getConfig().getBackgroundImpl();
            boolean isBorderDrawn = this.getConfig().isBorderDrawn();
            this.width = this.getConfig().getWidth();
            this.height = this.getConfig().getHeight();
            BufferedImage bi = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR );
            bi = gimpyEngine.getDistortedImage(bi);
            bi = backgroundProducer.addBackground(bi);
            Graphics2D graphics = bi.createGraphics();
            if (isBorderDrawn) {
                this.drawBox(graphics,text);
            }
            return bi;
        }
        private void drawBox(Graphics2D graphics,String text){
            Color borderColor = this.getConfig().getBorderColor();
            graphics.fillRect(0, 0, width, height);
            graphics.setFont(new Font("黑体",Font.BOLD , 8));
            //第一条
            graphics.setColor(borderColor);
            graphics.drawLine(5,20, 25, 10);
            graphics.drawLine(25, 10, 65, 10);
            graphics.drawLine( 65, 10,85,35);
            //第二条
            graphics.setColor(borderColor);
            graphics.drawLine(15,5, 25, 25);
            graphics.drawLine(25, 25, 65,38 );
            graphics.drawLine( 65,38,85,15);
            //第三条
            graphics.setColor(borderColor);
            graphics.drawLine(10,35, 35, 30);
            graphics.drawLine(35,30, 45,20 );
            graphics.drawLine( 45,20,75,10);
            graphics.setFont(new Font("微软雅黑", Font.PLAIN, 20));
            //写字符串
            for(int i=0;i<text.length();i++){
                graphics.drawString(String.valueOf(text.charAt(i)),10+(i*20+5),25);
            }
            graphics.dispose();
        }
    }
}

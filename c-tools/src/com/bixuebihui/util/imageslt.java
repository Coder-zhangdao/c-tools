package com.bixuebihui.util;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * 生成验证码的servlet
 */
public final class imageslt extends HttpServlet {

    Color getRandColor(int fc, int bc) {// ¸ø¶¨·¶Î§»ñµÃËæ»úÑÕÉ«
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException, ServletException {


        response.setContentType("image/jpeg");

        // ÉèÖÃÒ³Ãæ²»»º´æ
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // ÔÚÄÚ´æÖÐ´´½¨Í¼Ïó
        int width = 60, height = 20;
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // »ñÈ¡Í¼ÐÎÉÏÏÂÎÄ
        Graphics g = image.getGraphics();

        // Éú³ÉËæ»úÀà
        Random random = new Random();

        // Éè¶¨±³¾°É«
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // Éè¶¨×ÖÌå
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        // »­±ß¿ò
        // g.setColor(new Color());
        // g.drawRect(0,0,width-1,height-1);

        // Ëæ»ú²úÉú155Ìõ¸ÉÈÅÏß£¬Ê¹Í¼ÏóÖÐµÄÈÏÖ¤Âë²»Ò×±»ÆäËü³ÌÐòÌ½²âµ½
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // È¡Ëæ»ú²úÉúµÄÈÏÖ¤Âë(4Î»Êý×Ö)
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            // ½«ÈÏÖ¤ÂëÏÔÊ¾µ½Í¼ÏóÖÐ
            g.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));// µ÷ÓÃº¯Êý³öÀ´µÄÑÕÉ«ÏàÍ¬£¬¿ÉÄÜÊÇÒòÎªÖÖ×ÓÌ«½Ó½ü£¬ËùÒÔÖ»ÄÜÖ±½ÓÉú³É
            g.drawString(rand, 13 * i + 6, 16);
        }

        // ½«ÈÏÖ¤Âë´æÈëSESSION
        // session.setAttribute("rand",sRand);

        // Í¼ÏóÉúÐ§
        g.dispose();

        // Êä³öÍ¼Ïóµ½Ò³Ãæ
        ImageIO.write(image, "JPEG", response.getOutputStream());

    }
}

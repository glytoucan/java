package org.glytoucan.web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.servlet.CaptchaServletUtil;

import static nl.captcha.Captcha.NAME;

@Controller
@ApiExclude
@RequestMapping("/Captcha")
public class CaptchaController {
	Log logger = LogFactory.getLog(CaptchaController.class);
  public static final String FILE_TYPE = "jpeg";
  
  private static final String PARAM_HEIGHT = "height";
  private static final String PARAM_WIDTH = "width";

  protected int _width = 200;
  protected int _height = 50;
  
	 @RequestMapping("/image")
	  public void captcha(HttpServletRequest request,
	      HttpServletResponse response) {
	   

     Captcha captcha = new Captcha.Builder(_width, _height)
       .addText()
       .addBackground(new GradiatedBackgroundProducer())
         .gimp()
         .addNoise()
         .addBorder()
         .build();

     CaptchaServletUtil.writeImage(response, captcha.getImage());
     request.getSession().setAttribute(NAME, captcha);
     
//     response.setHeader("Cache-Control", "no-cache");
//     response.setDateHeader("Expires", 0);
//     response.setHeader("Pragma", "no-cache");
//     response.setDateHeader("Max-Age", 0);
//
//     String captchaStr="";
//
//     captchaStr=Captcha.generateText(6);
//
//     try {
//
//         int width=100;      int height=40;
//
//         Color bg = new Color(0,255,255);
//         Color fg = new Color(0,100,0);
//
//         Font font = new Font("Arial", Font.BOLD, 20);
//         BufferedImage cpimg =new BufferedImage(width,height,BufferedImage.OPAQUE);
//         Graphics g = cpimg.createGraphics();
//
//         g.setFont(font);
//         g.setColor(bg);
//         g.fillRect(0, 0, width, height);
//         g.setColor(fg);
//         g.drawString(captchaStr,10,25);   
//
//         HttpSession session = request.getSession(true);
//         session.setAttribute("CAPTCHA", captchaStr);
//
//        OutputStream outputStream = response.getOutputStream();
//
//        ImageIO.write(cpimg, FILE_TYPE, outputStream);
//        outputStream.close();
//
//     } catch (Exception e) {
//             e.printStackTrace();
//     }
     
     
     
//     byte[] captchaChallengeAsJpeg = null;
//     // the output stream to render the captcha image as jpeg into
//      ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
//      try {
//      // get the session id that will identify the generated captcha.
//      //the same id must be used to validate the response, the session id is a good candidate!
//      String captchaId = request.getSession().getId();
//      // call the ImageCaptchaService getChallenge method
//          BufferedImage challenge =
//              imageCaptchaService.getImageChallengeForID(captchaId,
//                          request.getLocale());
//
//          
//          ImageIO.write(challenge, "jpg", jpegOutputStream);
////          // a jpeg encoder
////          JPEGImageEncoder jpegEncoder =
////                  JPEGCodec.createJPEGEncoder(jpegOutputStream);
////          jpegEncoder.encode(challenge);
//
//      captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
//
//      // flush it in the response
//      response.setHeader("Cache-Control", "no-store");
//      response.setHeader("Pragma", "no-cache");
//      response.setDateHeader("Expires", 0);
//      response.setContentType("image/jpeg");
//      ServletOutputStream responseOutputStream;
//      responseOutputStream = response.getOutputStream();
//      responseOutputStream.write(captchaChallengeAsJpeg);
//      responseOutputStream.flush();
//      responseOutputStream.close();
//      } catch (IOException e) {
//      }
	 }
}
package com.oxchains.themis.user.rest;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.net.HttpHeaders;
import com.oxchains.basicService.files.tfsService.TFSConsumer;
import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.VerifyCode;
import com.oxchains.themis.common.util.ImageBase64;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyCodeUtils;

import com.oxchains.themis.repo.entity.User;
import com.oxchains.themis.repo.entity.UserRelation;
import com.oxchains.themis.user.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author ccl
 * @time 2017-10-12 18:19
 * @name UserController
 * @desc:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    UserService userService;

    @Value("${user.info.image}")
    private String imageUrl;

    @Resource
    TFSConsumer tfsConsumer;

    @PostMapping(value = "/register")
    public RestResp register(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping(value = "/login")
    public RestResp login(@RequestBody User user){
        return userService.login(user);
    }

    @GetMapping(value = "/queryRedis/{key}")
    public String exist(@PathVariable String key){
        return JsonUtil.toJson(userService._queryRedisValue(key));
    }

    @PostMapping(value = "/logout")
    public RestResp logout(@RequestBody User user){
        return userService.logout(user);
    }

    @PostMapping(value = "/update")
    public RestResp update(@RequestBody User user){
        return userService.updateUser(user);
    }
    @GetMapping(value = "/list")
    public RestResp list(){
        return userService.findUsers();
    }

    /**
     * Verification Code
     * @return
     */
    @GetMapping(value = "/verifyCode")
    public RestResp verifyCode(){
        return RestResp.success(VerifyCodeUtils.getRandCode(6));
    }

    @RequestMapping(value = "/info")
    public RestResp info(@ModelAttribute User user) throws Exception{
        if(null == user){
            return RestResp.fail("参数不能为空");
        }
        MultipartFile file = user.getFile();
        if(null != file){
//            String fileName = file.getOriginalFilename();
//            String suffix = fileName.substring(fileName.lastIndexOf("."));
//            String newFileName = user.getLoginname() + suffix;
//            String pathName = imageUrl + newFileName;
//            File f =new File(pathName);
//            if(f.exists()){
//                f.delete();
//            }
//            file.transferTo(new File(pathName));
            String newFileName = tfsConsumer.saveTfsFile(file);
            user.setImage(newFileName);
            return userService.updateUser(user,ParamType.UpdateUserInfoType.INFO);
        }

        String image = user.getLoginname()+".jpg";
        if(null != user.getImage() && !"undefined".equals(user.getImage())) {
            //ImageBase64.generateImage(user.getImage(), imageUrl + image);
            image = tfsConsumer.saveTfsFile(ImageBase64.getImageBytes(user.getImage()),null);
            user.setImage(image);
        }
        return userService.updateUser(user,ParamType.UpdateUserInfoType.INFO);
    }
    /*
   *下载图片
   * */
    @RequestMapping(value = "/image")
    public void downloadImage(String fileName, HttpServletResponse response){
        try {
            File file = new File(imageUrl + fileName);
            if(file.exists()){
                Path filePath = file.toPath();
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                response.setContentType(HttpURLConnection.guessContentTypeFromName(file.getName()));
                response.setContentLengthLong(file.length());
                Files.copy(filePath, response.getOutputStream());
            }else{
                try {
                    response.setStatus(SC_NOT_FOUND);
                    response.getWriter().write("file not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/fpassword")
    public RestResp fpassword(@RequestBody User user){
        return userService.updateUser(user, ParamType.UpdateUserInfoType.FPWD);
    }

    @PostMapping(value = "/email")
    public RestResp email(@RequestBody User user){
        return userService.updateUser(user, ParamType.UpdateUserInfoType.EMAIL);
    }

    @PostMapping(value = "/phone")
    public RestResp phone(@RequestBody User user){
        return userService.updateUser(user, ParamType.UpdateUserInfoType.PHONE);
    }

    @PostMapping(value = "/password")
    public RestResp password(@RequestBody User user){
        return userService.updateUser(user, ParamType.UpdateUserInfoType.PWD);
    }
    @GetMapping(value = "/trust")
    public RestResp trust(com.oxchains.themis.common.param.RequestBody body){
        if(body.getType() == ParamType.TrustTabType.TRUSTED.getType()){
            return userService.trustedUsers(body);
        }else if(body.getType() == ParamType.TrustTabType.TRUST.getType()){
            return userService.trustUsers(body, Status.TrustStatus.TRUST);
        }else {
            return userService.trustUsers(body, Status.TrustStatus.SHIELD);
        }
    }

    @PostMapping(value = "/trust")
    public RestResp relation(UserRelation relation){
        return userService.relation(relation);
    }

    @GetMapping(value = "/relation")
    public RestResp getRelation(UserRelation relation){
        return userService.getRelation(relation);
    }

    @PostMapping(value = "/forget")
    public RestResp forgetPwd(com.oxchains.themis.common.param.RequestBody body){
        return userService.forgetPwd(body);
    }

    @GetMapping(value = "/arbitrations")
    public String getArbitrations(){
        return JsonUtil.toJson(userService.getArbitrations());
    }

    @GetMapping(value = "/findOne")
    public String getUser(Long id){
        return JsonUtil.toJson(userService.getUser(id));
    }


    @Resource
    DefaultKaptcha defaultKaptcha;

    @RequestMapping(value = "/imgVcode")
    public void defaultKaptcha(VerifyCode vcode,HttpServletRequest request, HttpServletResponse response) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            if(!userService.saveVcode(vcode.getKey(),createText)){
                request.getSession().setAttribute(vcode.getKey(), createText);
            }
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping(value = "/phoneVcode")
    public RestResp phoneVcode(VerifyCode vcode,HttpServletRequest request) throws Exception{
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            if(!userService.saveVcode(vcode.getKey(),createText)){
                request.getSession().setAttribute(vcode.getKey(), createText);
            }
            //手机发送
            return RestResp.success(createText);
        } catch (IllegalArgumentException e) {
            return RestResp.fail("404");
        }
    }

    @RequestMapping("/verifyICode")
    public RestResp verifytKaptchaCode(VerifyCode vcode, HttpServletRequest request, HttpServletResponse response){
        String vcodeVal = userService.getVcodeFromRedis(vcode.getKey());
//        String captchaId = (String) request.getSession().getAttribute("vcode");
//        String parameter = request.getParameter("vcode");

        if (vcodeVal.equals(vcode.getVcode())) {
            return RestResp.success("验证码正确");
        }
        return RestResp.fail("验证码错误");
    }

    @RequestMapping(value = "/sendVmail")
    public RestResp sendVerifyMail(VerifyCode vcode){
        return userService.sendVmail(vcode);
    }

    @PostMapping(value = "/resetpwd")
    public RestResp resetpwd(String resetkey,String password){
        return userService.resetpwd(resetkey,password);
    }
}

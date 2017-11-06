package com.oxchains.themis.arbitrate.rest;
import com.google.common.net.HttpHeaders;
import com.oxchains.themis.arbitrate.common.Pojo;
import com.oxchains.themis.arbitrate.common.RegisterRequest;
import com.oxchains.themis.arbitrate.service.ArbitrateService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@RestController
public class ArbitrateController {
    @Resource
    private ArbitrateService arbitrateService;
    @Value("${evidence.image.url}")
    private String imageUrl;
    /**
     * 仲裁人查看自己可以仲裁的订单列表 或者是仲裁过得
     * */
    @RequestMapping("/arbitrate/findArbitrareOrderById")
    public RestResp findArbitrareOrderById(@RequestBody Pojo pojo){
        this.checkPage(pojo);
        return arbitrateService.findArbitrareOrderById(pojo);
    }
    /**
     * 卖家或买家弟订单发起仲裁
     * */
    @RequestMapping("/arbitrate/arbitrateOrder")
    public RestResp arbitrateOrder(@RequestBody Pojo pojo){
        return arbitrateService.arbitrateOrder(pojo.getId());
    }
    /*
    * 十二 仲裁者对订单进行仲裁 仲裁者仲裁将密匙碎片给胜利者
    * */
    @RequestMapping("/arbitrate/arbitrateOrderToUser")
    public RestResp arbitrateOrderToUser(@RequestBody Pojo pojo){
        return arbitrateService.arbitrateOrderToUser(pojo);
    }
    //上传交易凭据 包括 文本 和 图片 附件
    @RequestMapping("/arbitrate/uploadEvidence")
    public RestResp uploadEvidence(@ModelAttribute @Valid RegisterRequest registerRequest) throws IOException {
        return arbitrateService.uploadEvidence(registerRequest,imageUrl);
    }
    /*
    * 仲裁者获取 卖家买家上传的聊天记录和转账记录附件
    * */
    @RequestMapping("/arbitrate/getEvidence")
    public RestResp getEvidence(@RequestBody Pojo pojo){
        return arbitrateService.getEvidence(pojo);
    }
    /*
    *下载图片
    * */
    @RequestMapping("/arbitrate/{fileName}/downloadfile")
    public void downloadfile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response){
        try {
            File applicationFile = new File(imageUrl + fileName);
            if(applicationFile.exists()){
                Path filePath = applicationFile.toPath();
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + applicationFile.getName());
                response.setContentType(HttpURLConnection.guessContentTypeFromName(applicationFile.getName()));
                response.setContentLengthLong(applicationFile.length());
                Files.copy(filePath, response.getOutputStream());
            }
            else{
                fileNotFound(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkPage(Pojo pojo){
        if(pojo.getPageSize() == null){
            pojo.setPageSize(8);
        }
        if(pojo.getPageNum() == null){
            pojo.setPageNum(1);
        }
    }
    private void fileNotFound(HttpServletResponse response) {
        try {
            response.setStatus(SC_NOT_FOUND);
            response.getWriter().write("file not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

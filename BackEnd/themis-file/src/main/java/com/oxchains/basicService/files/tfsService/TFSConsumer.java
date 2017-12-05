package com.oxchains.basicService.files.tfsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.oxchains.basicService.files.entity.FileInfos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
/**
 * Created by xuqi on 2017/12/1.
 */
@Service
public class TFSConsumer {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
     @Reference(version = "1.0.0")
     TFSService tfsService;
    //将文件存入文件服务器 并返回一个新的文件name
    public String saveTfsFile(MultipartFile multipartFile) {
        String s = null;
        try {
            FileInfos fileInfos = new FileInfos();
            fileInfos.setFile(multipartFile.getBytes());
            fileInfos.setFilename(multipartFile.getOriginalFilename());
            s = tfsService.saveTfsFile(fileInfos);
        } catch (IOException e) {
            LOG.error("save file faild : {}",e);
        }
        return s;
    }
    public String saveTfsFile(byte[] bytes,String filename) {
        String s = null;
        try {
            FileInfos fileInfos = new FileInfos();
            fileInfos.setFile(bytes);
            fileInfos.setFilename(filename);
            s = tfsService.saveTfsFile(fileInfos);
        } catch (Exception e) {
            LOG.error("save file faild : {}",e);
        }
        return s;
    }
    public String saveTfsLargeFile(MultipartFile multipartFile) {
        String s = null;
        try {
            FileInfos fileInfos = new FileInfos();
            fileInfos.setFile(multipartFile.getBytes());
            fileInfos.setFilename(multipartFile.getOriginalFilename());
            s = tfsService.saveTfsLargeFile(fileInfos);
        } catch (IOException e) {
            LOG.error("save large file faild : {}",e);
        }
        return s;

    }

    //从文件服务器读取文件
    public FileInfos getTfsFile(String tfsFileName) {
        FileInfos tfsFile = null;
        try {
            tfsFile = tfsService.getTfsFile(tfsFileName);
        } catch (Exception e) {
            LOG.error("get file faild : {}",e);
        }
        return tfsFile;
    }


}

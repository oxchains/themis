package com.oxchains.basicService.files.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xuqi on 2017/12/1.
 */
@Data
public class FileInfos implements Serializable{
    private byte[] file;          //文件本身
    private String filename;     //上传时的文件名
    private String tfsFilename; //文件系统内的文件名
    private Integer length;    //文件大小
}

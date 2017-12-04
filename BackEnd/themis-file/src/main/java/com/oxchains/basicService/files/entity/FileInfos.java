package com.oxchains.basicService.files.entity;

import java.io.Serializable;

/**
 * Created by xuqi on 2017/12/1.
 */
public class FileInfos implements Serializable{
    private byte[] file;          //文件本身
    private String filename;     //上传时的文件名
    private String tfsFilename; //文件系统内的文件名
    private Integer length;    //文件大小

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTfsFilename() {
        return tfsFilename;
    }

    public void setTfsFilename(String tfsFilename) {
        this.tfsFilename = tfsFilename;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}

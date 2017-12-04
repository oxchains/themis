package com.oxchains.basicService.files.tfsService;


import com.oxchains.basicService.files.entity.FileInfos;

/**
 * Created by xuqi on 2017/12/1.
 */
public interface TFSService {
    public String saveTfsFile(FileInfos fileInfo);
    public String saveTfsLargeFile(FileInfos fileInfo);
    public FileInfos getTfsFile(String tfsFileName);
}

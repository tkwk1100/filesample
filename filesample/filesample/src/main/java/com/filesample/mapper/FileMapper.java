package com.filesample.mapper;

import com.filesample.vo.FileVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    public void insertFileInfo(FileVO vo);
    public String selectFileName(String uri);
}

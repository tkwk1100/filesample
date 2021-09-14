package com.filesample.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.filesample.mapper.FileMapper;
import com.filesample.vo.FileVO;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jdk.jfr.ContentType;

@RestController
public class FileController {
    @Autowired
    FileMapper mapper;

    @PostMapping("/upload")
    public Map<String, Object> postUpload(@RequestPart MultipartFile file) throws Exception{
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        //파일 저장되는 위치
        Path fileLocation = Paths.get("/uploaded");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Calendar c = Calendar.getInstance();
        fileName = c.getTimeInMillis()+"."+fileName.split("\\.")[1];
        //파일이 저장될 폴더 경로에, 받아온 파일 이름을 합치는데
        //모든 os에서 사용 가능하도록 공동 표현삭으로 바꿔주는 역할을 한다.
        Path targetLocation = fileLocation.resolve(fileName);
        //파일 복사 명령 (파라미터로 받아온 파일을 열어서)
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        //스페이스바로 띄워진 공가 모두 없애기
        String uri = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll(" "," ");
        uri = uri.split("\\.")[0];
        FileVO vo = new FileVO();
        vo.setF_name(fileName);
        vo.setF_uri(uri);

        mapper.insertFileInfo(vo);

        resultMap.put("status", true);
        resultMap.put("message", "전송완료");


        return resultMap;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> getDownloadFile(
        @PathVariable String filename, HttpServletRequest request) throws Exception{
        Path fileLocation = Paths.get("/uploaded");

        String real_filename = mapper.selectFileName(filename);
        String ext = real_filename.split("\\.")[1];

        Path filePath = fileLocation.resolve(filename).normalize();

        Resource r = new UrlResource(filePath.toUri());
        String contentType = null;
        contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            if(contentType == null) {

                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=\""+(filename+"."+ext)+"\"")
            .body(r);

    }
}

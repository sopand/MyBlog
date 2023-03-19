package com.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImgService {

    @Value("${file.Upimg}")
    private String path;
    public ModelAndView createBoardImg(MultipartFile file) throws IOException {
        ModelAndView mv=new ModelAndView("jsonView");
        String imgOriginalName = file.getOriginalFilename(); // 입력한 원본 파일의 이름
        String uuid = String.valueOf(UUID.randomUUID()); // toString 보다는 valueOf를 추천 , NPE에러 예방,
        String extension = imgOriginalName.substring(imgOriginalName.lastIndexOf(".")); // 원본파일의 파일확장자
        String savedName = uuid + extension; // 랜덤이름 + 확장자
        File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
        if (!converFile.exists()) {
            converFile.mkdirs();
        }
        file.transferTo(converFile);
        mv.addObject("uploaded",true);
        String imgView="/myblog/"+savedName;
        mv.addObject("url",imgView);
        return mv;
    }
}

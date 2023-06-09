package com.blog.service;

import com.blog.entity.Img;
import com.blog.entity.ImgRepository;
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
    private final ImgRepository imgRepository;
    @Value("${file.Upimg}")
    private String path;

    /**
     * 이미지를 등록해주는 로직,
     * @param file = 사용자가 등록한 이미지의 데이터가 들어있는 객체,
     * @return  = CKEditor에 출력시킬 이미지 데이터가 들어있는 객체
     * @throws IOException
     */
    public ModelAndView createBoardImg(MultipartFile file) throws IOException {
        ModelAndView mv=new ModelAndView("jsonView"); // 등록된 이미지 데이터를 다시 반환해주기 위한 객체
        String imgOriginalName = file.getOriginalFilename(); // 입력한 원본 파일의 이름
        String uuid = String.valueOf(UUID.randomUUID()); // toString 보다는 valueOf를 추천 , NPE에러 예방,
        String extension = imgOriginalName.substring(imgOriginalName.lastIndexOf(".")); // 원본파일의 파일확장자
        String savedName = uuid + extension; // 랜덤이름 + 확장자
        File converFile = new File(path, savedName); // path = 상품 이미지 파일의 저장 경로가 들어있는 프로퍼티 설정값
        if (!converFile.exists()) {
            converFile.mkdirs();
        }
        file.transferTo(converFile);
        String imgView="/myblog/"+savedName;
        mv.addObject("uploaded",true);  // 업로드의 성공여부
        mv.addObject("url",imgView);  // 실제 업로드된 데이터의 이름
        Img img=Img.builder().imgOrigin(imgOriginalName).imgNew(savedName).imgDirectory(imgView).build();
        imgRepository.save(img);
        return mv;
    }
}

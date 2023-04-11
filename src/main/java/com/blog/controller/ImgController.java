package com.blog.controller;

import com.blog.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImgController {

    private final ImgService imgService;

    /**
     * CKEditor에서 이미지를 등록할 경우 DB에 데이터를 입력시키고 다시 입력된 이미지 데이터를 반환해주는 맵핑 ( 게시글 출력시키기 위해 다시 반환 )
     * @param request = 사용자가 추가하려는 이미지 데이터
     * @return = 사용자가 입력시킨 이미지 데이터를 DB에 입력시키고 다시 반환 ( 게시글에 출력시키기 위함 )
     * @throws Exception
     */
    @PostMapping("/boards/img")
    public ModelAndView createBoardImg(MultipartHttpServletRequest request) throws  Exception{
        ModelAndView mv=imgService.createBoardImg(request.getFile("upload"));
        return mv;
    }
}

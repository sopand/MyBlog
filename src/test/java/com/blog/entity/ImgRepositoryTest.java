package com.blog.entity;

import com.blog.config.DataBaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataBaseConfig.class)
@ActiveProfiles("test")
class ImgRepositoryTest {
    @Autowired
    private ImgRepository imgRepository;

    @Test
    void findByImgDirectory() {
        //given
        List<Img> imgList=imgRepository.findAll();
        String imgDirectory=imgList.get(0).getImgDirectory();
        //when
        Img img=imgRepository.findByImgDirectory(imgDirectory);
        //then
        assertThat(imgDirectory).isEqualTo(img.getImgDirectory());
    }

    @Test
    void findByBoard_BoardId(){
        //given
        List<Img> imgList=imgRepository.findAll();
        Long boardId=imgList.get(0).getBoard().getBoardId();
        //when
        List<Img> imgPS=imgRepository.findByBoard_BoardId(boardId);
        //then
        assertThat(imgPS).isNotEmpty();
    }


}
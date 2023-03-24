package com.blog.entity;

import com.blog.config.DataBaseConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataBaseConfig.class)
class ImgRepositoryTest {
    @Autowired
    private ImgRepository imgRepository;

    @Test
    void findByImgDirectory() {
        //given
        String imgDirectory="/myblog/9c6cedc3-9e50-4863-82bf-d60d51f0e4d3.png";
        //when
        Img img=imgRepository.findByImgDirectory(imgDirectory);
        //then
        assertThat(imgDirectory).isEqualTo(img.getImgDirectory());
    }
}
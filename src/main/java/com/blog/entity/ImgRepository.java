package com.blog.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgRepository extends JpaRepository<Img,Long> {


    Img findByImgDirectory(String imgDirectory);
}

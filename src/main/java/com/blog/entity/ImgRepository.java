package com.blog.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img,Long> {


    Img findByImgDirectory(String imgDirectory);

    List<Img> findByBoard_BoardId(Long boardId);
}

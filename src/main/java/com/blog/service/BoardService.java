package com.blog.service;

import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.entity.Board;
import com.blog.entity.BoardRepository;
import com.blog.entity.Img;
import com.blog.entity.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${file.Upimg}")
    private String path;

    private final BoardRepository boardRepository;
    private final ImgRepository imgRepository;

    public static Map<String, Object> findPaging(Page<Board> pagingBoards) {
        Map<String, Object> pagingMap = new HashMap<>();
        int nowPage = pagingBoards.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pagingBoards.getTotalPages());
        pagingMap.put("startPage", startPage);
        pagingMap.put("nowPage", nowPage);
        pagingMap.put("endPage", endPage);
        return pagingMap;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findBoardAll(Pageable page) {
        Page<Board> pagingBoards = boardRepository.findAll(page);
        List<BoardResponse> findBoards = pagingBoards.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingMap = findPaging(pagingBoards);
        pagingMap.put("findBoards", findBoards);
        return pagingMap;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findBoardByCateogry(Pageable page, String boardCateogry) {
        Page<Board> pagingBoards = boardRepository.findByBoardCategory(boardCateogry, page);
        List<BoardResponse> findBoards = pagingBoards.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingMap = findPaging(pagingBoards);
        pagingMap.put("findBoards", findBoards);
        return pagingMap;
    }


    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest) {
        Board board = boardRepository.save(boardRequest.toEntity());
        if (!boardRequest.getImgList().equals("")) {
            List<String> imgList = List.of(boardRequest.getImgList().split(","));
            imgList.stream().forEach(entity -> {
                Img img = imgRepository.findByImgDirectory(entity);
                img.modifyImgBoard(board);
            });
        }
        return new BoardResponse(board);
    }

    @Transactional
    public BoardResponse findBoard(Long boardId) {
        Board boardPS = boardRepository.findByBoardId(boardId);
        boardPS.modifyBoardHit(boardPS.getBoardHit());
        return new BoardResponse(boardPS);
        //throw new IllegalStateException("찾는 게시글이 존재하지 않아요");
    }


    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }


    @Transactional
    public void modifyBoard(BoardRequest boardRequest) {
        Board board = boardRepository.findByBoardId(boardRequest.getBoardId()); // 더티체킹방식으로 업데이트를 진행하기위해 기존의 Board의 정보를 찾아온다.
        List<Img> beforeImgList = imgRepository.findByBoard_BoardId(boardRequest.getBoardId()); // 기존의 Board에 존재하던 이미지의 정보와 새롭게 수정한 Board의 이미지 정도를 비교하기 위해 기존 이미지 정보를 저장
        if (!boardRequest.getImgList().equals("")) { // Request된 board에서 이미지가 존재 할 경우 발동
            List<String> imgList = List.of(boardRequest.getImgList().split(",")); // JS 처리하여 넘긴 Img파일의 Direcotry를 포함한 주소값을 , 단위로 잘라서 반복하기 위함
            imgList.stream().forEach(entity -> {
                beforeImgList.stream().filter(before -> entity.equals(before.getImgDirectory()))
                        .collect(Collectors.toList())
                        .forEach(before -> {
                            beforeImgList.remove(before);// 기존의 이미지 리스트에 있던 이미지와 수정된 HTML 의 이미지 파일이 동일하면 before에서 remove처리 ( 남아 있는 리스트의 값들을 delete시킬 예정 )
                        });
                Img img = imgRepository.findByImgDirectory(entity); //더티 체킹으로 이미지의 BoardId를 넣어주기 위해 이미지파일명으로 DB에서 찾아옴
                img.modifyImgBoard(board); // boardId를 UPDATE
            });

            // 수정된 이미지 파일과equals하여 존재하지 않던 파일의 리스트 ( 현재 HTML 상에는 존재하지 않는 다는 뜻 ) 삭제
        }
        beforeImgList.stream().filter(entity -> beforeImgList.size() != 0).forEach(entity -> { // 기존 이미지에서 바뀌어진 이미지는 remove처리하기 위한 For문대신 Stream, ( 가동 속도에 이점이있다. )
            String deletePath = path + entity.getImgNew();
            File file = new File(deletePath);
            file.delete();
            imgRepository.delete(entity);
        });

        if (!boardRequest.getBoardThumbnail().equals("")) { // Request되어 넘어온 썸네일 이미지의 데이터가 있다면, ( 게시글의 첫번째 사진이 변경 되었을 경우 )
            board.modifyBoardAndImg(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory(), boardRequest.getBoardThumbnail());
            //넘어온 썸네일 이미지 데이터가 있다면 썸네일 까지 포함해서 UPDATE
        } else {
            // 넘어온 썸네일 데이터가 없다면 기본적인 게시글 관련 데이터로 업데이트
            board.modifyBoard(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory());
        }
        if(boardRequest.getImgList().equals("")){ // 게시글에 이미지 파일 자체가 존재하지 않을경우 썸네일 데이터를 공란으로 비워버린다.
            board.isNullBoardThumnail();
        }
    }

}

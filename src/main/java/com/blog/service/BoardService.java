package com.blog.service;

import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.entity.Board;
import com.blog.entity.BoardRepository;
import com.blog.entity.Img;
import com.blog.entity.ImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BoardService {

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
        if (boardRequest.getImgList() != null) {
            String[] imgList = boardRequest.getImgList().split(",");
            for (String list : imgList) {
                Img img = imgRepository.findByImgDirectory(list);
                img.modifyImgBoard(board);
            }
        }
        return new BoardResponse(board);
    }

    @Transactional
    public BoardResponse findBoard(Long boardId) {
        int hit = modifyBoardHit(boardId);
        if (hit == 1) {
            Board boardPS = boardRepository.findByBoardId(boardId);
            return new BoardResponse(boardPS);
        } else {
            throw new IllegalStateException("찾는 게시글이 존재하지 않아요");
        }
    }

    @Transactional
    public int modifyBoardHit(Long boardId) {
        return boardRepository.modifyBoardHit(boardId);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }


    @Transactional
    public void modifyBoard(BoardRequest boardRequest) {
        Board board = boardRepository.findByBoardId(boardRequest.getBoardId()); // 더티체킹방식으로 업데이트를 진행하기위해 기존의 Board의 정보를 찾아온다.
        List<Img> beforeImgList = imgRepository.findByBoard_BoardId(boardRequest.getBoardId()); // 기존의 Board에 존재하던 이미지의 정보와 새롭게 수정한 Board의 이미지 정도를 비교하기 위해 기존 이미지 정보를 저장
        if (boardRequest.getImgList() != null) { // Request된 board에서 Img가 존재 한다면
            String[] imgList = boardRequest.getImgList().split(","); // 
            for (String list : imgList) {
                beforeImgList.stream().filter(entity -> list.equals(entity.getImgDirectory())).collect(Collectors.toList()).forEach(entity -> {
                    beforeImgList.remove(entity);
                });
                beforeImgList.stream().forEach(li ->{
                    System.out.println("이미지리스트 "+li.getImgId());
                });
                Img img = imgRepository.findByImgDirectory(list);
                img.modifyImgBoard(board);
            }
        } else {
            if (beforeImgList.size() != 0) {
                for (Img before : beforeImgList) {
                    imgRepository.delete(before);
                }
            }
        }
        if (boardRequest.getBoardThumbnail() != "") {
            board.modifyBoardAndImg(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory(), boardRequest.getBoardThumbnail());
        } else {
            board.modifyBoard(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory());
        }
    }

}

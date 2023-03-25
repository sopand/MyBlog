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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 게시글 관련된 비즈니스 로직을 담당하는 클래스,
 * imgFolderPath = aaplication.yml에 설정해놓은 Imgfolder의 경로 값이다.
 * 게시글과 이미지는 입력,수정,삭제등에 있어서 같이 작동하는 경우가 많아 2개의 repository를 생성자 주입방식으로 의존성 주입,
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${file.Upimg}")
    private String imgFolderPath;

    private final BoardRepository boardRepository;
    private final ImgRepository imgRepository;

    /**
     * setPagingData  = 중복적으로 사용되는 페이징처리 관련 로직을 분할시켜 하나로 돌려 쓰기위해 제작.
     * @param pagingBoards = 페이징처리된 DB의 데이터로 View에 출력시켜줄 데이터를 만들기 위해서, 받아오는 인자값,
     * @return  View에 필요한 페이징 관련 데이터를 가공시켜 ,Map에 담아서 리턴시켜준다.
     */
    public static Map<String, Object> setPagingData(Page<Board> pagingBoards) {
        Map<String, Object> pagingContent = new HashMap<>();
        int nowPage = pagingBoards.getPageable().getPageNumber() + 1; // 현재 페이지에 대한 값으로 pageable의 시작페이지가 0이기 때문에 +1 시켜 1부터 시작하게 만든다.
        int startPage = Math.max(nowPage - 4, 1); // View에 출력될 최소페이지설정, 최소 값은 1이고 Now(현재 페이지)값 - 4한값이 더 크다면 시작 페이지 값이 변경된다.
        int endPage = Math.min(nowPage + 5, pagingBoards.getTotalPages());  // View에 보이게 될 최대 페이지 사이즈,
        pagingContent.put("startPage", startPage);
        pagingContent.put("nowPage", nowPage);
        pagingContent.put("endPage", endPage);
        return pagingContent;
    }


    /**
     * findAllBoars = 전체 게시글을 보여주기 위한 로직,
     * @param page = Paging관련 데이터가 들어있는 객체,
     * @return // 위의 페이징 관련 데이터 처리를 마친 Map 과 BoardResponse객체를 합쳐
     * Map으로 데이터를 리턴시켜준다. ( 여러 자료형의 데이터가 있기때문에 한번에 반환하기 위해 Map으로 선언)
     */
    @Transactional(readOnly = true)
    public Map<String, Object> findAllBoards(Pageable page) {
        Page<Board> pagingBoardList = boardRepository.findAllBoardList(page);
        List<BoardResponse> pagingBoardResponse = pagingBoardList.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingContent = setPagingData(pagingBoardList);
        pagingContent.put("pagingBoardResponse", pagingBoardResponse);
        return pagingContent;
    }

    /**
     * findBoardByCateogry = 카테고리를 선택해서 게시판 접근시 카테고리별 데이터를 찾아주는 로직,
     * @param page = 페이징 처리와 관련된 데이터를 가지고 있는 객체,
     * @param boardCateogry = 클라이언트가 선택한 Category의 정보를 가져온다.
     * @return = 해당 클라이언트가 선택한 카테고리와 페이징 정보로 찾아온 DB데이터를 setPagingData로 가공시킨 값과 게시글정보를 Map에 담아서 리턴
     */

    @Transactional(readOnly = true)
    public Map<String, Object> findBoardByCateogry(Pageable page, String boardCateogry) {
        Page<Board> pagingBoardList = boardRepository.findByBoardCategory(boardCateogry, page);
        List<BoardResponse> pagingBoardResponse = pagingBoardList.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingContent = setPagingData(pagingBoardList);
        pagingContent.put("pagingBoardResponse", pagingBoardResponse);
        return pagingContent;
    }

    /**
     * createBoard = 클라이언트의 게시글 입력 데이터를 받아와 DB에 게시글 생성을 해주는 로직,
     * @param boardRequest = 클라이언트가 입력한 BOARD의 데이터가 들어있는 Request객체,
     * @return  = INSERT가 성공적으로 이루어졌다면, 저장된 데이터를 다시 반환시켜 성공 여부를 확인한다.
     */
    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest) {
        Board getInsertBoardData = boardRepository.save(boardRequest.toEntity());
        if (!boardRequest.getImgList().equals("")) {
            List<String> imgList = List.of(boardRequest.getImgList().split(","));
            imgList.stream().forEach(entity -> {
                Img img = imgRepository.findByImgDirectory(entity);
                img.modifyImgBoard(getInsertBoardData);
            });
        }
        return new BoardResponse(getInsertBoardData);
    }

    /**
     * findBoard = 게시판 리스트에서 게시글 클릭시 게시글의 상세내용을 보여주기 위한 로직,
     * @param boardId = 클라이언트가 보기위해 클릭한 게시글의 고유번호 PK를 받아옴.
     * @return = 고유번호를 기반으로 찾아온 Board에 DB데이터를 Entity객체에서 DTO 객체로 변환시킨 값,
     */
    @Transactional
    public BoardResponse findBoard(Long boardId) {
        Board getBoard = boardRepository.findByBoardId(boardId);
        getBoard.modifyBoardHit(getBoard.getBoardHit());
        return new BoardResponse(getBoard);
    }

    /**
     * deleteBoard = 게시글을 삭제처리하기 위한 로직,
     * @param boardId = 게시글 삭제처리를 하려는 게시글의 고유번호 PK를 받아온다.
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }


    /**
     * modifyBoard = 게시글을 수정처리하기 위한 로직,
     * @param boardRequest = 게시글 수정페이지에서 입력한 변경된 데이터들의 정보가 담겨있는 객체,
     */
    @Transactional
    public void modifyBoard(BoardRequest boardRequest) {
        Board getBoard = boardRepository.findByBoardId(boardRequest.getBoardId()); // 더티체킹방식으로 업데이트를 진행하기위해 기존의 Board의 정보를 찾아온다.
        List<Img> beforeImgList = imgRepository.findByBoard_BoardId(boardRequest.getBoardId()); // 기존의 Board에 존재하던 이미지의 정보와 새롭게 수정한 Board의 이미지 정도를 비교하기 위해 기존 이미지 정보를 저장
        if (!boardRequest.getImgList().equals("")) { // Request된 board에서 이미지가 존재 할 경우 발동
            List<String> nowImgList = List.of(boardRequest.getImgList().split(",")); // JS 처리하여 넘긴 Img파일의 Direcotry를 포함한 주소값을 , 단위로 잘라서 반복하기 위함
            nowImgList.stream().forEach(entity -> {
                beforeImgList.stream().filter(before -> entity.equals(before.getImgDirectory()))
                        .collect(Collectors.toList())
                        .forEach(before -> {
                            beforeImgList.remove(before);// 기존의 이미지 리스트에 있던 이미지와 수정된 HTML 의 이미지 파일이 동일하면 before에서 remove처리 ( 남아 있는 리스트의 값들을 delete시킬 예정 )
                        });
                Img img = imgRepository.findByImgDirectory(entity); //더티 체킹으로 이미지의 BoardId를 넣어주기 위해 이미지파일명으로 DB에서 찾아옴
                img.modifyImgBoard(getBoard); // boardId를 UPDATE
            });

            // 수정된 이미지 파일과equals하여 존재하지 않던 파일의 리스트 ( 현재 HTML 상에는 존재하지 않는 다는 뜻 ) 삭제
        }
        beforeImgList.stream().filter(entity -> beforeImgList.size() != 0).forEach(entity -> { // 기존 이미지에서 바뀌어진 이미지는 remove처리하기 위한 For문대신 Stream, ( 가동 속도에 이점이있다. )
            String deletePath = imgFolderPath + entity.getImgNew();
            File file = new File(deletePath);
            file.delete();
            imgRepository.delete(entity);
        });

        if (!boardRequest.getBoardThumbnail().equals("")) { // Request되어 넘어온 썸네일 이미지의 데이터가 있다면, ( 게시글의 첫번째 사진이 변경 되었을 경우 )
            getBoard.modifyBoardAndImg(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory(), boardRequest.getBoardThumbnail());
            //넘어온 썸네일 이미지 데이터가 있다면 썸네일 까지 포함해서 UPDATE
        } else {
            // 넘어온 썸네일 데이터가 없다면 기본적인 게시글 관련 데이터로 업데이트
            getBoard.modifyBoard(boardRequest.getBoardName(), boardRequest.getBoardContent(), boardRequest.getBoardCategory());
        }
        if(boardRequest.getImgList().equals("")){ // 게시글에 이미지 파일 자체가 존재하지 않을경우 썸네일 데이터를 공란으로 비워버린다.
            getBoard.isNullBoardThumnail();
        }
    }

}

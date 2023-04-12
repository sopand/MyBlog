package com.blog.service;

import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.dto.PagingList;
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
import java.util.List;

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
     * createBoard = 클라이언트의 게시글 입력 데이터를 받아와 DB에 게시글 생성을 해주는 로직,
     *
     * @param boardRequest = 클라이언트가 입력한 BOARD의 데이터가 들어있는 Request객체,
     * @return = INSERT가 성공적으로 이루어졌다면, 저장된 데이터를 다시 반환시켜 성공 여부를 확인한다.
     */
    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest) {
        Board getInsertBoardData = boardRepository.save(boardRequest.toEntity());
        if (!boardRequest.getImgList().equals("")) {
            List<String> imgList = List.of(boardRequest.getImgList().split(","));
            imgList.stream().forEach(nowImg -> {
                updateImgDirectoryAndBoardId(nowImg,getInsertBoardData);
            });
        }
        return new BoardResponse(getInsertBoardData);
    }



    /**
     * findBoard = 게시판 리스트에서 게시글 클릭시 게시글의 상세내용을 보여주기 위한 로직,
     *
     * @param boardId = 클라이언트가 보기위해 클릭한 게시글의 고유번호 PK를 받아옴.
     * @return = 고유번호를 기반으로 찾아온 Board에 DB데이터를 Entity객체에서 DTO 객체로 변환시킨 값,
     */
    @Transactional
    public BoardResponse findBoard(Long boardId) {
        Board getBoard = boardRepository.findByBoardId(boardId);
        getBoard.modifyBoardHit();
        return new BoardResponse(getBoard);
    }

    /**
     * deleteBoard = 게시글을 삭제처리하기 위한 로직,
     *
     * @param boardId = 게시글 삭제처리를 하려는 게시글의 고유번호 PK를 받아온다.
     */
    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }


    /**
     * 게시글 수정파트의 복잡도를 낮추기위해 기존 이미지의 삭제처리를 담당하는 기능을 분리
     * 해당 로직은 기존 이미지와 현재 이미지정보를 매개변수로 받아 기존이미지가 남아 있다면 배열에서 remove처리를 하고 없어진 이미지라면 remove하지않고 남겨둔다.
     * 그리고 modifyBoard 하단의 로직에서 남아있는 배열에 대한 삭제처리를 진행한다.
     * @param nowImgList = 현재의 이미지 리스트
     * @param beforeImgList = 기존에 존재하던 이미지리스트
     * @param getBoard = 새롭게 추가된 현재의 이미지에 board FK를 입력
     */
    @Transactional
    public void deleteBeforeImgList(List<String> nowImgList,List<Img> beforeImgList,Board getBoard){
        nowImgList.stream().forEach(nowImg -> {
            beforeImgList.stream().filter(beforeImg -> beforeImg.getImgDirectory().equals(nowImg)) // 기존 이미지 파일과 현재 이미지파일에서 같은 이미지가 존재한다면
                    .toList()
                    .forEach(beforeImg -> {
                        beforeImgList.remove(beforeImg);// 기존의 이미지 리스트에 있던 이미지와 수정된 HTML 의 이미지 파일이 동일하면 before에서 remove처리 ( 남아 있는 리스트의 값들을 delete시킬 예정 )
                    });
            updateImgDirectoryAndBoardId(nowImg,getBoard);
        });
    }

    /**
     * 게시글 생성과 수정에서 중복으로 사용되는 이미지의데이터와 BoardId 데이터 수정파트를 분할
     * @param imgDirectoryPath = DB에 넣을 이미지의 경로
     * @param getBoardIdEntity = BoardId를 넣기위한 BoardEntity
     */
    @Transactional
    public void updateImgDirectoryAndBoardId(String imgDirectoryPath,Board getBoardIdEntity){
        Img img = imgRepository.findByImgDirectory(imgDirectoryPath);
        img.modifyImgBoard(getBoardIdEntity);
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
            deleteBeforeImgList(nowImgList,beforeImgList,getBoard);
            getBoard.modifyBoardAndThubmnail(boardRequest);  // 해당 메서드 안에서 썸네일 사진의 유무를 판단하여 썸네일까지 업데이트를 할지 아니면 일반 board에 대한 정보만 업데이트할지 결정.
        }else{
            getBoard.isNullBoardThumnail(); // 게시글에 이미지 파일 자체가 존재하지 않을경우 썸네일 데이터를 공란으로 비워버린다.
        }
        beforeImgList.stream().filter(deleteEntity -> beforeImgList.size() != 0).forEach(deleteEntity -> { // 기존 이미지에서 바뀌어진 이미지는 remove처리하기 위한 For문대신 Stream, ( 가동 속도에 이점이있다. )
            deleteImg(deleteEntity); //코드의 복잡도를 낮추기위해 delete 파트를 메서드로 분할
        });

    }

    /**
     * Img의 이미지 삭제처리를 하기위한 로직으로 modify파트의 복잡도를 줄이기 위해 분할처리
     *
     * @param imgEntity = 삭제하려는 이미지 Entity
     */
    @Transactional
    public void deleteImg(Img imgEntity) {
        String deletePath = imgFolderPath + imgEntity.getImgNew();
        File file = new File(deletePath);
        file.delete();
        imgRepository.delete(imgEntity);
    }

    /**
     * findAllBoars = 전체 게시글을 보여주기 위한 로직,
     *
     * @param page = Paging관련 데이터가 들어있는 객체,
     * @return // 위의 페이징 관련 데이터 처리를 마친 Map 과 BoardResponse객체를 합쳐
     * Map을 리턴값으로 하려다가 PagingList라는 하나의페이징 전용 객체를 만들어 페이징처리 데이터를 리턴.
     */


    @Transactional(readOnly = true)
    public PagingList findAllBoards(Pageable page) {
        Page<Board> pagingBoardList = boardRepository.findAll(page);
        List<BoardResponse> pagingBoardResponse = setPagingBoardResponse(pagingBoardList);
        return setPagingData(pagingBoardList,pagingBoardResponse);
    }

    /**
     * @param page         = 페이징 처리에 관련된 데이터들이 있는 객체
     * @param boardRequest = 사용자가 검색하기 위해 입력한 검색어와, Category정보가 들어있다.
     * @return = 페이징 처리가된 데이터들을 저장해놓은 객체를 리턴해준다.
     */
    @Transactional(readOnly = true)
    public PagingList findAllBoardByCateogrySearch(Pageable page, BoardRequest boardRequest) {
        Page<Board> pagingBoardList;
        if (boardRequest.getSearchText() == null) {
            pagingBoardList = boardRepository.findByBoardCategory(boardRequest.getBoardCategory(), page);
        } else {
            pagingBoardList = boardRepository.findSearchBoard(boardRequest, page);
        }
        List<BoardResponse> pagingBoardResponse = setPagingBoardResponse(pagingBoardList);
        PagingList getPagingContent = setPagingData(pagingBoardList, pagingBoardResponse);
        getPagingContent.setSearchText(boardRequest.getSearchText());
        return getPagingContent;
    }

    /**
     * 카테고리가 없는 ( 전체보기에서 검색 or 정렬 선택 ) 정렬 or 검색시 작동하는 로직,
     *
     * @param page         = 페이징할 기준이 되는 데이터들이 들어있는 객체,
     * @param boardRequest = 사용자가 입력한 검색어의 정보가 들어있다. ( 이 로직은 Category가 없을때만 작동하기에 Category정보는 없다. )
     * @return
     */

    @Transactional(readOnly = true)
    public PagingList findAllBoardByNoCategorySearch(Pageable page, BoardRequest boardRequest) {
        Page<Board> pagingBoardList;
        if (boardRequest.getSearchText() != null) {
            pagingBoardList = boardRepository.findSearchBoardNoBoardCateogry(boardRequest, page);
        } else {
            pagingBoardList = boardRepository.findAll(page);
        }
        List<BoardResponse> pagingBoardResponse = setPagingBoardResponse(pagingBoardList);
        return setPagingData(pagingBoardList, pagingBoardResponse);
    }

    /**
     * 페이징 처리에 필요한 DB데이터를 가져온뒤 가공하는 부분, 중복되는 코드가 많아 하나의 로직으로 분리시킴.
     *
     * @param pagingEntity   = 페이징에 관련된 정보와 페이징 처리된 SELECT 데이터를 가지고 있는 객체,
     * @param pagingResponse = 실제 페이징된 데이터들을 가지고 있는 객체
     * @return = 페이징처리를 위해 생성한 PagingList객체에 데이터를 담아서 한번에 리턴해준다.
     */
    public static PagingList setPagingData(Page<?> pagingEntity, List<?> pagingResponse) {
        int nowPage = pagingEntity.getPageable().getPageNumber() + 1; // 현재 페이지에 대한 값으로 pageable의 시작페이지가 0이기 때문에 +1 시켜 1부터 시작하게 만든다.
        int startPage = Math.max(nowPage - 4, 1); // View에 출력될 최소페이지설정, 최소 값은 1이고 Now(현재 페이지)값 - 4한값이 더 크다면 시작 페이지 값이 변경된다.
        int endPage = Math.min(nowPage + 5, pagingEntity.getTotalPages());  // View에 보이게 될 최대 페이지 사이즈,
        return PagingList.builder().pagingList(pagingResponse).nowPage(nowPage).startPage(startPage).endPage(endPage).build();
    }

    /**
     * 페이징 처리된 Page객체에서 응답을 위한 객체 Response로 변환시켜 추출하는 작업을 하는 메서드
     * @param pagingBoardList = 페이징된 데이터를 가진 객체
     * @return = Page<Board>에서 Board를 추출 -> Response객체로 변환 하여 리턴
     */
    public List<BoardResponse> setPagingBoardResponse(Page<Board> pagingBoardList){

        return pagingBoardList.stream().filter(entity -> pagingBoardList != null).map(BoardResponse::new).toList();
    }

}

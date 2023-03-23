$(function () {
    let reviewName = $("input[name=reviewName]").val();
    let loginId=$("input[name=boardName]").val();
    let boardId = $("input[name=boardId]").val();
    $.ajax({
        url: "/boards/review",
        method: "GET",
        data: {
            boardId: boardId,
        },
        success: function (data) {
            const list = data.review;
            const nowPage = data.nowPage;
            const endPage = data.endPage;
            let html = findReview(list, nowPage, endPage);
            $(".review_box").html(html);
        },
        error: function () {
            alert("리뷰 불러오기 실패");
        }
    });

    $(".review_add_btn").click(function () {
        if (reviewName == null) {
            alert("로그인을 하셔야 작성할수 있어용!!");
            return false;
        }
        let boardId = $("input[name=boardId]").val();
        let reviewContent = $(".review_area").val();
        $.ajax({
            url: "/boards/review",
            method: "POST",
            data: {
                boardId: boardId,
                reviewName: reviewName,
                reviewContent: reviewContent
            },
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("리뷰 생성 실패");
            }
        });

    });

    $(document).on('click', '.re_review', function () {
        const chk = $(this).val();
        if ($(this).text() == '대댓글') {
            $(this).text("대댓글입력중");
            let html = `
            <form class="re_review_form ${chk}">                
                <textarea name="reviewContent" class="re_review_area area${chk}"></textarea>
                <button type="button" class="re_review_add_btn" value="${chk}">대댓글 등록</button>
            </form>
        `;
            $("#" + chk).after(html);
        } else {
            $("."+chk).remove();
            $(this).text("대댓글");
        }
    });
    $(document).on('click', '.re_review_add_btn', function () {
        let reviewParent =$(this).val();
        if (reviewName == null) {
            alert("로그인을 하셔야 작성할수 있어용!!");
            return false;
        }
        let boardId = $("input[name=boardId]").val();
        let reviewContent = $(".area"+reviewParent).val();
        $.ajax({
            url: "/boards/review",
            method: "POST",
            data: {
                boardId: boardId,
                reviewName: reviewName,
                reviewContent: reviewContent,
                reviewParent:reviewParent
            },
            success: function (data) {
                alert(data);
                location.reload();

            },
            error: function () {
                alert("리뷰 생성 실패");
            }
        });

    });

    $(document).on('click', '.re_reviewList', function () {
        let reviewParent=$(this).val();
        let listopenChk=$(this).text();
        if(listopenChk!='댓글 축소') {
            $(this).text("댓글 축소");
            $.ajax({
                url: "/boards/review/parent",
                method: "GET",
                data: {
                    reviewParent: reviewParent
                },
                success: function (data) {

                    let list = data;
                    let html = "";
                    console.log(data);
                    $(list).each(function (index, item) {
                        if(item.reviewDeep<2) {
                            console.log(item.reviewDeep);
                            html += `
                        <div class="review_content2 ${reviewParent}re" id="${item.reviewId}">
                            <div class="review_top2">
                                <span class="review_name2">작성자 : ${item.reviewName}</span>
                                <span class="review_date2">작성 날짜 : ${item.reviewDate}</span>
                            </div>
                            <div class="review_center2">
                                <span class="review_text2">${item.reviewContent}</span>
                            </div>
                            <div class="review_bot2">
                                <button type="button" class="re_review" value="${item.reviewId}">대댓글</button>                   
                    `;
                            if (item.reviewName == loginId) {
                                html += `<button type="button" class="review_delete" value="${item.reviewId}">삭제</button>`;
                            }
                            if (item.reviewGroupNo != 0) {
                                html += `<button class="re_reviewList" value="${item.reviewId}">댓글 더 보기...(${item.reviewGroupNo})</button>`;
                            }
                            html += "</div></div>";
                        }else{
                            html += `
                        <div class="review_content3 ${reviewParent}re" id="${item.reviewId}">
                            <div class="review_top3">
                                <span class="review_name3">작성자 : ${item.reviewName}</span>
                                <span class="review_date3">작성 날짜 : ${item.reviewDate}</span>
                            </div>
                            <div class="review_center3">
                                <span class="review_text3">${item.reviewContent}</span>
                            </div>
                            <div class="review_bot3">
                                <button type="button" class="re_review" value="${item.reviewId}">대댓글</button>                   
                    `;
                            if (item.reviewName == loginId) {
                                html += `<button type="button" class="review_delete" value="${item.reviewId}">삭제</button>`;
                            }
                            if (item.reviewGroupNo != 0) {
                                html += `<button class="re_reviewList" value="${item.reviewId}">댓글 더 보기...(${item.reviewGroupNo})</button>`;
                            }
                            html += "</div></div>";
                        }
                    });
                    $("#" + reviewParent).after(html);
                },
                error: function () {

                    alert("삭제에 실패했어요!@!");
                }
            });
        }else{
            let listlength=$("."+reviewParent+"re").length;
            $("."+reviewParent+"re").remove();
            $(this).text("댓글 더 보기...("+listlength+")");

        }

    });

    $(document).on('click', '.review_delete', function () {
        let reviewId = $(this).val();
        $.ajax({
            url: "/boards/review",
            method: "DELETE",
            data: {
                reviewId: reviewId
            },
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("삭제에 실패했어요!@!");
            }
        });
    });
    $(document).on('click', '.nextPage', function () {
        let page = $(this).text() - 1;
        $.ajax({
            url: "/boards/review",
            method: "GET",
            data: {
                boardId: boardId,
                page: page
            },
            success: function (data) {
                const list = data.review;
                const nowPage = data.nowPage;
                const endPage = data.endPage;
                let html = findReview(list, nowPage, endPage);
                $(".review_box").html(html);

            },
            error: function () {
                alert("다음 페이지 불러오기 실패!!");
            }
        });
    });
    $(document).on('click', '.modifyReview', function () {
        const chk = $(this).val();
        if ($(this).text() == '수정') {
            $(this).text("수정진행중");
            let html = `
            <form class="modifyReview_form modify_${chk}">                
                <textarea name="reviewContent" class="modifyArea modifyarea_${chk}"></textarea>
                <button type="button" class="modifyReviewBtn" value="${chk}">댓글 수정</button>
            </form>
        `;
            $("#" + chk).after(html);
        } else {
            $(".modify_"+chk).remove();
            $(this).text("수정");
        }
        let page = $(this).text() - 1;
    });

    $(document).on('click', '.modifyReviewBtn', function () {
        const reviewId=$(this).val();
        const reviewContent=$(".modifyarea_"+reviewId).val();
        $.ajax({
            url: "/boards/review",
            method: "PUT",
            data: {
                reviewId: reviewId,
                reviewContent: reviewContent
            },
            success: function (data) {
                if(data!=0){
                    alert("댓글 수정 완료~~!!");
                    location.reload();
                }else{
                    alert("댓글 수정 이상발생");
                }

            },
            error: function () {
                alert("내부 서버에서 이상이 발생했어요@!!!");
            }
        });
    });


    function findReview(list, nowPage, endPage) {
        let html = "";
        $(list).each(function (index, item) {
            if (item.reviewParent == null) {
                html += `
                        <div class="review_content" id="${item.reviewId}">
                            <div class="review_top">
                                <span class="review_name">작성자 : ${item.reviewName}</span>
                                <span class="review_date">작성 날짜 : ${item.reviewDate}</span>
                            </div>
                            <div class="review_center">
                                <span class="review_text">${item.reviewContent}</span>
                            </div>
                            <div class="review_bot">
                                <button type="button" class="re_review" value="${item.reviewId}">대댓글</button>                                                              
                    `;

                if(item.reviewName==loginId){
                    html+=`<button type="button" class="review_delete" value="${item.reviewId}">삭제</button>
                           <button type="button" class="modifyReview" value="${item.reviewId}">수정</button>`;
                }
                if (item.reviewGroupNo != 0) {
                    html += `<button class="re_reviewList" value="${item.reviewId}">댓글 더 보기...(${item.reviewGroupNo})</button>`;
                }
                html += "</div></div>";
            }
        });

        html += `<div class="btnbox">  `;
        for (let i = 1; i <= endPage; i++) {
            if (i == nowPage) {
                html += `<p class="nowPage">${i}</p>`;
            } else {
                html += `<a href="javascript:void(0)" class="nextPage">${i}</a>`;
            }
        }
        html += "</div>";
        return html;
    }
});
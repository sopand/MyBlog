$(function () {
    let reviewName = $("input[name=reviewName]").val();
    let boardId = $("input[name=boardId]").val();
    $.ajax({
        url: "/boards/review",
        method: "GET",
        data: {
            boardId: boardId,
        },
        success: function (data) {
            const list = data;
            console.log(data);
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
                <button type="button" class="review_delete" value="${item.reviewId}">삭제</button>                   
                    `;

                    if (item.reviewGroupNo != 0) {
                        html += `<span>더보기(${item.reviewGroupNo})</span>`;
                    }

                    html += `    
                </div>             
            </div>
            `;
                }
            });
            $(".review_box").append(html);

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
            },
            error: function () {
                alert("리뷰 생성 실패");
            }
        });

    });

    $(document).on('click', '.re_review', function () {
        alert("asdsad");
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
});
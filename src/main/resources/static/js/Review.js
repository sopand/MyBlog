$(function () {
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
                console.log(item.boardId);
                if (item.reviewParent == null) {
                    html += `
                 <div class="review_content">
                <div class="review_top">
                    <span class="review_name">작성자 : ${item.boardId}</span>
                    <span class="review_date">작성 날짜 : ${item.reviewDate}</span>
                </div>
                <div class="review_center">
                    <span class="review_text">${item.reviewContent}</span>
                </div>
                <div class="review_bot">
                    <span>삭제/대댓글달기</span>
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
            alert("리뷰 생성 실패");
        }
    });

    $(".review_add_btn").click(function () {
        let reviewName = $("input[name=reviewName]").val();
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
});
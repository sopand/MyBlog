$(function(){
    $(".review_add_btn").click(function (){
        let reviewName=$("input[name=reviewName]").val();
        let reviewContent=$(".review_area").val();
        $.ajax({
           url:"/boards/review",
           method:"POST",
           data:{
               reviewName:reviewName,
               reviewContent:reviewContent
           },
            success : function (data){
                alert(data);
            },
            error : function (){
               alert("리뷰 생성 실패");
            }
        });

    });
});
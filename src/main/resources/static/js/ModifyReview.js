
$(function () {
    simpleEdit.setData(board.boardContent);
    $(".boardModifyBtn").click(function () {
        $("input[name=boardThumbnail]").val(UploadURL);
        $(".boardadd_main_box").submit();
    });

});

$(function () {
    simpleEdit.setData(board.boardContent);

    $(".boardModifyBtn").click(function () {
        const data = simpleEdit.getData();
        const str = (data.match(/(?<=\<img src\=\")(.*?)(?=\"\>)/g));
        if (str != null) {
            if (str[0] == UploadURL) {
                $("input[name=boardThumbnail]").val(UploadURL);
            }
        }else{
            $("input[name=boardThumbnail]").val("");
        }
        $("input[name=imgList]").val(str);
        $(".boardadd_main_box").submit();
    });

});

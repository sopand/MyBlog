
$(function () {
    simpleEdit.setData(board.boardContent);

    $(".boardModifyBtn").click(function () {
        const data=simpleEdit.getData();
        let chk=[];
        chk.push(data.match(/(?<=\<img src\=\")(.*?)(?=\"\>)/g));

        $("input[name=boardThumbnail]").val(UploadURL);
        $(".boardadd_main_box").submit();
    });

});

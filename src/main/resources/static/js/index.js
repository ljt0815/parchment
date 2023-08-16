function calcPercent(loadingBook){
    let imgBox = $(loadingBook.children(".thumbnail-box"));
    let data = {
        uuid : $(loadingBook.children("input")).val()
    }
    let rtn = false;
    $.ajax({
        url: "/book/getConvertProgress",
        data: JSON.stringify(data),
        async: false,
        contentType: "application/json; charset=utf-8",
        method: "POST",
        dataType: "json"
    }).done(function(resp){
        let convertedPage = resp.data.convertedPage;
        let pageTotal = resp.data.pageTotal;
        let percent = Math.floor(convertedPage / pageTotal * 100);
        if (percent !== 100) {
            $(imgBox.children(".loading-percent").children("#percent-lbl")).text(percent + "%");
            rtn = true;
        }
    }).fail(function(xhr, status, errorThrown){
        console.log(errorThrown + status);
    });
    return rtn;
}
function intervalControl(ele) {
    $(ele).parent().parent().children(".book-thumbnail").css("opacity", "0.2")
    let loadingBook = $(ele).parent().parent().parent();
    $(loadingBook).css("cursor", "default");
    let interval = setInterval(function(){
            let result = calcPercent(loadingBook);
            if (!result) {
                clearInterval(interval);
                $(ele).parent().parent().children(".book-thumbnail").css("opacity", "1");
                $(ele).parent().parent().children(".loading-percent").css("display", "none");
                $(ele).parent().parent().children(".loading").css("display", "none");
                $(loadingBook).css("cursor", "pointer");
            }
        }
        , 300);
}
$(function() {
    let isDropped = false;
    let targetUuid;
    let targetName;
    function dropMenu(e) {
        e.preventDefault();
        if ($(e.target).parents(".book").length >= 1) {
            $("#dropdown-menu2").css({"left":e.clientX, "top":(e.clientY + window.scrollY)});
            $("#dropdown-menu2").show();
            isDropped = true;
            let targetBook = $(e.target).closest(".book");
            targetUuid = targetBook.children("input").val();
            targetName = targetBook.children(".book-lbl").text();
            console.log(targetUuid + "," + targetName);
        }
    }
    $(".book").click(function(){
        location.href="/book/viewer/" + $(this).children("input").val();
    });
    $("body").contextmenu(function(e) {
        dropMenu(e);
    });
    $("body").on("touchstart", function(e) {
        let timer = setTimeout(function(){
            dropMenu(e);
        }, 300);
        $("body").on("touchend", function(e) {
            clearTimeout(timer);
        })
    });
    $("body").click(function(e) {
        if (isDropped == true && !($(e.target).hasClass("dropdown-menu")))
            $("#dropdown-menu2").hide();
    });
    $("#book-rename").click(function() {
        $("#modal").modal("show");
        $("#myModalLabel").text("책 이름 변경");
        $("#modal-lbl").children().remove();
        $("#modal-lbl").append('<input type="text" id="rename-bookName" placeholder="'+targetName+'" class="form-control">');
        $("#btn_ok").text("변경");
    });
    $("#book-delete").click(function() {
        $("#modal").modal("show");
        $("#myModalLabel").text("책 삭제");
        $("#modal-lbl").children().remove();
        $("#modal-lbl").append('<p>정말로 책을 삭제하시겠습니까?</p>');
        $("#btn_ok").text("삭제");
    });

})
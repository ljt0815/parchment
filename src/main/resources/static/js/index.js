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
    $(".book").click(function(){
        location.href="/book/viewer/" + $(this).children("input").val();
    })
})
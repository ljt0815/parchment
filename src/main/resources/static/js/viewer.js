let navToggle = false;
let arrowToggle = false;
function getPagePath(flag, type) {
    let data = {
        flag,
        type,
        uuid : $("#uuid").val()
    }
    $.ajax({
        url: "/book/getPagePath",
        data: JSON.stringify(data),
        async: true,
        contentType: "application/json; charset=utf-8",
        method: "POST",
        dataType: "json"
    }).done(function(resp){
        console.log(resp)
        $("#page1").attr("src", resp.data.page1Path);
        if (type == "pc")
            $("#page2").attr("src", resp.data.page2Path);
    }).fail(function(xhr, status, errorThrown){
        console.log(errorThrown + status);
    });
}
function getNextPage() {
    if (window.innerWidth > 1200)
        getPagePath("next","pc");
    else
        getPagePath("next","mobile");
}
function getPreviousPage() {
    if (window.innerWidth > 1200)
        getPagePath("previous","pc")
    else
        getPagePath("previous", "mobile");
}
$(function() {
    if (window.innerWidth > 1200)
        getPagePath("","pc");
    else
        getPagePath("","mobile");
    let nav = $("nav");
    nav.addClass("fixed-top");
    $(".navbar").css("display", "none");
    $("body").click(function(e) {
        let target = $(e.target);
        if (target.parents("#page").length >= 1 || target.parents(".navbar").length >= 1) {
            // 컨테이너 내부 클릭했을때
            if (target.hasClass("arrowBox") || target.hasClass("fa"))
                console.log("hasclass('arrowBox')");
            else {
                let arrows = $("#page div i");
                if (arrowToggle === false) {
                    arrows.removeClass("animate__animated animate__fadeOut animate__faster");
                    arrows.addClass("animate__animated animate__fadeIn animate__faster");
                    arrows.css("display","inline-block");
                    arrows.on("animationend", function() {
                        arrows.removeClass("animate__animated animate__fadeIn animate__faster");
                        arrows.css("display", "inline-block");
                    });
                    arrowToggle = true;
                } else {
                    arrows.removeClass("animate__animated animate__fadeIn animate__faster");
                    arrows.addClass("animate__animated animate__fadeOut animate__faster");
                    arrows.on("animationend", function() {
                        arrows.removeClass("animate__animated animate__fadeOut animate__faster");
                        arrows.css("display", "none");
                    });
                    arrowToggle = false;
                }
            }
        } else if (navToggle === false) {
            // 그 외는 네베게이션바 토글
            nav.removeClass("animate__fadeOutUp");
            nav.addClass("animate__animated animate__fadeInDown");
            nav.css("display","flex");
            nav.on("animationend", function() {
                nav.removeClass("animate__animated animate__fadeInDown");
                nav.css("display","flex");
            });
            navToggle = true;
        } else {
            nav.removeClass("animate__fadeInDown");
            nav.addClass("animate__animated animate__fadeOutUp");
            nav.on("animationend", function() {
                nav.removeClass("animate__animated animate__fadeOutUp");
                nav.css("display","none");
            });
            navToggle = false;
        }
    });
    $("#btn_next").click(function(){
        getNextPage();
    });
    $("#btn_previous").click(function(){
        getPreviousPage();
    });

    // 모바일 화면조정
    let page1 = $("#page1")[0];
    // 1. ResizeObserver 객체 만들기
    const observer = new ResizeObserver(entries => {
        for (let entry of entries) {
            const {width, height} = entry.contentRect;
            if (window.innerWidth > 1200) {
                if (width > ((window.innerWidth * 0.95) - 48) / 2) {
                    let convertWidth = ((window.innerWidth * 0.95) - 48) / 2;
                    $("#page1").width(convertWidth);
                    $("#page2").width(convertWidth);
                }
            } else {
                if (width > window.innerWidth * 0.95)
                    $("#page1").width(window.innerWidth * 0.95);
                if (height > window.innerHeight * 0.95)
                    $("#page1").height(window.innerHeight * 0.95);
            }
        }
    });
    // 2. 감지할 요소 추가하기
    observer.observe(page1);
    $("#page1").height(window.innerHeight * 0.98);
    $("#page2").height(window.innerHeight * 0.98);
    // 키보드이벤트
    $(document).keydown(function(e) {
        if ( e.keyCode == 39 || e.which == 39 ) {
            console.log("asdf");
            getNextPage();
        } else if ( e.keyCode == 37 || e.which == 37 ) {
            console.log("qwer");
            getPreviousPage();
        }
    });
});
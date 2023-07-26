let mode;
//  ------ 비디오의 크기 반환--------
function getVidSize(){
    let width = $("#myVideo").width();
    let height = $("#myVideo").height();
    return {width, height};
}

// -------텍스트 박스 크기조정--------
function txtBoxResize(){
    let v = getVidSize();
    if (mode == 0) {
        // 텍스트 박스 위치 조정
        $(".txtBox").css("left", (v.width * 0.405) + "px");
        $(".txtBox").css("top", (v.height * 0.295) + "px");
        // 텍스트 박스 크기 조정
        $(".txtBox").css("width", (v.width * 0.15) + "px");
        // 인풋텍스트 위치, 크기 조정
        $(".txt").css("width", (v.width * 0.15) + "px");
        $(".txt").css("height", (v.height * 0.05) + "px");
        $(".txt").css("margin-bottom", (v.height * 0.01) + "px");
        $(".txt").css("font-size", (v.height * 0.017) + "px");
        // label
        $(".myLabel").css("font-size", (v.height * 0.015) + "px");
    } else {
        // 텍스트 박스 위치 조정
        $(".txtBox").css("left", (v.width * 0.31) + "px");
        $(".txtBox").css("top", (v.height * 0.282) + "px");
        // 텍스트 박스 크기 조정
        $(".txtBox").css("width", (v.width * 0.35) + "px");
        // 인풋텍스트 위치, 크기 조정
        $(".txt").css("width", (v.width * 0.35) + "px");
        $(".txt").css("height", (v.height * 0.03) + "px");
        $(".txt").css("margin-bottom", (v.height * 0.01) + "px");
        $(".txt").css("font-size", (v.height * 0.022) + "px");
        $("#login").css("margin-bottom", "");
        // 비디오 살짝 올리기
        $(".vid").css("top", (v.width * -0.3) + "px");
        // label
        $(".myLabel").css("font-size", (v.height * 0.015) + "px");
    }
}
function videoInit() {
    $(".navbar").addClass("animate__animated animate__fadeInDown animate__fast");
    if (window.innerWidth < 768) {
        $("#videoSrc").attr("src", "/img/main2.mp4");
        $("#myVideo")[0].load();
        mode = 1;
    } else {
        $("#videoSrc").attr("src", "/img/main.mp4");
        $("#myVideo")[0].load();
        mode = 0;
    }
}

// ---------MAIN----------
$(function(){
    videoInit();
    // 동영상이 끝날 경우 나머지 UI 보이기
    $("#myVideo")[0].addEventListener("ended",function(e){
        $(".txtBox").css("display", "block");
        $(".navbar").css("display", "block");
    },false);
    // 클릭시 스킵
    $("#myVideo").click(function(){
        $("#myVideo")[0].currentTime = 3.5;
    })
    // 화면 조정시 박스 위치 조정
    window.onresize = function(e) {
        txtBoxResize();
    };
});
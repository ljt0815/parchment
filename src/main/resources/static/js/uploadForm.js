let uploadFile = null;
let isAnimated = false;
let bodycnt = 0;
let cnt = 0;

function fileSelect(files) {
    $("#file-ico, #error-ico, #success-ico").css("display", "none");
    if (files == null) {
        $("#error-ico").css("display", "block");
        $("#file-lbl").text("알 수 없는 에러");
        return false;
    }
    if (files.length > 1) {
        $("#error-ico").css("display", "block");
        $("#file-lbl").text("파일은 1개만 업로드 가능합니다.");
        return false;
    }
    if (files[0].name.substring(files[0].name.lastIndexOf('.') + 1, files[0].name.length) != "pdf") {// .부터 마지막까지 문자열
        $("#error-ico").css("display", "block");
        $("#file-lbl").text("PDF파일만 업로드 가능합니다.");
        return false;
    }
    if (files[0].size > 500000000) {
        $("#error-ico").css("display", "block");
        $("#file-lbl").text("용량이 500MB를 넘을 수 없습니다.");
        return false;
    }
    $("#success-ico").css("display", "block");
    $(".upload-ico").css("display", "none");
    $("#file-lbl").text(files[0].name);
    if ($("#title").val() == "")
        $("#title").val(files[0].name.substring(0, files[0].name.lastIndexOf('.')));
    return true;
}

$(function() {
    $("#file").on("change", function(e){
        if (!fileSelect($(this)[0].files)) { // 파일이 유효하지 않을 때
            $("#file-drop-box").addClass("animate__animated animate__bounce animate__shakeX");
            $("#file-drop-box").on("animationend", function() {
                $("#file-drop-box").removeClass("animate__animated animate__bounce animate__shakeX");
            });
            $("#file").val("");
            $("#file-drop-box").css("background-color", "#FCBAAD");
            $("#file-drop-box").css("border-color", "#E48586");
            uploadFile = null;
        } else { // 파일이 유효할 때
            $("#file-drop-box").css("background-color", "#91C8E4");
            $("#file-drop-box").css("border-color", "#749BC2");
            uploadFile = $(this)[0].files[0];
        }
    });
    $("#upload-btn").click(function() {
        if (uploadFile == null) {
            $("#file-drop-box").addClass("animate__animated animate__bounce animate__shakeY");
            $("#file-drop-box").on("animationend", function() {
                $("#file-drop-box").removeClass("animate__animated animate__bounce animate__shakeY");
            });
            return ;
        }
        $("#modal").modal("show");
        let title = $("#title").val();
        if (title == "")
            $("#title-lbl").text("제목 없음");
        else {
            $("#filename-lbl").text(uploadFile.name);
            $("#title-lbl").text(title);
        }
    });
    $("#file-drop-box").on({
        "click": function(e){
            $("#file")[0].click();
        },
        "dragenter" : function(e) {
            e.preventDefault();
            cnt++;
            $(this).css({
                "background-color": "#749BC2",
                "border-color": "#91C8E4"
            });
        },
        "dragleave" : function(e) {
            // e.stopPropagation();
            e.preventDefault();
            cnt--;
            if (cnt == 0) {
                $(this).css("background-color", "#91C8E4");
                $(this).css("border-color", "#749BC2");
            }
        },
        'dragover' : false,
        "drop": function(e) {
            e.preventDefault();
            $("#file-drop-box").removeClass("animate__animated animate__bounce animate__shakeY");
            bodycnt = 0;
            cnt = 0;
            let files = e.originalEvent.dataTransfer.files;
            if (fileSelect(files)) { // 파일이 유효하다면
                $(this).css("background-color", "#91C8E4");
                $(this).css("border-color", "#749BC2");
                uploadFile = files[0];
            } else { //파일 유효하지 않을 때
                $("#file-drop-box").addClass("animate__animated animate__bounce animate__shakeX");
                $("#file-drop-box").on("animationend", function() {
                    $("#file-drop-box").removeClass("animate__animated animate__bounce animate__shakeX");
                });
                $(this).css("background-color", "#FCBAAD");
                $(this).css("border-color", "#E48586");
                uploadFile = null;
            }
        }
    });
    $("body").on({
        "dragenter": function(e){
            e.preventDefault();
            bodycnt++;
            $("#file-drop-box").addClass("animate__animated animate__bounce animate__shakeY");
        },
        "dragleave" : function(e) {
            e.preventDefault();
            bodycnt--;
            if (bodycnt == 0)
                $("#file-drop-box").removeClass("animate__animated animate__bounce animate__shakeY");
        }
    });

});
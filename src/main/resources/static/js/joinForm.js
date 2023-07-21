function pwdCheckLabel(input) {
    let lowercaseRegex = /[a-z]/;
    let uppercaseRegex = /[A-Z]/;
    let numberRegex = /[0-9]/;
    let specialRegex = /[^0-9a-zA-Z\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\<\.\>\/\?]+/;
    let lenMinRegex = /.{10,}/;
    if (input.match(lowercaseRegex))
        $("#lblLower").css("color", "green");
    else
        $("#lblLower").css("color", "red");
    if (input.match(uppercaseRegex))
        $("#lblUpper").css("color", "green");
    else
        $("#lblUpper").css("color", "red");
    // 특수문자는 허용값 이외의 값이 들어오면 정규표현식이 잡아줌
    if (input.match(specialRegex))
        $("#lblSpecial").css("color", "red");
    else
        $("#lblSpecial").css("color", "green");
    if (input.match(numberRegex))
        $("#lblNum").css("color", "green");
    else
        $("#lblNum").css("color", "red");
    if (input.match(lenMinRegex))
        $("#lblLength").css("color", "green");
    else
        $("#lblLength").css("color", "red");
}
$(function(){
    let idDupChk = false;
    $("#btn_join").click(function(){
        let data = {
            username: $("#id").val(),
            email: $("#email").val(),
            password: $("#pwd").val()
        };
        $.ajax({
            url: "/user/join",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            method: "POST",
            dataType: "json"
        }).done(function(resp){
            console.log(resp);
        }).fail(function(xhr, status, errorThrown){
            console.log(errorThrown + status);
        });
        // // location.href="first_main.html";
        // if ($("#repwd")[0].value !== $("#pwd").val()) {
        //     alert("비밀번호와 비밀번호 확인이 서로 다릅니다.");
        //     return ;
        // }
        // //최종 정규식
        // let idRegex = /^[a-z0-9]{5,20}$/;
        // let pwdRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9\`\~\!\@\#\$\%\^\&\*\(\)\-\_\=\+\,\<\.\>\/\?]{10,}$/;
        // if (!$("#id").val().match(idRegex) || !idDupChk){
        //     alert("아이디 중복확인이 되지 않았습니다.");
        //     return ;
        // }
        // if (!$("#pwd").val().match(pwdRegex)){
        //     alert("비밀번호가 규칙에 맞지 않습니다.");
        //     return ;
        // }

    });
    $("#btn_dupChk").click(function(){
        let regex = /^[a-z0-9]{5,20}$/;
        let data = {
            id: $("#id").val()
        };
        if ($("#id").val().match(regex)) {
            idDupChk = true;
            $.ajax({
                url: "/user/idDupChk",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                method: "POST",
                dataType: "json"
            }).done(function(resp){
                if (resp.data == -1) {
                    $("#lblId").css("display", "block");
                    $("#lblId").css("color", "green");
                    $("#lblId").text("사용 가능한 아이디입니다.");
                } else {
                    $("#lblId").css("display", "block");
                    $("#lblId").css("color", "red");
                    $("#lblId").text("존재하는 아이디입니다.");
                    idDupChk = false;
                }
            }).fail(function(xhr, status, errorThrown){
                console.log(errorThrown + status);
            });
        }
        else {
            $("#lblId").css("display", "block");
            $("#lblId").css("color", "red");
            $("#lblId").text("아이디는 소문자, 숫자만 허용되며 5자이상 20자이하여야 합니다.");
        }
    });
    $("#pwd").focusin(function(){
        let location = document.querySelector("#pwd").offsetTop;
        window.scrollTo({top: location, behavior:"smooth"});
        $("#pwdLabelBox").css("display", "block");
        pwdCheckLabel(this.value);
    });
    $("#pwdLabelBox").focusout(function(){
        $("#pwdLabelBox").css("display", "none");
    });
    $("#repwd").focusin(function(){
        let location = document.querySelector("#repwd").offsetTop;
        window.scrollTo({top: location, behavior:"smooth"});
        if (this.value != "")
            $("#lblrepwd").css("display", "block");
    });
    $("#pwd").keyup(function(){
        pwdCheckLabel(this.value);
        if (this.value === $("#repwd").val()) {
            $("#lblrepwd").text("비밀번호가 일치합니다.");
            $("#lblrepwd").css("color", "green");
        } else {
            $("#lblrepwd").text("비밀번호가 일치하지 않습니다.")
            $("#lblrepwd").css("color", "red");
        }
    });
    $("#id").keyup(function(){
        $("#lblId").css("display", "none");
        idDupChk = false;
    })
    $("#repwd").keyup(function(){
        if (this.value != "")
            $("#lblrepwd").css("display", "block");
        else
            $("#lblrepwd").css("display", "none");
        if (this.value === $("#pwd").val()) {
            $("#lblrepwd").text("비밀번호가 일치합니다.")
            $("#lblrepwd").css("color", "green");
        } else {
            $("#lblrepwd").text("비밀번호가 일치하지 않습니다.")
            $("#lblrepwd").css("color", "red");
        }
    });
});
document.getElementsByClassName('emailButton').item(0).addEventListener('click', checkEmail);
document.getElementsByClassName('authenticationNumberButton').item(0).addEventListener('click', checkAuthenticationNumber);
var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
let timer;

// 이메일 중복 체크 & 인증 번호 수신 가능한 이메일인지 체크
// 2분 이내에 인증하도록 timer (인증할 동안 이메일 바꾸지 못하도록 input과 button 잠시 비활성화)
function checkEmail() {
    let email = document.getElementById('email').value;
    document.getElementById('emailDuplicated').innerHTML = "";
    fetch('/checkEmail', {
        method: 'POST',
        headers: {
            'header': headerName,
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-Token': token,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({email: email})
    })
        .then(function (response) {
            if (response.ok) {
                document.getElementById('emailDuplicated').innerHTML = "";
                document.getElementById('timer').innerHTML = "";
                document.getElementById('authenticationNumber').readOnly = false;
                document.getElementById('authenticationButton').disabled = false;
                document.getElementById('rightOrWrongMessage').innerHTML = "";
                console.log(response)
                return response.json();
            } else {
                throw new Error('네트워크 문제');
            }
        })
        .then(function (result) {
            console.log(result)

            if (result.nullOrBlank) {
                document.getElementById('nullOrBlank').innerHTML = "공백이거나 입력하지 않은 이메일을 인증할 수 없습니다.";
            }
            if (result.duplicated) {
                document.getElementById('timer').innerHTML = "";
                document.getElementById('emailDuplicated').innerHTML = "이미 존재하는 이메일입니다.";
            }
            if (result.success) {
                document.getElementById('authenticationNumber').style.display = "block";
                document.getElementById('authenticationButton').style.display = "block";

                document.getElementById('authenticationNumber').value = "";
                document.getElementById('authenticationNumber').placeholder = "이메일로 전송된 숫자 6자리 입력";

                document.getElementById('email').readOnly = true;
                document.getElementsByClassName('emailButton').item(0).disabled = true;

                let time = 120;
                let min, sec;
                timer = setInterval(function () {
                    // 숫자 두 자리씩 유지
                    min = String(parseInt(time / 60)).padStart(2, '0');
                    sec = String(time % 60).padStart(2, '0');
                    document.getElementById('timer').innerHTML = "발송된 인증 번호를 2분 이내에 입력해주세요. " + min + ":" + sec;
                    time--;

                    if (time < 0) {
                        clearInterval(timer);
                        document.getElementsByClassName('emailButton').item(0).disabled = false;
                        document.getElementById('email').readOnly = false;
                        document.getElementById('timer').innerHTML = "시간이 만료되었습니다. 다시 인증해주세요.";
                    }
                }, 1000);
            }
        })
        .catch(function (error) {
            alert(error.message);
        });
}


function checkAuthenticationNumber() {
    let email = document.getElementById('email').value;
    let authenticationNumber = document.getElementById('authenticationNumber').value;
    fetch('/checkAuthenticationNumber', {
        method: 'POST',
        headers: {
            'header': headerName,
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-Token': token,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({email: email, authenticationNumber: authenticationNumber})
    })
        .then(function (response) {
            if (response.ok) {
                document.getElementById('rightOrWrongMessage').innerHTML = "";
                return response.text();
            } else {
                throw new Error('네트워크 문제');
            }
        })
        .then(function (result) {
            if (result === "wrong") {
                document.getElementById('rightOrWrongMessage').innerHTML = "인증 번호를 다시 확인해주세요.";
            }
            if (result === "right") {
                clearInterval(timer);
                document.getElementById('timer').innerHTML = "";

                document.getElementById('authenticationNumber').readOnly = true;
                document.getElementById('authenticationButton').disabled = true;
                document.getElementById('rightOrWrongMessage').innerHTML = "인증되었습니다.";

                document.getElementsByClassName('emailButton').item(0).disabled = false;
                document.getElementById('email').readOnly = false;
            }
        })
        .catch(function (error) {
            alert(error.message);
        });
}
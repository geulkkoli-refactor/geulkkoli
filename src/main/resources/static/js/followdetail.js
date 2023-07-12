let backButton = document.querySelector("button.btn-back_text_hide");
backButton.addEventListener("click", function () {
    window.history.back();
});

let isFetching = false;
const allCountText = document.querySelector('span#all-count').innerText;
const allCount = parseInt(allCountText,10);
const getList = () => {
    var lastId = document.querySelector('ul li:last-child span#follow-id').innerText;
    console
    URL = "/api/followees/" + lastId;
    console.log(URL);
    console.log('getList()');
    isFetching = true;
    fetch(URL, {
        method: "GET",
    }).then(response => response.ok ? response.json() : Promise.reject(response))
        .then(data => {
            console.log(data);
            drawList(data);
        });
    isFetching = false;
}
// 스크롤 이벤트 시 실행할 구독하는 사람 목록 구독 취소 버튼과과 각 구독하는 사람의 구독 취소 버튼에 이벤트를 추가하는 함수
const drawList = (data) => {
    const list = document.querySelector('ul.list_follow');
    data.followInfos.forEach((followInfo, index) => {
        console.log(followInfo)
        const li = document.createElement('li');
        const span = document.createElement('span');
        span.classList.add('span_follow-profile-image');
        li.appendChild(span);
        const span2 = document.createElement('span');
        span2.innerText = followInfo.nickName;
        span2.classList.add('span_follow');
        li.appendChild(span2);
        const span3 = document.createElement('span');
        span3.innerText = followInfo.id;
        span3.classList.add('span_follow');
        span3.hidden = true;
        span3.id = 'follow-id';
        li.appendChild(span3);
        list.appendChild(li);
        const span4 = document.createElement('span');
        span4.innerText = followInfo.userId;
        span4.classList.add('span_follow');
        span4.hidden = true;
        span4.id = 'userId';
        li.appendChild(span4);
        const button = document.createElement('button');
        const uniqueId = 'btn-unfollow-' + followInfo.id;
        button.id = uniqueId;
        button.className = 'btn-subscribe';
        button.setAttribute('type', 'button');
        const buttonInnerSpan = document.createElement('span');
        buttonInnerSpan.classList.add('txt_default');
        buttonInnerSpan.innerText = '구독중';
        button.appendChild(buttonInnerSpan);
        const buttonInnerSpan2 = document.createElement('span');
        buttonInnerSpan2.classList.add('txt_on');
        buttonInnerSpan2.innerText = '구독 취소';
        button.appendChild(buttonInnerSpan2);

        li.appendChild(button);

        list.appendChild(li);
        if (index === data.length - 1) {
            lastId = followInfo.id;
        }
        unFollowButtonHandler(uniqueId);
    });
}

function getNewId(buttonId) {
    return buttonId.startsWith('btn-unfollow')
        ? buttonId.replace('btn-unfollow', 'btn-follow')
        : buttonId.replace('btn-follow', 'btn-unfollow');
}

function followButtonHandler(buttonId) {
    var followButton = document.getElementById(buttonId);
    console.log(followButton)
    if (followButton) {
        followButton.addEventListener('click', function (event) {
            let target = event.target;
            console.log(target)
            console.log(target.parentElement)
            var userId = target.parentElement.parentElement.querySelector('span#userId').innerText;
            var url = '/api/follow/' + userId;
            fetch(url, {
                method: 'GET', // or 'POST'
            })
                .then(function (response) {
                    if (response.ok) {
                        var unfollowButton = document.createElement('button');
                        unfollowButton.id = getNewId(buttonId);
                        unfollowButton.className = 'btn-subscribe';
                        unfollowButton.setAttribute('type', 'button');

                        const buttonInnerSpan = document.createElement('span');
                        buttonInnerSpan.classList.add('txt_default');
                        buttonInnerSpan.innerText = '구독중';
                        unfollowButton.appendChild(buttonInnerSpan);
                        const buttonInnerSpan2 = document.createElement('span');
                        buttonInnerSpan2.classList.add('txt_on');
                        buttonInnerSpan2.innerText = '구독 취소';
                        unfollowButton.appendChild(buttonInnerSpan2);

                        var parentElement = followButton.parentNode;
                        if (parentElement) {
                            parentElement.replaceChild(unfollowButton, followButton);
                        }
                        unFollowButtonHandler(unfollowButton.id);
                    }
                })
                .catch(function (error) {
                    console.log('Error:', error);
                });
        });
    }
}

function unFollowButtonHandler(buttonId) {
    var unfollowButton = document.getElementById(buttonId);
    console.log(unfollowButton)
    if (unfollowButton) {
        unfollowButton.addEventListener('click', function (event) {
            event.preventDefault();
            let target = event.target;
            var userId = target.parentElement.parentElement.querySelector('span#userId').innerText;
            console.log(userId)
            var url = '/api/unfollow/' + userId;
            fetch(url, {
                method: 'GET', // or 'POST'
            })
                .then(function (response) {
                    if (response.ok) {
                        var followButton = document.createElement('button');
                        followButton.id = getNewId(buttonId);
                        followButton.className = 'btn-subscribe';
                        followButton.setAttribute('type', 'button');
                        const buttonInnerSpan = document.createElement('span');
                        buttonInnerSpan.innerText = '구독하기';
                        followButton.appendChild(buttonInnerSpan);
                        var parentElement = unfollowButton.parentNode;
                        if (parentElement) {
                            parentElement.replaceChild(followButton, unfollowButton);
                        }

                        followButtonHandler(followButton.id);
                    }
                })
                .catch(function (error) {
                    console.log('Error:', error);
                });
        });
    }
}

// 처음 서버 렌더링 시에는 각 버튼에 이벤트 핸들러를 등록해준다.
document.addEventListener('DOMContentLoaded', function () {
    //button.btn-subscribe 클래스를 가진 모든 요소를 찾아서 이벤트 핸들러를 등록한다.
    let element = document.querySelectorAll('button.btn-subscribe');
    element.forEach(function (element) {
        var buttonId = element.id;
        if (!Object.is(buttonId, null)) {
            if (buttonId.startsWith('btn-unfollow')) {
                console.log(buttonId)
                unFollowButtonHandler(buttonId);
            }

            if (buttonId.startsWith('btn-follow')) {
                console.log(buttonId)
                followButtonHandler(buttonId);
            }
        }
    });
});
window.addEventListener("scroll", () => {
    console.log('scroll')
    let scrollHeight = window.scrollY;
    let innerHeight = window.innerHeight;
    let offsetHeight = document.body.offsetHeight;
    let elementById = document.getElementById('ul-follow');
    let length = elementById.getElementsByTagName('li').length;
    console.log(length)
    console.log(allCount)
    const Is_END = scrollHeight + innerHeight > offsetHeight - 10;
    if (Is_END && !isFetching && length < allCount) {
        console.log('list')
        getList();
    }
});
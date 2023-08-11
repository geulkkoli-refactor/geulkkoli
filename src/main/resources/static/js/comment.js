commentEditButtonInit();
commentDeleteButtonInit();

const commentBodyBox = document.getElementById('commentBody');

// 댓글 작성 버튼 이벤트 등록
const commentSubmit = document.getElementById('commentSubmit');
commentSubmit.addEventListener('click', function (ev) {

    ev.preventDefault();
    const commentForm = document.getElementById('comment');
    console.log(commentForm);
    const str_json = JSON.stringify(
        Object.fromEntries(
            new FormData(commentForm)));
    const createFieldError = commentForm.querySelector('.field-error');
    var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
    var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
    if(commentBodyBox.value) {
        fetch(commentForm.getAttribute('action'), {
            method: 'POST',
            headers: {
                'header': headerName,
                'X-Requested-With': 'XMLHttpRequest',
                'X-CSRF-Token': token,
                'Content-Type': 'application/json',
            },
            body: str_json
        })
            .then((response) => {
                if (response.ok) {
                    response.json().then((commentList) => {
                        if (commentList) {
                            allRemoveChild(createFieldError);
                            commentRemake(commentList, true);
                        }
                    });
                } else {
                    response.json().then((validList) => {
                        validCheckCommentSize(validList, createFieldError);
                    });
                } // if (response.ok)
            }); // then

    } // if(commentBodyBox.value)
}); // #commentSubmit.addEventListener

// 댓글 수정 버튼 이벤트 초기화
function commentEditButtonInit() {
    let commentEdits = document.getElementsByClassName('commentEdit');
    for (let commentEdit of commentEdits) {
        commentEdit.removeEventListener('click', commentEditButtonEvent);
        commentEdit.addEventListener('click', commentEditButtonEvent);
    }
} //commentEditButtonInit

// 댓글 삭제 버튼 이벤트 초기화
function commentDeleteButtonInit() {
    for ( let commentDeleteButton of document.getElementsByClassName("commentDelete")) {
        commentDeleteButton.removeEventListener('click', commentDeleteButtonEvent);
        commentDeleteButton.addEventListener('click', commentDeleteButtonEvent);
    }
} //commentDeleteButtonInit

// 댓글 수정 버튼 이벤트
function commentEditButtonEvent(event) {
    event.preventDefault();

    if (document.querySelectorAll('.editing').length) {
        alert('수정 중인 댓글이 있습니다. 수정 중인 댓글을 등록하거나 취소해주세요.');
        return;
    }

    const target = event.target;
    const currentEditComment = target.closest('.card');
    currentEditComment.classList.add('editing');
    let cardBody = target.closest('.card-body');
    let textarea = document.createElement('textarea');
    let p = cardBody.firstElementChild;
    let commentBodyText = p.innerText;
    let transButton = target.closest('div').querySelector('.commentDelete');

    textarea.setAttribute('class', 'col form-control')
    textarea.value = commentBodyText;
    cardBody.replaceChild(textarea, p);

    target.removeEventListener('click', commentEditButtonEvent);
    target.innerText = '등록';
    transButton.innerText = '취소';
    const updateFieldError = currentEditComment.querySelector('.field-error');

    transButton.addEventListener('click', function() {
        if(confirm('정말 댓글 작성을 취소하시겠습니까?')) {
            p.innerText = commentBodyText;
            cardBody.replaceChild(p, textarea);
            target.innerText = '수정';
            transButton.innerText = '삭제';
            target.addEventListener('click', commentEditButtonEvent);
            allRemoveChild(updateFieldError);
            currentEditComment.classList.remove('editing');
        }
    });

    target.addEventListener('click', function (ev) {
        ev.preventDefault();

        if (target.innerText != '등록')
            return;

        let formData = new FormData();
        formData.set('commentId', target.closest('.card').querySelector('h5').id);
        formData.set('commentBody', target.closest('.card-body').firstElementChild.value);
        var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
        var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
        const postId = document.getElementById("post-id").getAttribute("name");
        fetch('/comments/' + postId, {
            method: 'PUT',
            headers: { 'header': headerName,
                'X-Requested-With': 'XMLHttpRequest',
                'content-type': 'application/json',
                'X-CSRF-Token': token,},
            body: JSON.stringify(Object.fromEntries(formData))
        })
            .then((response) => {
                if (response.ok) {
                    response.json().then((commentList) => {
                        if (commentList) commentRemake(commentList, false);
                    });
                } else {
                    response.json().then((validList) => {
                        validCheckCommentSize(validList, updateFieldError);
                    });
                }
            })
    });
} //commentEditButtonEvent

// 댓글 삭제 버튼 이벤트
function commentDeleteButtonEvent(ev) {
    ev.preventDefault();

    let commentDeleteButton = ev.target;
    if (commentDeleteButton.innerText != '삭제')
        return;

    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    let formData = new FormData();
    formData.set('commentId', commentDeleteButton.closest('.card').querySelector('h5').id);
    var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
    var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
    fetch('/comments', {
        method: 'DELETE',
        headers: {
            'header': headerName,
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-Token': token,
            'Content-Type': 'application/json'},
        body: JSON.stringify(Object.fromEntries(formData))
    }).then((response) => {
        if (response.status == 200) {
            commentDeleteButton.closest('.card').remove();
        }
    });
} //commentDeleteButtonEvent

//댓글들을 새로 작성한다.
function commentRemake(list, isClear) {
    const postComment = document.getElementById('postComment');

    allRemoveChild(postComment);

    for (let newComment of list) {
        let forComment = document.createElement('div');
        forComment.setAttribute('class', 'mt-2 card w-100');
        forComment.style.width = '18rem';
        let forCommentHeader = document.createElement('div');
        forCommentHeader.setAttribute('class', 'card-header row justify-content-md-center');
        let forCommentBody = document.createElement('div');
        forCommentBody.setAttribute('class', 'card-body');

        let userNickName = document.createElement('h5');
        userNickName.setAttribute('id', newComment.commentId);
        userNickName.setAttribute('class', 'card-title col');
        userNickName.innerText = newComment.nickName;

        let writeDate = document.createElement('h6');
        writeDate.setAttribute('class', 'card-subtitle col-md-auto');
        writeDate.innerText = newComment.date;

        let newCommentBody = document.createElement('p');
        newCommentBody.setAttribute('class', 'card-text commentBody');
        newCommentBody.innerText = newComment.commentBody;

        let errorDiv = document.createElement('div');
        errorDiv.setAttribute('class', 'field-error');

        let buttonDiv = document.createElement('div');
        buttonDiv.setAttribute('class', 'mt-1 d-grid gap-2 d-md-flex justify-content-md-end');

        let editButton;
        let deleteButton;
        var loggedInUser = /*[[${#authentication.principal}]]*/ null;

        validName = document.getElementById("auth-user-nickName").innerText;
        if (newComment.nickName === validName) {
            editButton = document.createElement('button');
            editButton.setAttribute('class', 'btn btn-outline-warning btn-sm commentEdit');
            editButton.innerText = '수정';

            deleteButton = document.createElement('button');
            deleteButton.setAttribute('class', 'btn btn-outline-danger btn-sm commentDelete');
            deleteButton.innerText = '삭제';

            buttonDiv.appendChild(editButton);
            buttonDiv.appendChild(deleteButton);
        }

        forComment.appendChild(forCommentHeader);
        forCommentHeader.appendChild(userNickName);
        forCommentHeader.appendChild(writeDate);

        forComment.appendChild(forCommentBody);
        forCommentBody.appendChild(newCommentBody);
        forCommentBody.appendChild(errorDiv);
        forCommentBody.appendChild(buttonDiv);

        postComment.appendChild(forComment);

        if(isClear)
            commentBodyBox.value = "";

    }
    commentEditButtonInit();
    commentDeleteButtonInit();
} // commentRemake

// 댓글 글자 수 유효성 검사에 걸렸을 경우
function validCheckCommentSize(validList, fieldError) {
    if (fieldError.childElementCount) {
        allRemoveChild(fieldError);
        validCheckCommentSize(validList, fieldError);
    } else {
        for (let valid of validList) {
            let validText = document.createElement('p');
            validText.innerText = valid;
            validText.style.color = '#dc3545';
            fieldError.appendChild(validText);
        }
    }
} // validCheckCommentSize

function allRemoveChild(node) {
    while (node.hasChildNodes()) {
        node.firstChild.remove();
    }
}
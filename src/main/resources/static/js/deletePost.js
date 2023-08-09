document.getElementById("deletePostButton").addEventListener("click", deletePost);
function deletePost() {
    const deleteTitle = document.getElementById("content-title").outerText;
    const postId = document.getElementById("post-id").getAttribute("name");
    const userNickName = document.getElementById("nickName").getAttribute("name");
    const compareTitle = document.getElementById("post-Compare").value;
    var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
    var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
    let params = {
        postId: postId,
        userNickName: userNickName
    };
    let query = Object.keys(params)
        .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(params[key]))
        .join('&');
    if (compareTitle === deleteTitle) {
        fetch('/post/request?' + query, {
            headers: {
                'header': headerName,
                'X-Requested-With': 'XMLHttpRequest',
                'X-CSRF-Token': token
            },
            method: 'DELETE'
        })
            .then(response => {
                window.location = response.url;
            })
            .then(
                alert("삭제되었습니다.")
            )
    } else {
        alert("제목이 일치하지 않습니다.");
    }
}
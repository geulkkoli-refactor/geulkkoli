document.getElementById("favoriteForm").addEventListener("click", function (event) {
    event.preventDefault();

    // Perform necessary AJAX request

    // 자기 자신의 글이면 좋아요 함수를 실행하지 않는다.
    nickName = document.getElementById("auth-user-nickName").innerText;
    var author_nickName = document.getElementById("auth-nick-name").innerText;
    if (nickName !== author_nickName) {
        toggleFavorite();
    }
});

function toggleFavorite(event) {
    const postId = document.getElementById("post-id").getAttribute("name");
    const likeImage = document.getElementById("favorite_like");
    const disLikeImage = document.getElementById("favorite_disLike");

    var headerName = document.getElementsByClassName("csrf_input")[1].getAttribute("name");
    var token = document.getElementsByClassName("csrf_input")[0].getAttribute("value");
    const requestOptions = {
        headers: {
            'header': headerName,
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-Token': token
        },
    };

    const baseURL = "/favorite/";
    const URL = baseURL + "pressFavorite/" + postId;
    console.log(URL)
    fetch(URL, requestOptions)
        .then(response => response.json())
            .then(data => {
            if (data.requestResult === 'Add Success') {
                if (likeImage === null) {
                    disLikeImage.src = "/images/free-icon-like-3984277.png";
                } else {
                    likeImage.src = "/images/free-icon-like-3984277.png";
                }
               document.getElementById("favoriteCount").innerText = (String(data.favoriteCount));
                return ;
            }
            if (data.requestResult === 'Cancel Success') {
                // Handle cancel success case
                if (likeImage === null) {
                    disLikeImage.src = "/images/free-icon-like-3984190.png";
                } else {
                    likeImage.src = "/images/free-icon-like-3984190.png";
                }
               document.getElementById("favoriteCount").innerText = (String(data.favoriteCount));
                return ;
            }
            if (data.requestResult === 'Error') {
                // Handle error case
                alert("로그인이 필요합니다.");
                return window.location.href = "/login";
            }
        })
        .catch(error => {
            console.log(error);
        }
    )
}
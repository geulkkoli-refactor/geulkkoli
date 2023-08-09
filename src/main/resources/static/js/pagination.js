
const blockCount = 5;
const block = Math.floor(currentPage / blockCount) * blockCount;

const ul = document.querySelector('.pagination');
const li = document.getElementsByClassName('page-item');

let nextPage = block + blockCount;
if(nextPage >= endPage) {
    nextPage = endPage-1;
}

for (let page = block, index = 0;
     page < (block + blockCount) && page < endPage;
     ++page, ++index) {

    const liPage = document.createElement('li');
    liPage.className = 'page-item';
    if (page === Number(currentPage)) {
        liPage.className += ' active';
    }

    const a = document.createElement('a');
    a.className = 'page-link';
    a.text = String(page + 1);
    a.href = makeURI(a.text - 1, size);
    liPage.append(a);
    ul.appendChild(liPage);
}
console.log(li[0]);
console.log(li[1]);

isVisible(isFirst, li[0]);
li[0].querySelector('a').href = makeURI(0, size);
isVisible(isLast, li[1]);
li[1].querySelector('a').href = makeURI(endPage-1, size);
ul.appendChild(li[1]);

function isVisible(bool, obj) {
    bool ? obj.style.visibility = 'hidden' : obj.style.visibility = 'visible'
}

function makeURI(page, size) {
    const type =document.getElementById('search-type').value;
    const words =document.getElementById('search-words').value;
    let uri = '/post/channels?page=' + page + '&size=' + size;

    if (type != null) {
        uri += '&searchType=' + type;
        if (words != null) {
            let encodedWords = encodeURIComponent(words);
            uri += '&searchWords=' + encodedWords;
        }
    }

    return uri;
}
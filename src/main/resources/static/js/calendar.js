const monthNames = [
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
];

const dayNames = ["Mon", "Wed", "Fri"];

let calendar = document.getElementsByClassName('calendar').item(0);
let daysBox = document.getElementsByClassName('days').item(0);

// 달의 마지막 날짜 구하기 (혹은 그 달의 전체 일수로 볼 수도 있는)
function getMonthDates(year, month) {
    return new Date(year, month + 1, 0).getDate();
}

// 요일 구하기
function getWeekDay(year, month, date) {
    return new Date(year, month, date).getDay();
}

//맨 왼쪽 월수금 출력
function makeDays() {
    for (let i = 0; i < 3; i++) {
        let createDay = document.createElement('div');
        createDay.innerHTML = dayNames[i];
        daysBox.appendChild(createDay);
    }
}

let postingDates; //한 유저의 글 쓴 날들의 집합
const userNickName = document.getElementById('user-nickname').innerText.split(" ")[0];

async function settingCalendarDaysByUserSignUpDate() {
    try {
        const response = await fetch(`/user/${userNickName}/calendar`);
        const data = await response.json();

        makeDays();
        let signUpDate = new Date(data.signUpDate);
        let startYear = signUpDate.getFullYear();
        let now = new Date();
        let nowYear = now.getFullYear();
        let nowMonth = now.getMonth() +1;
        postingDates = data.allPostDatesByOneUser.map(postDate => postDate.replace(/(\d{4})\. (\d{2})\. (\d{2}) (.+)/, '$2/$3/$1 $4'));
        console.log(postingDates);
        //가입 후 1년이 지났는지
        let timeDiff = Math.abs(now.getTime() - signUpDate.getTime());
        let diffYears = Math.floor(timeDiff / (1000 * 3600 * 24 * 365));

        if (diffYears >= 1) {
            createBeforeYearsDropDown(nowYear, startYear);
        }

        if (nowMonth === 12)  //최근 1년 보여주는데, 현재가 12월이면 해당 년도 1~12월 노출
            for (let month = 0; month < nowMonth; month++)
                createCalendar(nowYear, month);

        if (nowMonth !== 12) { //최근 1년 보여주는데, 현재가 12월이 아니면 전년도까지 합쳐서 1년 달력 노출
            for (let month = nowMonth; month < 12; month++)
                createCalendar(nowYear - 1, month);

            for (let month = 0; month < nowMonth; month++)
                createCalendar(nowYear, month);
        }

    } catch (error) {
        console.error(error);
    }
}

function createCalendar(year, month) {
    let oneMonth = document.createElement('div'); //하나의 달 묶음
    oneMonth.classList.add('one-month');
    calendar.appendChild(oneMonth);

    let monthName = document.createElement('div'); //그 달이 무슨 달인지
    oneMonth.appendChild(monthName);
    monthName.innerText = monthNames[month];
    monthName.classList.add('month-name');

    let oneMonthColumns = document.createElement('div');
    oneMonthColumns.classList.add('one-month-columns');
    oneMonth.appendChild(oneMonthColumns);

    let firstDay = getWeekDay(year, month, 1); //요일
    let dates = getMonthDates(year, month); //달의 전체 일수
    let columns = Math.ceil((firstDay + dates) / 7); //주

    let date = 1;
    for (let i = 0; i < columns; i++) {
        let column = document.createElement('div');
        column.classList.add('calendar-column');
        let cellCount = 0; //막주랑 다음 첫주 간격이 벌어지는데 막주에 7일 다 있으면 margin 조절 필요 없어서 조절용 count
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < firstDay) {
                column.classList.add('first-column');
            } else if (date > dates) {
                column.classList.add('last-column');
            } else {
                let cell = document.createElement('div');
                cell.classList.add('calendar-cell');
                cell.innerHTML = '';
                cell.classList.add('borderLine');
                column.appendChild(cell);
                if (checkDate(year, month, date))
                    cell.classList.add('coloring');
                date++;
                cellCount++;
            }
        }
        if (cellCount === 7) {
            oneMonth.classList.add('seven-cells');
        }
        oneMonthColumns.appendChild(column);
    }
}

settingCalendarDaysByUserSignUpDate().then(r => console.log('success'));

//post 연월일과 네모네모로 만들어준 연월일 위치 check (후에 coloring 해주려고)
function checkDate(year, month, date) {
    for (let oneDate of postingDates) {
        let postingDate = new Date(oneDate);
        let postYear = postingDate.getFullYear();
        let postMonth = postingDate.getMonth();
        let postDate = postingDate.getDate();
        if (postYear === year && postMonth === month && postDate === date) {
            return true;
        }
    }
    return false;
}

//전년도 활동을 골라 볼 수 있는 select 등장
function createBeforeYearsDropDown(nowYear, startYear) {
    let beforeYears = document.getElementById('beforeYears');
    beforeYears.style.display = 'inline-block';
    for (let i = nowYear; i >= startYear; i--) {
        let beforeOneYear = document.createElement('option');
        beforeOneYear.value = i + "";
        beforeOneYear.textContent = i + "";
        beforeYears.appendChild(beforeOneYear);
    }
}

//선택한 연도의 활동 내역 보기
document.querySelectorAll('select').forEach(function (select) {
    select.addEventListener('change', function () {
        let selectedOption = this.options[this.selectedIndex];
        let beforeOneMonth = parseInt(selectedOption.value);

        while (calendar.childElementCount > 1) //맨 왼쪽에 써 있는 days 살리고 다 지우기
            calendar.removeChild(calendar.lastElementChild);

        for (let month = 0; month <= 11; month++)
            createCalendar(beforeOneMonth, month );
    });
});

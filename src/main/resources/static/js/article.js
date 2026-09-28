// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`삭제 요청 실패: ${response.status}`);
                }
                alert('삭제가 완료되었습니다.');
                location.replace('/articles');
            })
            .catch(() => {
                alert('삭제에 실패했습니다. 다시 시도해 주세요.');
            });
    })
}

// 수정 기능
// [1] id가 modify-btn인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    // [2] 클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({  // json 형식으로 변환
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`수정 요청 실패: ${response.status}`);
                }
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            })
    })
}

// 등록 기능
// [1] id가 create-btn인 엘리먼트
const createButton = document.getElementById("create-btn");

if (createButton) {
    // [2] 클릭 이벤트가 감지되면 생성 API 요청
    createButton.addEventListener("click", (event) => {
        fetch("/api/articles", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            }),
        }).then((response) => {
            if (!response.ok) {
                throw new Error(`생성 요청 실패: ${response.status}`);
            }
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        })
    })
}
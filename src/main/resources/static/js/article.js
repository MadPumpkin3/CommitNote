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

// newArticle.html 에 있는 AI 도움받기 버튼 클릭 시, 입력칸에 있는 데이터를 가져와 API를 호출해 AI 제안을 받는 JS코드
// [1] id가 ai-assist-btn인 엘리먼트
const aiAssistButton = document.getElementById("ai-assist-btn")

if (aiAssistButton) {
    // [2] 클릭 이벤트가 감지되면 모달을 열고 이전 제안 숨기기
    aiAssistButton.addEventListener('click', event => {
        $('#aiAssistModal').modal('show');
        document.getElementById('ai-suggestion').style.display = 'none';
        document.getElementById('ai-question').value = '';
    });
}

// [3] id가 get-suggestion-btn인 엘리먼트 (모달에 있는 버튼)
const getSuggestionButton = document.getElementById('get-suggestion-btn');

if (getSuggestionButton) {
    // [4] 클릭 이벤트가 감지되면 작성한 내용을 바탕으로 글 작성 도움 API 호출
    getSuggestionButton.addEventListener('click', event => {
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        const question = document.getElementById('ai-question').value;

        if (!question.trim()) {
            alert('고민되는 내용을 입력해주세요.')
            return;
        }

        document.getElementById('ai-loading').style.display = 'block';
        document.getElementById('ai-suggestion').style.display = 'none';

        const body = JSON.stringify({
            title: title,
            content: content,
            question: question
        })

        fetch('/api/ai-suggestions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: body
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`ai 요청 실패: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('ai-loading').style.display = 'none';
                const suggestionContent = document.getElementById('ai-suggestion-content');

                // [5] 응답이 오면 제안 뷰 표시
                let html = '';
                if (data.suggestions && data.suggestions.length > 0) {
                    html += '<ul class="list-group">';
                    data.suggestions.forEach((suggestion, index) => {
                        html += `<li class="list-group-item suggestion-item"
                                style="cursor: pointer;"
                                data-suggestion="${suggestion.replace(/"/g, '&quot;')}"
                                title="클릭하면 본문에 추가됩니다">
                                ${suggestion}
                                <small class="text-muted float-right">클릭하여 추가</small>
                        </li>`;
                    });
                    html += '</ul>';
                }

                suggestionContent.innerHTML = html;
                document.getElementById('ai-suggestion').style.display = 'block';
            })
    });
}

// [6] 제안 선택을 누르면 현재 내용 끝에 제안을 추가
const suggestionContent = document.getElementById('ai-suggestion-content');
if (suggestionContent) {
    suggestionContent.addEventListener('click', function(e) {
        const suggestionItem = e.target.closest('.suggestion-item');
        if (suggestionItem) {
            const suggestion = suggestionItem.getAttribute('data-suggestion');
            const contentTextarea = document.getElementById('content');

            const currentContent = contentTextarea.value;
            const separator = currentContent && !currentContent.endsWith('\n') ? '\n\n' : '';
            contentTextarea.value = currentContent + separator + suggestion;
            $('#aiAssistModal').modal('hide');
            contentTextarea.focus();
        }
    });
}
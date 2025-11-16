// ========================================
// TOPCIT 게임 시스템
// ========================================

console.log('🎮 TOPCIT Quest 시작!');

// 게임 상태 관리
let gameState = {
    playerLevel: 5,
    playerEXP: 0,
    playerMaxEXP: 100,
    currentProblem: null,
    score: 0,
    defeatedEnemies: 0
};

// 문제 데이터베이스
const problems = [
    {
        id: 1,
        title: "Solve the equation:",
        content: "x² - 3x + 2 = 0",
        description: "위의 이차방정식을 풀어보세요. 근의 공식을 사용하거나 인수분해를 통해 해를 구할 수 있습니다.",
        answer: ["x=1, x=2", "x=2, x=1", "1, 2", "2, 1", "1,2", "2,1"],
        hint: "이 방정식은 (x-1)(x-2) = 0 으로 인수분해할 수 있습니다.",
        exp: 30,
        // 백엔드에서 제공할 오답 피드백 데이터
        wrongFeedback: {
            chapter: "1단원 - 수학 기초",
            topic: "이차방정식 풀이",
            detail: "이차방정식 x² - 3x + 2 = 0은 인수분해를 통해 (x-1)(x-2) = 0으로 나타낼 수 있습니다. 따라서 x = 1 또는 x = 2가 해가 됩니다. 근의 공식을 사용할 경우: x = (3 ± √(9-8))/2 = (3 ± 1)/2이므로 x = 1 또는 x = 2입니다."
        }
    },
    {
        id: 2,
        title: "프로그래밍 문제:",
        content: "배열의 평균을 구하는 함수의 시간 복잡도는?",
        description: "크기가 n인 배열의 모든 원소를 더하고 n으로 나누어 평균을 구하는 알고리즘의 시간 복잡도를 빅오 표기법으로 나타내세요.",
        answer: ["O(n)", "o(n)", "O(N)", "o(N)"],
        hint: "배열의 모든 원소를 한 번씩 방문해야 합니다.",
        exp: 25,
        wrongFeedback: {
            chapter: "2단원 - 알고리즘",
            topic: "시간 복잡도 분석",
            detail: "배열을 순회하는 알고리즘의 시간 복잡도는 배열의 크기 n에 비례합니다. 배열의 모든 원소를 한 번씩 방문하므로 O(n)의 시간 복잡도를 가집니다. 상수 시간에 실행되는 연산(덧셈, 나눗셈)은 빅오 표기법에서 무시됩니다."
        }
    },
    {
        id: 3,
        title: "데이터베이스 문제:",
        content: "SELECT * FROM users WHERE age > 20",
        description: "위 SQL 쿼리의 결과는 무엇을 반환하나요?",
        answer: ["나이가 20보다 큰", "age > 20", "20 초과", "20보다 큰"],
        hint: "WHERE 절은 조건을 만족하는 행만 선택합니다.",
        exp: 20,
        wrongFeedback: {
            chapter: "3단원 - 데이터베이스",
            topic: "SQL SELECT 문",
            detail: "WHERE 절은 특정 조건을 만족하는 행만 선택하는 데 사용됩니다. 'age > 20'은 age 컬럼의 값이 20보다 큰 모든 레코드를 선택합니다. 만약 20 이상을 선택하려면 'age >= 20'을 사용해야 합니다."
        }
    }
];

let currentProblemIndex = 0;

// ========================================
// GIF 효과 함수
// ========================================
function flashGif(color = 'white') {
    const gif = document.getElementById('game-gif');
    if (!gif) return;
    
    gif.style.transition = 'filter 0.3s';
    
    if (color === 'green') {
        gif.style.filter = 'brightness(1.5) saturate(1.3)';
    } else if (color === 'red') {
        gif.style.filter = 'brightness(0.7) hue-rotate(330deg)';
    } else {
        gif.style.filter = 'brightness(1.3)';
    }
    
    setTimeout(() => {
        gif.style.filter = 'brightness(1)';
    }, 300);
}

function shakeGif() {
    const gameWorld = document.querySelector('.game-world');
    if (!gameWorld) return;
    
    gameWorld.style.animation = 'shake 0.5s';
    setTimeout(() => {
        gameWorld.style.animation = 'none';
    }, 500);
}

// ========================================
// 경험치 획득 화면 표시
// ========================================
function showExpGain(expAmount) {
    const overlay = document.getElementById('exp-gain-overlay');
    const expAmountElement = document.getElementById('exp-amount');
    
    if (!overlay || !expAmountElement) return;
    
    // 경험치 금액 설정
    expAmountElement.textContent = '+' + expAmount;
    
    // 화면 표시
    overlay.classList.add('show');
    
    // 2초 후 숨김
    setTimeout(() => {
        overlay.classList.remove('show');
    }, 2000);
}

// ========================================
// 오답 피드백 표시
// ========================================
function showWrongFeedback(feedbackData) {
    const section = document.getElementById('wrong-feedback-section');
    const chapterElement = document.getElementById('feedback-chapter');
    const topicElement = document.getElementById('feedback-topic');
    const detailElement = document.getElementById('feedback-detail');
    
    if (!section) return;
    
    // 백엔드 데이터 설정
    chapterElement.textContent = feedbackData.chapter;
    topicElement.textContent = feedbackData.topic;
    detailElement.textContent = feedbackData.detail;
    
    // 화면 표시
    section.classList.add('show');
}

// ========================================
// 오답 피드백 숨김
// ========================================
function hideWrongFeedback() {
    const section = document.getElementById('wrong-feedback-section');
    if (section) {
        section.classList.remove('show');
    }
}

// ========================================
// 페이지 로드 시 초기화
// ========================================
document.addEventListener('DOMContentLoaded', function() {
    console.log('📄 페이지 로드 완료!');
    
    loadProblem(currentProblemIndex);
    updateUI();
    
    // CSS 애니메이션 추가
    addAnimationStyles();
    
    console.log('🚀 게임 준비 완료!');
});

// ========================================
// 게임 로직 함수들
// ========================================

function loadProblem(index) {
    if (index >= problems.length) {
        showCompletionMessage();
        return;
    }
    
    const problem = problems[index];
    gameState.currentProblem = problem;
    
    document.getElementById('problem-title').textContent = problem.title;
    document.getElementById('problem-content').textContent = problem.content;
    document.getElementById('problem-description').textContent = problem.description;
    document.getElementById('answer-input').value = '';
    
    // 오답 피드백 숨김
    hideWrongFeedback();
}

function updateUI() {
    document.getElementById('player-level').textContent = gameState.playerLevel;
}

function submitAnswer() {
    console.log('📝 답안 제출!');
    const userAnswer = document.getElementById('answer-input').value.trim();
    
    if (userAnswer === '') {
        alert('답안을 입력해주세요!');
        return;
    }
    
    const problem = gameState.currentProblem;
    const isCorrect = problem.answer.some(answer => 
        userAnswer.toLowerCase().includes(answer.toLowerCase())
    );
    
    console.log('정답 여부:', isCorrect);
    
    if (isCorrect) {
        handleCorrectAnswer(problem);
    } else {
        handleIncorrectAnswer(problem);
    }
}

function handleCorrectAnswer(problem) {
    console.log('✅ 정답!');
    
    // 오답 피드백 숨김
    hideWrongFeedback();
    
    // 경험치 획득
    gameState.playerEXP += problem.exp;
    gameState.defeatedEnemies++;
    
    if (gameState.playerEXP >= gameState.playerMaxEXP) {
        levelUp();
    }
    
    gameState.score += 10;
    updateUI();
    
    // GIF 효과
    flashGif('green');
    
    // 경험치 획득 화면 표시
    showExpGain(problem.exp);
    
    // 2.5초 후 다음 문제로 이동
    setTimeout(() => {
        currentProblemIndex++;
        loadProblem(currentProblemIndex);
    }, 2500);
}

function handleIncorrectAnswer(problem) {
    console.log('❌ 오답!');
    
    // GIF 효과
    flashGif('red');
    shakeGif();
    
    // 오답 피드백 표시 (백엔드에서 받아올 데이터)
    // 실제로는 서버 API 호출: fetch('/api/feedback', { problemId: problem.id })
    showWrongFeedback(problem.wrongFeedback);
}

function levelUp() {
    gameState.playerLevel++;
    gameState.playerEXP = gameState.playerEXP - gameState.playerMaxEXP;
    gameState.playerMaxEXP = Math.floor(gameState.playerMaxEXP * 1.5);
    
    updateUI();
    flashGif('white');
    
    alert('🎉 레벨업! LV ' + gameState.playerLevel);
}

function getHint() {
    const problem = gameState.currentProblem;
    if (problem && problem.hint) {
        alert('💡 힌트: ' + problem.hint);
    }
}

function skipProblem() {
    if (confirm('이 문제를 건너뛰시겠습니까? 경험치를 얻을 수 없습니다.')) {
        hideWrongFeedback();
        
        setTimeout(() => {
            currentProblemIndex++;
            loadProblem(currentProblemIndex);
        }, 500);
    }
}

function showCompletionMessage() {
    alert(`🎓 모든 문제를 완료했습니다!
    
최종 레벨: ${gameState.playerLevel}
총 점수: ${gameState.score}
처치한 적: ${gameState.defeatedEnemies}마리

TOPCIT 준비가 ${gameState.playerLevel >= 10 ? '완료' : '진행 중'}되었습니다!`);
}

// ========================================
// 애니메이션 스타일 추가
// ========================================
function addAnimationStyles() {
    const style = document.createElement('style');
    style.textContent = `
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            50% { transform: translateX(10px); }
            75% { transform: translateX(-10px); }
        }
    `;
    document.head.appendChild(style);
}

// Enter 키로 답안 제출
document.getElementById('answer-input')?.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        submitAnswer();
    }
});

console.log('✅ 스크립트 로드 완료!');
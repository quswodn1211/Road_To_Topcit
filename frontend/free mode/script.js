// 게임 상태 관리
let gameState = {
    playerLevel: 5,
    playerEXP: 60,
    playerMaxEXP: 100,
    currentProblem: null,
    score: 0,
    defeatedEnemies: 0
};

// 문제 데이터베이스 (예시)
const problems = [
    {
        id: 1,
        title: "Solve the equation:",
        content: "x² - 3x + 2 = 0",
        description: "위의 이차방정식을 풀어보세요. 근의 공식을 사용하거나 인수분해를 통해 해를 구할 수 있습니다.",
        answer: ["x=1, x=2", "x=2, x=1", "1, 2", "2, 1"],
        hint: "이 방정식은 (x-1)(x-2) = 0 으로 인수분해할 수 있습니다.",
        exp: 20
    },
    {
        id: 2,
        title: "프로그래밍 문제:",
        content: "배열의 평균을 구하는 함수의 시간 복잡도는?",
        description: "크기가 n인 배열의 모든 원소를 더하고 n으로 나누어 평균을 구하는 알고리즘의 시간 복잡도를 빅오 표기법으로 나타내세요.",
        answer: ["O(n)", "o(n)", "O(N)", "o(N)"],
        hint: "배열의 모든 원소를 한 번씩 방문해야 합니다.",
        exp: 25
    },
    {
        id: 3,
        title: "데이터베이스 문제:",
        content: "SELECT * FROM users WHERE age > 20",
        description: "위 SQL 쿼리의 결과는 무엇을 반환하나요?",
        answer: ["나이가 20보다 큰 모든 사용자", "나이가 20 초과인 사용자들", "age > 20인 레코드"],
        hint: "WHERE 절은 조건을 만족하는 행만 선택합니다.",
        exp: 15
    }
];

let currentProblemIndex = 0;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    loadProblem(currentProblemIndex);
    updateUI();
});

// 문제 로드
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
    
    // 피드백 영역 숨기기
    const feedbackArea = document.getElementById('feedback-area');
    feedbackArea.style.display = 'none';
}

// UI 업데이트
function updateUI() {
    // 레벨 업데이트
    document.getElementById('player-level').textContent = gameState.playerLevel;
    
    // EXP 바 업데이트
    const expPercentage = (gameState.playerEXP / gameState.playerMaxEXP) * 100;
    document.querySelector('.exp-bar').style.width = expPercentage + '%';
}

// 답안 제출
function submitAnswer() {
    const userAnswer = document.getElementById('answer-input').value.trim();
    
    if (userAnswer === '') {
        showFeedback(false, '답안을 입력해주세요!');
        return;
    }
    
    const problem = gameState.currentProblem;
    const isCorrect = problem.answer.some(answer => 
        userAnswer.toLowerCase().includes(answer.toLowerCase())
    );
    
    if (isCorrect) {
        handleCorrectAnswer(problem);
    } else {
        handleIncorrectAnswer();
    }
}

// 정답 처리
function handleCorrectAnswer(problem) {
    showFeedback(true, '🎉 정답입니다! 경험치 +' + problem.exp);
    
    // 경험치 획득
    gameState.playerEXP += problem.exp;
    
    // 적 처치
    gameState.defeatedEnemies++;
    
    // 레벨업 체크
    if (gameState.playerEXP >= gameState.playerMaxEXP) {
        levelUp();
    }
    
    gameState.score += 10;
    updateUI();
    
    // 플레이어가 몬스터를 공격하는 애니메이션
    animatePlayerAttack();
    
    // 다음 문제로 이동
    setTimeout(() => {
        currentProblemIndex++;
        loadProblem(currentProblemIndex);
    }, 3000);
}

// 오답 처리
function handleIncorrectAnswer() {
    showFeedback(false, '❌ 틀렸습니다. 다시 도전해보세요!');
    
    // 몬스터가 플레이어를 공격하는 애니메이션
    animateMonsterAttack();
}

// 피드백 표시
function showFeedback(isCorrect, message) {
    const feedbackArea = document.getElementById('feedback-area');
    const feedbackIcon = document.getElementById('feedback-icon');
    const feedbackMessage = document.getElementById('feedback-message');
    
    feedbackArea.className = 'feedback-area';
    feedbackArea.classList.add(isCorrect ? 'correct' : 'incorrect');
    
    feedbackIcon.textContent = isCorrect ? '✓' : '✗';
    feedbackMessage.textContent = message;
    
    feedbackArea.style.display = 'block';
}

// 레벨업
function levelUp() {
    gameState.playerLevel++;
    gameState.playerEXP = gameState.playerEXP - gameState.playerMaxEXP;
    gameState.playerMaxEXP = Math.floor(gameState.playerMaxEXP * 1.5);
    
    showFeedback(true, '🎉 레벨업! LV ' + gameState.playerLevel);
}

// 플레이어 공격 애니메이션 (정답 시)
function animatePlayerAttack() {
    const player = document.getElementById('player');
    const monster = document.getElementById('monster');
    const attackEffect = document.getElementById('attack-effect');
    
    // 1. 플레이어가 앞으로 돌진
    player.style.transition = 'transform 0.4s ease-out';
    player.style.transform = 'translateX(300px) translateY(-20px)';
    
    setTimeout(() => {
        // 2. 공격 이펙트 표시
        const monsterRect = monster.getBoundingClientRect();
        const gameRect = document.querySelector('.game-world').getBoundingClientRect();
        
        attackEffect.style.left = (monsterRect.left - gameRect.left + monsterRect.width / 2) + 'px';
        attackEffect.style.top = (monsterRect.top - gameRect.top + monsterRect.height / 2) + 'px';
        attackEffect.style.animation = 'attack-burst 0.6s ease-out';
        
        // 3. 몬스터가 맞음
        monster.style.animation = 'monster-hit 0.6s ease-out';
        monster.style.filter = 'brightness(1.5) hue-rotate(90deg)';
        
    }, 400);
    
    setTimeout(() => {
        // 4. 몬스터 쓰러짐
        monster.style.transition = 'all 0.8s ease-out';
        monster.style.transform = 'translateY(100px) rotate(90deg) scale(0.8)';
        monster.style.opacity = '0.3';
        
    }, 800);
    
    setTimeout(() => {
        // 5. 플레이어 원위치
        player.style.transform = 'translateX(0) translateY(0)';
        
    }, 1200);
    
    setTimeout(() => {
        // 6. 몬스터 리셋 (다음 문제를 위해)
        monster.style.transition = 'none';
        monster.style.animation = 'monster-idle 2.5s ease-in-out infinite';
        monster.style.transform = 'translateY(0) rotate(0deg) scale(1)';
        monster.style.opacity = '1';
        monster.style.filter = 'none';
        attackEffect.style.animation = 'none';
        
    }, 2500);
}

// 몬스터 공격 애니메이션 (오답 시)
function animateMonsterAttack() {
    const player = document.getElementById('player');
    const monster = document.getElementById('monster');
    const attackEffect = document.getElementById('attack-effect');
    
    // 1. 몬스터가 플레이어 쪽으로 이동
    monster.style.transition = 'transform 0.5s ease-out';
    monster.style.transform = 'translateX(-250px) scale(1.3)';
    
    setTimeout(() => {
        // 2. 몬스터가 입을 벌림 (플레이어를 물려는 모션)
        const monsterMouth = monster.querySelector('.monster-mouth');
        monsterMouth.style.transition = 'all 0.3s';
        monsterMouth.style.transform = 'translateX(-50%) scaleY(1.5)';
        
        // 3. 플레이어가 놀람
        player.style.animation = 'player-scared 0.5s ease-out';
        player.style.filter = 'brightness(0.7)';
        
        // 4. 공격 이펙트 (플레이어 위치)
        const playerRect = player.getBoundingClientRect();
        const gameRect = document.querySelector('.game-world').getBoundingClientRect();
        
        attackEffect.style.left = (playerRect.left - gameRect.left + playerRect.width / 2) + 'px';
        attackEffect.style.top = (playerRect.top - gameRect.top + playerRect.height / 2) + 'px';
        attackEffect.style.animation = 'attack-burst 0.6s ease-out';
        attackEffect.style.background = 'radial-gradient(circle, rgba(231, 76, 60, 0.8), transparent)';
        
    }, 500);
    
    setTimeout(() => {
        // 5. 몬스터 원위치
        monster.style.transform = 'translateX(0) scale(1)';
        
        const monsterMouth = monster.querySelector('.monster-mouth');
        monsterMouth.style.transform = 'translateX(-50%) scaleY(1)';
        
    }, 1200);
    
    setTimeout(() => {
        // 6. 플레이어 원상복구
        player.style.animation = 'player-idle 2s ease-in-out infinite';
        player.style.filter = 'none';
        attackEffect.style.animation = 'none';
        attackEffect.style.background = 'radial-gradient(circle, rgba(255, 215, 0, 0.8), transparent)';
        
    }, 1500);
}

// 힌트 보기
function getHint() {
    const problem = gameState.currentProblem;
    if (problem && problem.hint) {
        showFeedback(true, '💡 힌트: ' + problem.hint);
        
        // 힌트 사용 시 경험치 약간 감소
        gameState.playerEXP = Math.max(0, gameState.playerEXP - 5);
        updateUI();
    }
}

// 문제 건너뛰기
function skipProblem() {
    if (confirm('이 문제를 건너뛰시겠습니까? 경험치를 얻을 수 없습니다.')) {
        showFeedback(false, '문제를 건너뛰었습니다.');
        
        updateUI();
        
        setTimeout(() => {
            currentProblemIndex++;
            loadProblem(currentProblemIndex);
        }, 1500);
    }
}

// 완료 메시지
function showCompletionMessage() {
    const feedbackArea = document.getElementById('feedback-area');
    feedbackArea.className = 'feedback-area correct';
    feedbackArea.style.display = 'block';
    
    document.getElementById('feedback-icon').textContent = '🎓';
    document.getElementById('feedback-message').innerHTML = 
        `<strong>모든 문제를 완료했습니다!</strong><br>
        최종 레벨: ${gameState.playerLevel} | 총 점수: ${gameState.score}<br>
        처치한 적: ${gameState.defeatedEnemies}마리<br>
        TOPCIT 준비가 ${gameState.playerLevel >= 10 ? '완료' : '진행 중'}되었습니다!`;
    
    document.querySelector('.problem-box').style.display = 'none';
    document.querySelector('.answer-box').style.display = 'none';
}

// 애니메이션 스타일 추가
const style = document.createElement('style');
style.textContent = `
    @keyframes attack-burst {
        0% {
            transform: translate(-50%, -50%) scale(0);
            opacity: 1;
        }
        50% {
            transform: translate(-50%, -50%) scale(2);
            opacity: 0.8;
        }
        100% {
            transform: translate(-50%, -50%) scale(3);
            opacity: 0;
        }
    }
    
    @keyframes monster-hit {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(15px) rotate(5deg); }
        50% { transform: translateX(-15px) rotate(-5deg); }
        75% { transform: translateX(10px) rotate(3deg); }
    }
    
    @keyframes player-scared {
        0%, 100% { transform: translateX(0) scale(1); }
        25% { transform: translateX(-15px) scale(0.95); }
        50% { transform: translateX(-20px) scale(0.9); }
        75% { transform: translateX(-10px) scale(0.95); }
    }
    
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        25% { transform: translateX(-10px); }
        75% { transform: translateX(10px); }
    }
`;
document.head.appendChild(style);

// Enter 키로 답안 제출
document.getElementById('answer-input').addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        submitAnswer();
    }
});
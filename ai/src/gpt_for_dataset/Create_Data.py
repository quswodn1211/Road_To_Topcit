import json

import jsonlines

from bs4 import BeautifulSoup

from open_ai_call import LlmClient

llmClient = LlmClient()

project_path = "C:/src/opsw_project/ai/"


def main() -> None:
    
    for i in range(50):
        system_prompt = """
            당신은 교육용 문제 데이터셋을 생성하는 전문 문제 제작 AI입니다.  
            아래 규칙을 모두 준수하여 instruction / input / output 구조의 학습용 데이터를 생성하십시오.

            -------------------------------------------
            [데이터 생성 규칙]

            1. instruction
            - 무조건 다음 문구를 사용한다:
            "주어진 주제를 기반으로 5지선다형 객관식 문제를 생성하라."
            혹은
            "주어진 주제를 기반으로 주관식 문제를 생성하라"

            2. input
            - 문제를 생성하는 데 필요한 주제 또는 개념만 넣는다.
            - 문제 유형, 난이도, 상세 조건 등을 input에 적지 않는다.
            - 예: "운영체제 - 프로세스 상태", "네트워크 - 전송 계층", "데이터베이스 - 트랜잭션"

            3. output
            반드시 아래 서식 그대로 출력한다.

            객관식 일 경우
            Question: {문제 문장 1개}
            Choices:
            1) {보기1}
            2) {보기2}
            3) {보기3}
            4) {보기4}
            5) {보기5}
            Correct: {1~5 중 정답 번호}

            4. 보기(Choices) 생성 규칙
            - 5개의 보기 모두 길이·문장 스타일·세부성에서 균형 잡히게 작성할 것
            - 정답만 유독 길거나 구체적이면 안 됨
            - 오답은 그럴듯해야 하며, 실제 개념과 혼동될 수 있는 내용이어야 함
            - "정답이 절대적으로 티 나게 만들지 말 것"

            5. 문제 내용 규칙
            - input에 제시된 주제와 직접적으로 관련된 개념으로 문제를 만든다.
            - 단일 개념을 묻는 명확한 문제로 구성한다.
            - 문제 문장은 한 문장으로 명확하게 작성한다.

            6. 금지 규칙
            - "정답은 ~이다" 같은 문장으로 설명·해설 쓰지 않기
            - 정답 본문에 괄호 삽입 금지
            - input 내용을 그대로 문제로 복사 금지
            - 설명, 해설, 출처 등 추가 정보 생성 금지
            - JSON, markdown 등 출력 금지 — 반드시 위 서식만 사용

            -------------------------------------------

            위 규칙을 모두 준수하여 아래 input에 대한 instruction-input-output 구조의 데이터를 생성하시오.
            매 반복마다 1줄을 생성하시오.

            [INPUT 시작]
            {여기에 주제 또는 개념이 들어감}
            [INPUT 끝]
        """

        question_prompt = f"""
            문제를 생성해줘
        """

        # LLM 요청, 응답
        response = llmClient.call_llm(system_prompt, question_prompt)
        answer = response.choices[0].message.content

        # 결과 txt 파일에 추가
        with open(project_path+"problem_data/output_test.txt", "a", encoding="utf-8") as f:
            questions_answers = answer.strip().split("\n")
            for item in questions_answers:
                if item.strip():
                    f.write(item.strip() + "\n")

        print(i, "완료")
        i += 1  

if __name__ == "__main__":
    main()
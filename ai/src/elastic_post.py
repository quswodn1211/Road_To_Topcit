import requests
import json
import time


def main() -> None:
   
    # 개발자 유미 QnA JSON 파일
    file_path = "devyummi_qna.json"

    # 파일 열기
    with open(file_path, "r", encoding="utf-8") as file:
        data = json.load(file)

        for i, qna in enumerate(data):
            question_data = qna["Question"]
            answer_data = qna["Answer"]

            body_data = {
                "Question": question_data,
                "Answer": answer_data
            }

            response = requests.post("아이피:9200/qna_sparse/_doc", json=body_data)
            print(i, response.status_code)
            time.sleep(0.1)

if __name__ == "__main__":
    main()
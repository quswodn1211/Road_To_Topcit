import json
import re
project_path = "C:/src/opsw_project/ai/"

def convert_txt_to_jsonl(input_file, output_file):

    with open(input_file, "r", encoding="utf-8") as f:
        text = f.read()


    blocks = re.split(r'(?=instruction\s*:)', text)

    results = []

    for block in blocks:
        block = block.strip()
        if not block:
            continue

        i_match = re.search(r'^instruction\s*:\s*(.*)', block)
        if not i_match:
            continue
        instruction = i_match.group(1).strip()

        p_match = re.search(r'input\s*:\s*(.*)', block)
        if not p_match:
            continue
        input = p_match.group(1).strip()
        a_match = block[p_match.end():]
        answer = a_match[1:]

        print(p_match)
        results.append({
            "instruction": instruction,
            "input": input,
            "output": answer
        })

    # JSONL 저장
    with open(output_file, "w", encoding="utf-8") as f:
        for item in results:
            f.write(json.dumps(item, ensure_ascii=False) + "\n")

    print(f"완료: {output_file} 생성됨.")


if __name__ == "__main__":
    convert_txt_to_jsonl(project_path+"/problem_data/output_test.txt", project_path+"/problem_data/test.jsonl")

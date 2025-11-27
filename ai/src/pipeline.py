import json
import os

import torch
from datasets import Dataset, load_dataset
from peft import LoraConfig
from transformers import (AutoModelForCausalLM, AutoTokenizer,
                          BitsAndBytesConfig, TrainingArguments)
from trl import SFTTrainer


model_path = "../models/llama3-ko-alwayssaewoo_problem-adapter"

tokenizer = AutoTokenizer.from_pretrained(model_path, use_fast=False)
tokenizer.pad_token = tokenizer.eos_token

quant_config = BitsAndBytesConfig(
    load_in_4bit = True,
    bnb_4bit_quant_type = "nf4",
    bnb_4bit_compute_dtype = torch.bfloat16
)

device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
model = AutoModelForCausalLM.from_pretrained(
    model_path,
    quantization_config = quant_config
).to(device)

pipe = pipeline(
    task="text-generation",   # 텍스트 생성 파이프라인
    model=model,              # 로드한 LLaMA 등 모델
    tokenizer=tokenizer,      # 해당 모델의 토크나이저
    device_map="auto"         # 가능한 장치(GPU) 자동 선택
)

system_prompt = """
너는 TOPCIT 기반 챗봇이야 아래 조건을 따르도록 해

1. 한국어로 들어온 대답에는 한국어로 대답한다. (용어가 영어인 경우는 제외)
2. 모든 내용은 1000토큰 이내로 대답하나, 질문에 대한 내용은 전부 담을 수 있도록 할 것.
3. 대화체는 최대한 제거하고 질문에 대한 대답만을 생성할 것
4. TOPCIT을 제외한 모든 질문에는 대답하지 말 것
5. 질문을 인식하지 못했을 경우 재차 질문을 요구할 것
"""

user_prompt = """


"""
messages = [
    {"role": "system", "content": system_prompt},
    {"role": "user", "content": user_prompt}
]
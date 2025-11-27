from openai import OpenAI

class LlmClient():
    
    def __init__(self):
        
        self.client = OpenAI(
            api_key="",
        )
        self.model = "gpt-5.1"

    def call_llm(self, system_prompt: str, user_prompt: str) -> str:
        
        return self.client.chat.completions.create(
            model = self.model,
            messages = [
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt}
            ],
            seed=42
        )
    

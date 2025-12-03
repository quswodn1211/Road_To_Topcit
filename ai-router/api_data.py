from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from client import call_runpod

app = FastAPI()

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    response: str

@app.post("/ai/chat", response_model=ChatResponse)
async def chat_api(body: ChatRequest):

    user_msg = body.message
    if not user_msg:
        raise HTTPException(status_code=400, detail="message is required")

    result = call_runpod(user_msg)

    return ChatResponse(response=result)

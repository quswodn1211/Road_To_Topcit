import asyncio
import websockets

URL = "wss://a2u2mglnolktsnolktsn.proxy.runpod.net/ws"
API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhc3NzYSIsIm5hbWUiOiJqYWV3b28iLCJhZG1pbiI6dHJ1ZSwiaWF0IjoxNTE2MjM5MDIyfQ.Zm8kUhVG6a47XHN7YX1RQDTGA4dV4a7zuqOK48eCzEY"  


async def main():
    async with websockets.connect(
        URL,
        additional_headers={"Authorization": f"Bearer {API_TOKEN}"}
    ) as ws:
        print("connect")
        await ws.send("소프트웨어 공학에 대해 알려줘")
        reply = await ws.recv()
        print("Reply:", reply)

asyncio.run(main())
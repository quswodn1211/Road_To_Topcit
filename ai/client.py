import asyncio
import ssl
import websockets

URL = "wss://a2u2mglnolktsn-8000.proxy.runpod.net/ws"
API_TOKEN = "mysecret123"  


async def main():
    # TLS 설정
    ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS)
    ssl_context.check_hostname = False
    ssl_context.verify_mode = ssl.CERT_NONE

    headers = {"Authorization": f"Bearer {API_TOKEN}"}

    async with websockets.connect(
        URL,
        ssl=ssl_context,             # 🔥 TLS 적용
        additional_headers=headers,
        origin="*"                   # 필요할 경우만 사용
    ) as ws:
        print("Connected!")

        await ws.send("소프트웨어 공학에 대해 알려줘")
        reply = await ws.recv()
        print("Reply:", reply)

asyncio.run(main())
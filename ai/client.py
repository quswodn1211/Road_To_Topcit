import asyncio
import websockets

EC2_IP = "1njwm13rrg08je-20.proxy.runpod.net"
EC2_WS = f"ws://{EC2_IP}:8000/ws"  


async def main():
    async with websockets.connect(f"ws://{EC2_IP}:8000") as ws:
        await ws.send("소프트웨어 공학에 대해 알려줘")
        reply = await ws.recv()
        print("Reply:", reply)

asyncio.run(main())
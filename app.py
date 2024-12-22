import json
import logging
import asyncio
from enum import Enum
from fastapi import FastAPI, WebSocket
from typing import Any, Coroutine


class EventType(Enum):
    START = "START"
    UPDATE = "UPDATE"
    STOP = "STOP"


class TimerEvent:
    def __init__(self, event_type: EventType):
        self.remaining_time = None
        self.type = event_type

    def set_remaining_time(self, remaining_time: int):
        self.remaining_time = remaining_time

    def to_dict(self):
        d = {"type": str(self.type).split(".")[1]}
        if self.remaining_time is not None:
            d["remaining_time"] = self.remaining_time
        return d


class TimerEventEncoder(json.JSONEncoder):
    def default(self, o: Any):
        if isinstance(o, TimerEvent):
            return o.to_dict()
        return super().default(o)


class AsyncTimer:

    def __init__(self, remaining: int):
        self._on_stop_hook = None
        self.remaining = remaining
        self.task = None

    async def start(self, on_stop_hook: Coroutine[Any, Any, None]):
        self._on_stop_hook = on_stop_hook
        if self.task and not self.task.done():
            self.task.cancel()
        self.task = asyncio.create_task(self._countdown(on_stop_hook))

    async def reset(self):
        if not self.task or self.task.done():
            raise "Can't stop task. Because doesn't exist"
        if not self._on_stop_hook:
            raise "Can't reset task. On stop hook is None"
        self.task.cancel()
        await self.start(self._on_stop_hook)
        logger.info("Timer reset.")

    async def _countdown(self, on_stop_hook: Coroutine[Any, Any, None]):
        try:
            await asyncio.sleep(self.remaining)
            await on_stop_hook
        except asyncio.CancelledError:
            logger.info("Timer canceled.")


logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)
app = FastAPI()

timer = AsyncTimer(10)
sockets = set()


@app.websocket("/ws")
async def ws_servo_endpoint(websocket: WebSocket):
    await websocket.accept()
    try:
        while True:
            start_event = await websocket.receive_json()
            if start_event["type"] != str(EventType.START).split(".")[1]:
                continue
            logger.info("Received start event.")
            if timer.task and not timer.task.done():
                logger.info("Timer is already running")
                for socket in sockets:
                    update_event = TimerEvent(EventType.UPDATE)
                    update_event.set_remaining_time(timer.remaining)
                    await socket.send_text(json.dumps(update_event, cls=TimerEventEncoder))
                await timer.reset()
            else:
                await timer.start(send_stop_events())
            sockets.add(websocket)
    except Exception as e:
        logger.error(f"Error in WebSocket connection: {e}")
    finally:
        if websocket in sockets:
            sockets.remove(websocket)


@app.get("/")
async def root():
    return {"message": "Hello World"}


async def toggle_servo():
    pass


async def send_stop_events():
    stop_event = TimerEvent(EventType.STOP)
    logger.info("Send stop event")
    for ws in sockets:
        await ws.send_text(json.dumps(stop_event, cls=TimerEventEncoder))
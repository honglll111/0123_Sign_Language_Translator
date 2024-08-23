import paho.mqtt.client as mqtt
import base64
import cv2
import threading


# update_caption 함수 정의
def update_caption(input_text, output_text, is_final=False):
    if is_final:
        # 최종 문장 (파란색으로 처리)
        print(f"OutputText: {output_text}")
    else:
        # 중간 단어들 (검정색으로 처리)
        print(f"InputText: {input_text}")
class ImageMqttPublisher:
    def __init__(self, broker_ip="localhost", broker_port=1883, pub_topic_image="/camerapub",
                 pub_topic_text="/textpub"):
        self.broker_ip = broker_ip
        self.broker_port = broker_port
        self.pub_topic_image = pub_topic_image
        self.pub_topic_text = pub_topic_text
        self.client = None

    def connect(self):
        thread = threading.Thread(target=self._run, daemon=True)
        thread.start()

    def _run(self):
        self.client = mqtt.Client()
        self.client.on_connect = self._on_connect
        self.client.on_disconnect = self._on_disconnect
        self.client.connect(self.broker_ip, self.broker_port)
        self.client.loop_forever()

    def _on_connect(self, client, userdata, flags, rc):
        print("MQTT broker connected")
        self.send_initial_empty_list()
        self.client.subscribe(self.pub_topic_text)  # 메시지 구독

    def _on_disconnect(self, client, userdata, rc):
        print("MQTT broker disconnected")

    def _on_message(self, client, userdata, message):
        # 메시지 수신 처리
        self.on_message_arrived(message)
    def on_message_arrived(self, message):
        # 메시지 처리 로직
        message_text = message.payload.decode().strip()
        # 접두어로 최종 문장을 구분
        if message_text.startswith("FINAL_OUTPUT:"):
            output_text = message_text.replace("FINAL_OUTPUT:", "").strip()
            update_caption("", output_text, is_final=True)
        else:
            input_text = message_text
            update_caption(input_text, "", is_final=False)

    def disconnect(self):
        if self.client:
            self.client.disconnect()

    def send_base64(self, frame):
        if self.client is None or not self.client.is_connected():
            return
        retval, buffer = cv2.imencode(".jpg", frame)
        if not retval:
            print("Image encoding failed")
            return
        b64_bytes = base64.b64encode(buffer)
        self.client.publish(self.pub_topic_image, b64_bytes, retain=True)

    def send_text(self, text):
        if self.client is None or not self.client.is_connected():
            return
        self.client.publish(self.pub_topic_text, text, retain=True)

    def send_initial_empty_list(self):
        if self.client is None or not self.client.is_connected():
            return
        self.client.publish(self.pub_topic_text, "", retain=True)


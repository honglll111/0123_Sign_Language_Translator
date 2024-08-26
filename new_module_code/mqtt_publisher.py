import cv2
import paho.mqtt.client as mqtt
import base64
import threading

def update_caption(input_text, output_text, is_final=False):
    if is_final:
        print(f"OutputText: {output_text}")
    else:
        print(f"InputText: {input_text}")

class ImageMqttPublisher:
    def __init__(self, broker_ip="localhost", broker_port=1883, pub_topic_image="/camerapub", pub_topic_text="/textpub"):
        self.broker_ip = broker_ip
        self.broker_port = broker_port
        self.pub_topic_image = pub_topic_image
        self.pub_topic_text = pub_topic_text
        self.client = None
        self.last_sent_text = ""  # 마지막으로 전송된 텍스트 저장
        self.last_sent_final_output = ""  # 마지막으로 전송된 최종 문장 저장
        self.final_output_sent = False  # 최종 문장이 전송되었는지 여부

    def connect(self):
        thread = threading.Thread(target=self._run, daemon=True)
        thread.start()

    def _run(self):
        self.client = mqtt.Client()
        self.client.on_connect = self._on_connect
        self.client.on_disconnect = self._on_disconnect
        self.client.on_message = self._on_message  # 메시지 수신 콜백 설정
        self.client.connect(self.broker_ip, self.broker_port)
        self.client.loop_forever()

    def _on_connect(self, client, userdata, flags, rc):
        print("MQTT broker connected")
        self.send_initial_empty_list()
        self.client.subscribe(self.pub_topic_text)  # 메시지 구독

    def _on_disconnect(self, client, userdata, rc):
        print("MQTT broker disconnected")

    def _on_message(self, client, userdata, message):
        self.on_message_arrived(message)

    def on_message_arrived(self, message):
        message_text = message.payload.decode().strip()
        if message_text.startswith("FINAL_OUTPUT:"):
            output_text = message_text.replace("FINAL_OUTPUT:", "").strip()
            update_caption("", output_text, is_final=True)
        else:
            input_text = message_text
            update_caption(input_text, "", is_final=False)

    def disconnect(self):
        if self.client:
            self.client.disconnect()

    def send_text(self, text, is_final=False):
        if self.client is None or not self.client.is_connected():
            return

        if self.final_output_sent and not is_final:
            return

        if is_final:
            if text == self.last_sent_final_output:
                return  # 중복된 최종 문장인 경우 전송하지 않음
            self.last_sent_final_output = text  # 최종 문장 업데이트
            self.final_output_sent = True  # 최종 문장 전송 플래그 설정
        else:
            if text == self.last_sent_text:
                return  # 중복된 중간 텍스트인 경우 전송하지 않음
            self.last_sent_text = text  # 중간 텍스트 업데이트

        self.client.publish(self.pub_topic_text, text, retain=True)

    def send_base64(self, frame):
        if self.client is None or not self.client.is_connected():
            return
        retval, buffer = cv2.imencode(".jpg", frame)
        if not retval:
            print("Image encoding failed")
            return
        b64_bytes = base64.b64encode(buffer)
        self.client.publish(self.pub_topic_image, b64_bytes, retain=True)

    def send_initial_empty_list(self):
        if self.client is None or not self.client.is_connected():
            return
        self.client.publish(self.pub_topic_text, "", retain=True)



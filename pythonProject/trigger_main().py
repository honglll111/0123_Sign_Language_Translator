import paho.mqtt.client as mqtt
import threading
from main import main  # Import the main function from your existing script

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("/python/start")

def on_message(client, userdata, msg):
    if msg.topic == "/python/start" and msg.payload.decode() == "start":
        print("Received start signal. Running main() function...")
        # Run main() in a separate thread to avoid blocking the MQTT client
        threading.Thread(target=main).start()

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("localhost", 1883, 60)

# Start the MQTT client loop
client.loop_forever()
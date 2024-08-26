import requests

def send_llama(input_text, mqtt_publisher=None):
    url = 'https://9010-35-204-34-40.ngrok-free.app/generate'

    instruction = "You are a patient, sick person because you were sick. You receive medical treatment at the hospital. Explain where you are sick."

    data = {
        'instruction': instruction,
        'input': input_text
    }

    try:
        response = requests.post(url, json=data)
        response_data = response.json()
        if response.status_code == 200:
            output_text = response_data['output']
            print(output_text)
            if mqtt_publisher:
                mqtt_publisher.send_text(f"FINAL_OUTPUT: {output_text}")
        else:
            print(f"Error: {response.status_code}")
            print(response_data['error'])
    except requests.exceptions.JSONDecodeError:
        print("Failed to decode JSON response")
        print(response.text)

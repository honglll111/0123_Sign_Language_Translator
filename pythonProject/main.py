import cv2
import mediapipe as mp
import numpy as np
from tensorflow import keras
from landmark_tracker import LandmarkTracker
from mediapipe_utils import mediapipe_detection, draw_styled_landmarks, extract_keypoints
from mqtt_publisher import ImageMqttPublisher
from llama_client import send_llama


def main():
    video_capture = cv2.VideoCapture(0)
    mqtt_publisher = ImageMqttPublisher()
    mqtt_publisher.connect()

    model = keras.models.load_model('fcn-model-reference-wang2017-0100-0.9591.keras')
    actions = {0: 'CT', 1: '가끔', 2: '가능', 3: '가다', 4: '간(신체)', 5: '간호사', 6: '감사(고마움)', 7: '갑자기', 8: '걱정', 9: '건강',
               10: '걷다(걸음)', 11: '검사(검진)', 12: '결과(결말)', 13: '계속', 14: '고생', 15: '곳', 16: '관절', 17: '괜찮다', 18: '그냥',
               19: '그러면', 20: '그런데', 21: '근육', 22: '기능', 23: '기억', 24: '꼭', 25: '끝', 26: '나', 27: '나쁘다', 28: '나오다',
               29: '날(시간)', 30: '남다', 31: '너무', 32: '노력', 33: '높다', 34: '느낌', 35: '다니다', 36: '다음', 37: '다치다', 38: '다행',
               39: '달다(맛)', 40: '당뇨', 41: '더', 42: '동안(시간)', 43: '되다', 44: '듣다', 45: '디스크', 46: '따로', 47: '때(시간)',
               48: '때문에', 49: '떨어지다', 50: '또(and)', 51: '마시다', 52: '마음', 53: '막다', 54: '만(어미)', 55: '만나다', 56: '만들다',
               57: '만약', 58: '많다', 59: '맞다(옳다)', 60: '먹다', 61: '면(어미)', 62: '모두', 63: '모르다', 64: '몸', 65: '못하다',
               66: '무엇', 67: '문제', 68: '바꾸다', 69: '바쁘다', 70: '방문', 71: '변하다', 72: '병(질병)', 73: '병원', 74: '보다(구경)',
               75: '보통', 76: '부작용', 77: '부족', 78: '부탁', 79: '부터', 80: '불편', 81: '비교', 82: '사라지다', 83: '사용', 84: '사진',
               85: '상관(관여)없다', 86: '상태', 87: '생각', 88: '생기다', 89: '선생님', 90: '설명', 91: '수술', 92: '술(주류)', 93: '쉽다',
               94: '시간', 95: '시작', 96: '심장', 97: '심하다', 98: '아니다', 99: '아니면', 100: '아마', 101: '아직', 102: '아침',
               103: '아프다', 104: '안과(전문)', 105: '안내', 106: '안녕', 107: '안되다', 108: '알다', 109: '암', 110: '약(물질)',
               111: '어떻게', 112: '어렵다', 113: '어지럽다', 114: '없다', 115: '예약', 116: '오다', 117: '오래', 118: '요즘',
               119: '우리(나의무리)', 120: '우선', 121: '운동', 122: '움직이다', 123: '원래', 124: '원인', 125: '원하다', 126: '이유',
               127: '이해', 128: '일(업무)', 129: '입원', 130: '있다', 131: '잘', 132: '잠깐', 133: '저녁', 134: '전(시간)에', 135: '점수',
               136: '정도', 137: '정상(제대로)', 138: '조금', 139: '조심', 140: '조절', 141: '좋다', 142: '주다', 143: '주사(행동)',
               144: '줄이다', 145: '중(가운데)', 146: '중요', 147: '증상', 148: '지금', 149: '지내다', 150: '진행', 151: '집', 152: '참다',
               153: '충분', 154: '치료', 155: '콩팥', 156: '특별', 157: '편하다', 158: '피', 159: '필요', 160: '필요없다', 161: '하다',
               162: '하루', 163: '함께', 164: '항상', 165: '해(하다)보다', 166: '허리', 167: '혈당', 168: '확인', 169: '환자', 170: '회복',
               171: '후', 172: '힘들다'}

    sequence = []
    sentence = []
    threshold = 0.8

    tracker = LandmarkTracker()

    with mp.solutions.holistic.Holistic(min_detection_confidence=0.5, min_tracking_confidence=0.5) as holistic:
        while True:
            if video_capture.isOpened():
                ret, frame = video_capture.read()
                if not ret:
                    print("Video capture failed")
                    break

                # Draw landmarks
                image, results = mediapipe_detection(frame, holistic)

                # Check if dispose condition is met
                if tracker.dispose(results):
                    print("Dispose condition met: No landmarks for 10 seconds or only pose detected")
                    break

                # Extract keypoints
                draw_styled_landmarks(image, results)

                keypoints = extract_keypoints(results)
                if keypoints is not None:
                    sequence.append(keypoints)
                    sequence = sequence[-30:]

                    if len(sequence) == 30:
                        res = model.predict(np.expand_dims(sequence, axis=0))[0]
                        predicted_action = actions[np.argmax(res)]

                        if res[np.argmax(res)] > threshold:
                            if len(sentence) > 0 and predicted_action != sentence[-1]:
                                sentence.append(predicted_action)
                            elif not sentence:
                                sentence.append(predicted_action)

                            if len(sentence) > 10:
                                sentence = sentence[-10:]

                            # 중간 단어 전송 (검정색으로 표시될 단어들)
                            if "FINAL_OUTPUT:" not in ' '.join(sentence):
                                mqtt_publisher.send_text(' '.join(sentence))  # 중간 단어들을 전송합니다.
                                print(sentence)
                                sequence = []

                            # 최종 문장 전송 (Llama 모델을 통해 생성된 문장만 파란색으로 표시)
                            if "FINAL_OUTPUT:" in ' '.join(sentence):
                                final_sentence = ' '.join(sentence).replace("FINAL_OUTPUT:", "").strip()
                                mqtt_publisher.send_text(f"FINAL_OUTPUT: {final_sentence}",
                                                         is_final=True)  # Llama에서 생성된 최종 문장만 전송합니다.
                                break
                            print(sentence)
                            sequence = sequence[1:]

                mqtt_publisher.send_base64(image)

    send_llama(' '.join(sentence), mqtt_publisher)
    mqtt_publisher.disconnect()

    video_capture.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    main()
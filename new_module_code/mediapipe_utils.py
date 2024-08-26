import cv2
import mediapipe as mp
import numpy as np

mp_holistic = mp.solutions.holistic
mp_drawing = mp.solutions.drawing_utils

def mediapipe_detection(image, model):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image.flags.writeable = False
    results = model.process(image)
    image.flags.writeable = True
    return cv2.cvtColor(image, cv2.COLOR_RGB2BGR), results

def draw_styled_landmarks(image, results):
    pose_landmarks = [0, 2, 5, 7, 8, 11, 12, 13, 14, 15, 16, 23, 24]
    pose_connections = [
        (0, 2), (0, 5), (2, 7), (5, 8), (11, 12), (11, 23),
        (12, 24), (23, 24), (11, 13), (13, 15), (12, 14), (14, 16)
    ]

    if results.pose_landmarks:
        for connection in pose_connections:
            start_idx, end_idx = connection
            start_landmark = results.pose_landmarks.landmark[start_idx]
            end_landmark = results.pose_landmarks.landmark[end_idx]
            h, w, _ = image.shape
            x1, y1 = int(start_landmark.x * w), int(start_landmark.y * h)
            x2, y2 = int(end_landmark.x * w), int(end_landmark.y * h)
            cv2.line(image, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.circle(image, (x1, y1), 3, (255, 0, 255), -1)
            cv2.circle(image, (x2, y2), 3, (255, 0, 255), -1)

    if results.left_hand_landmarks:
        mp_drawing.draw_landmarks(image, results.left_hand_landmarks, mp_holistic.HAND_CONNECTIONS)

    if results.right_hand_landmarks:
        mp_drawing.draw_landmarks(image, results.right_hand_landmarks, mp_holistic.HAND_CONNECTIONS)

    return image, results

def extract_keypoints(results):
    if results.pose_landmarks and (results.left_hand_landmarks or results.right_hand_landmarks):
        pose_landmarks = [0, 2, 5, 7, 8, 11, 12, 13, 14, 15, 16, 23, 24]
        pose = np.array([[results.pose_landmarks.landmark[idx].x, results.pose_landmarks.landmark[idx].y] for idx in pose_landmarks]).flatten() if results.pose_landmarks else np.zeros(13 * 2)
        lh = np.array([[res.x, res.y] for res in results.left_hand_landmarks.landmark]).flatten() if results.left_hand_landmarks else np.zeros(21*2)
        rh = np.array([[res.x, res.y] for res in results.right_hand_landmarks.landmark]).flatten() if results.right_hand_landmarks else np.zeros(21*2)
        return np.concatenate([pose, lh, rh])
    else:
        return None

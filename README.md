# Criminal Face Detection System

An academic face recognition prototype that uses DeepFace, OpenCV, Python, Java Swing, and MySQL to compare a query image against a registered fictional dataset and retrieve the corresponding database record.

> This project is developed for academic demonstration purposes using fictional/test identities. A face match should not be treated as proof of criminal identity or criminal activity.

## Features

- Select a face image through a Java Swing interface
- Face detection and recognition using DeepFace
- Face embeddings using FaceNet
- RetinaFace for face detection
- Comparison against multiple registered subjects
- Average-distance based matching across multiple reference images
- MySQL database integration
- Java Swing frontend with Python backend integration
- Displays matched subject details

## Technologies Used

- Python 3.11
- DeepFace
- TensorFlow
- FaceNet
- RetinaFace
- OpenCV
- Java
- Java Swing
- MySQL
- MySQL Connector/Python

## Project Structure

```text
CriminalFaceDetection/
│
├── dataset/
│   ├── CR-001/
│   ├── CR-002/
│   └── CR-003/
│
├── java-frontend/
│   └── CriminalFaceDetectionUI.java
│
└── python-backend/
    ├── face_search.py
    ├── test_cr002.py
    ├── test_cr003.py
    ├── test_face.py
    └── test_mysql.py

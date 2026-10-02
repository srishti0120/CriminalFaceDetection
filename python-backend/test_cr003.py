from deepface import DeepFace

image1 = "dataset/CR-003/face1 (1).jpg"
image2 = "dataset/CR-003/face1 (2).jpg"

print("Starting CR-003 face verification...")

result = DeepFace.verify(
    img1_path=image1,
    img2_path=image2,
    model_name="Facenet",
    detector_backend="retinaface"
)

print("\nVerification Result:")
print(result)

if result["verified"]:
    print("\nMATCH FOUND")
else:
    print("\nNO MATCH")
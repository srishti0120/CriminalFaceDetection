from deepface import DeepFace

image1 = "dataset/CR-001/face1.jpg"
image2 = "dataset/CR-001/face2.jpg"

print("Starting face verification...")

result = DeepFace.verify(
    img1_path=image1,
    img2_path=image2,
    model_name="Facenet",
    detector_backend="opencv"
)

print("\nVerification Result:")
print(result)

if result["verified"]:
    print("\nMATCH FOUND")
else:
    print("\nNO MATCH")
from deepface import DeepFace
import mysql.connector
import os
import sys

# ---------------------------------------
# 1. Get input image
# ---------------------------------------

if len(sys.argv) > 1:
    query_image = sys.argv[1]
else:
    query_image = input(
        "Enter the path of the image to search: "
    ).strip().strip('"')

if not os.path.exists(query_image):
    print("Image not found!")
    sys.exit()

# ---------------------------------------
# 2. Database connection
# ---------------------------------------

connection = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Chichu@2706",
    database="criminal_detection"
)

cursor = connection.cursor(dictionary=True)

# ---------------------------------------
# 3. Compare all registered images
# ---------------------------------------

dataset_path = "dataset"

subjects = ["CR-001", "CR-002", "CR-003"]

subject_distances = {}

print("\nSearching registered faces...\n")

for criminal_id in subjects:

    folder = os.path.join(dataset_path, criminal_id)

    if not os.path.exists(folder):
        continue

    distances = []

    for filename in os.listdir(folder):

        if not filename.lower().endswith(
            (".jpg", ".jpeg", ".png")
        ):
            continue

        registered_image = os.path.join(
            folder,
            filename
        )

        print(
            f"Comparing with {criminal_id} - {filename}"
        )

        try:

            result = DeepFace.verify(
                img1_path=query_image,
                img2_path=registered_image,
                model_name="Facenet",
                detector_backend="retinaface"
            )

            distance = result["distance"]

            print(f"Distance: {distance:.4f}")

            distances.append(distance)

        except Exception as error:

            print(
                f"Could not process {registered_image}"
            )

            print(error)

    if distances:

        average_distance = sum(distances) / len(distances)

        subject_distances[criminal_id] = average_distance

        print(
            f"Average distance for {criminal_id}: "
            f"{average_distance:.4f}\n"
        )

# ---------------------------------------
# 4. Find best identity
# ---------------------------------------

if not subject_distances:

    print("No usable face images found.")
    cursor.close()
    connection.close()
    sys.exit()

best_match = min(
    subject_distances,
    key=subject_distances.get
)

best_distance = subject_distances[best_match]

# ---------------------------------------
# 5. Match threshold
# ---------------------------------------

threshold = 0.40

if best_distance <= threshold:

    cursor.execute(
        "SELECT * FROM criminals WHERE criminal_id = %s",
        (best_match,)
    )

    record = cursor.fetchone()

    print("\n==============================")
    print("MATCH FOUND")
    print("==============================")

    print("Criminal ID :", record["criminal_id"])
    print("Name        :", record["name"])
    print("Age         :", record["age"])
    print("Gender      :", record["gender"])
    print("Crime       :", record["crime"])
    print("Average Distance :", round(best_distance, 4))

else:

    print("\n==============================")
    print("NO MATCH FOUND")
    print("==============================")

# ---------------------------------------
# 6. Close database
# ---------------------------------------

cursor.close()
connection.close()
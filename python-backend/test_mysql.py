import mysql.connector

try:
    connection = mysql.connector.connect(
        host="localhost",
        user="root",
        password="Chichu@2706",
        database="criminal_detection"
    )

    if connection.is_connected():
        print("MySQL connection successful!")

        cursor = connection.cursor()
        cursor.execute("SELECT * FROM criminals")

        rows = cursor.fetchall()

        print("\nRegistered records:")
        for row in rows:
            print(row)

        cursor.close()
        connection.close()

except mysql.connector.Error as error:
    print("MySQL connection failed!")
    print(error)
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CriminalFaceDetectionUI extends JFrame {

    private JLabel imageLabel;
    private JTextArea resultArea;
    private File selectedImage;

    public CriminalFaceDetectionUI() {

        setTitle("Criminal Face Detection System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel titleLabel = new JLabel(
                "Criminal Face Detection System",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(
                BorderFactory.createTitledBorder("Selected Image")
        );

        imageLabel = new JLabel(
                "No image selected",
                SwingConstants.CENTER
        );
        imageLabel.setPreferredSize(new Dimension(400, 350));

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(
                BorderFactory.createTitledBorder("Detection Result")
        );

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setEditable(false);

        resultPanel.add(
                new JScrollPane(resultArea),
                BorderLayout.CENTER
        );

        // Center section
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));

        centerPanel.add(imagePanel);
        centerPanel.add(resultPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JButton selectButton = new JButton("Select Image");
        JButton searchButton = new JButton("Search Face");
        JButton clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(selectButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Select Image button
        selectButton.addActionListener(e -> selectImage());

        // Search button
        searchButton.addActionListener(e -> searchFace());

        // Clear button
        clearButton.addActionListener(e -> clearScreen());

        add(mainPanel);
    }

    private void selectImage() {

        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            selectedImage = fileChooser.getSelectedFile();

            ImageIcon icon = new ImageIcon(
                    selectedImage.getAbsolutePath()
            );

            Image image = icon.getImage();

            Image scaledImage = image.getScaledInstance(
                    400,
                    350,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setText("");

            imageLabel.setIcon(
                    new ImageIcon(scaledImage)
            );

            resultArea.setText(
                    "Image selected:\n" +
                    selectedImage.getName() +
                    "\n\nClick 'Search Face' to continue."
            );
        }
    }

    private void searchFace() {

    if (selectedImage == null) {

        JOptionPane.showMessageDialog(
                this,
                "Please select an image first."
        );

        return;
    }

    resultArea.setText(
            "Searching...\n\n" +
            "Please wait while the face is being verified."
    );

    try {

        String pythonPath = "python";
        String scriptPath = "python-backend\\face_search.py";
        String imagePath = selectedImage.getAbsolutePath();

        ProcessBuilder processBuilder = new ProcessBuilder(
        pythonPath,
        "-X",
        "utf8",
        scriptPath,
        imagePath
);

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        java.io.BufferedReader reader =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                                process.getInputStream()
                        )
                );

        StringBuilder output = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();

String outputText = output.toString();

if (outputText.contains("MATCH FOUND")) {

    int start = outputText.indexOf("==============================");

    if (start >= 0) {
        outputText = outputText.substring(start);
    }

    resultArea.setText(outputText);

} else if (outputText.contains("NO MATCH FOUND")) {

    resultArea.setText(
            "==============================\n" +
            "       NO MATCH FOUND\n" +
            "=============================="
    );

} else {

    resultArea.setText(
            "Unable to process the image.\n\n" +
            outputText
    );
}

    } catch (Exception ex) {

        resultArea.setText(
                "Error while running face detection:\n\n" +
                ex.getMessage()
        );
    }
}

    private void clearScreen() {

        selectedImage = null;

        imageLabel.setIcon(null);
        imageLabel.setText("No image selected");

        resultArea.setText("");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CriminalFaceDetectionUI ui =
                    new CriminalFaceDetectionUI();

            ui.setVisible(true);
        });
    }
}
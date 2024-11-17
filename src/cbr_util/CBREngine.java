package cbr_util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CBREngine {
    private String myCBRUrl;

    public CBREngine(String myCBRUrl) {
        this.myCBRUrl = myCBRUrl;
    }

    public String retrieveCase(String jsonData) {
        try {
            URL url = new URL(myCBRUrl + "/retrieve");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } else {
                return "Error: " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    public void reviseCase(String caseId, String revisedData) {
        // Implementiere hier den Revise-Prozess
    }
}

package cbr_util;

    import com.google.gson.JsonObject;
    import com.google.gson.JsonParser;

    public class RetrievalHelper {
        public static String formatRequestData(String input) {
            // Hilfsmethode zum Erstellen der Anfrage als JSON-String
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("input", input);
            return jsonObject.toString();
        }

        public static void processResponse(String jsonResponse) {
            // Methode zur Verarbeitung der Antwort von myCBR
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            System.out.println("Verarbeitete Antwort: " + jsonObject);
        }
    }

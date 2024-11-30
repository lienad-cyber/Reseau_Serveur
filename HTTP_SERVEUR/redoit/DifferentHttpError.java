import com.sun.net.httpserver.HttpExchange;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DifferentHttpError {
    public void Error500(HttpExchange exchange, String message) throws IOException {
        String FilePathPageNotFound = "../../htdocs/Error/Error500.html";

        Path pathToFile = Paths.get(FilePathPageNotFound);
        byte[] fileContentBytes = Files.readAllBytes(pathToFile);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(500, fileContentBytes.length);

        // La reponse du servera
        try (
                OutputStream os = exchange.getResponseBody()) {
            os.write(fileContentBytes);
        }
    }

    public static  void Error404(DataOutputStream writer) throws IOException {
        writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
        writer.writeBytes("Content-Type: text/html\r\n");
        writer.writeBytes("\r\n");
        writer.writeBytes("<html><body><h1>Erreur 404 - Fichier non trouvé</h1></body></html>");
    }
}

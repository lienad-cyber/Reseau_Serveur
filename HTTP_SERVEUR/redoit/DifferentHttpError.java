import com.sun.net.httpserver.HttpExchange;
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

    public void Error404(HttpExchange exchange, String message) throws IOException {
        String FilePathPageNotFound = "../../htdocs/Error/Error404.html";

        Path pathToFile = Paths.get(FilePathPageNotFound);
        byte[] fileContentBytes = Files.readAllBytes(pathToFile);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(404, fileContentBytes.length);

        // La reponse du servera
        try (
                OutputStream os = exchange.getResponseBody()) {
            os.write(fileContentBytes);
        }
    }
}

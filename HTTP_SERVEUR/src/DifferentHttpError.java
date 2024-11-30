import java.io.DataOutputStream;
import java.io.IOException;

public class DifferentHttpError {
    public static  void Error500(DataOutputStream writer) throws IOException {
        writer.writeBytes("HTTP/1.1 500 Internal Server Error\r\n");
        writer.writeBytes("Content-Type: text/html\r\n");
        writer.writeBytes("\r\n");
        writer.writeBytes(
                "<html><body><h1>Erreur 500 - Erreur lors de l'exécution du fichier PHP.</h1></body></html>");
    }

    public static  void Error404(DataOutputStream writer) throws IOException {
        writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
        writer.writeBytes("Content-Type: text/html\r\n");
        writer.writeBytes("\r\n");
        writer.writeBytes("<html><body><h1>Erreur 404 - Fichier non trouvé</h1></body></html>");
    }
}

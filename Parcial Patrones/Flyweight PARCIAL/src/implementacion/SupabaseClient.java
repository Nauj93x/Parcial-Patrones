package implementacion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cliente mínimo para consultar la API REST de Supabase (tabla playlists).
 * Usa la API key (anon o service_role) para autenticarse.
 */
public class SupabaseClient {
    /**
     * Lista playlists mediante la REST API de Supabase.
     * projectUrl: por ejemplo https://<project>.supabase.co
     * apiKey: la API key provista (anon o service_role)
     */
    public static void listPlaylistsViaRest(String projectUrl, String apiKey) {
        if (projectUrl == null || projectUrl.isEmpty()) {
            System.out.println("[SupabaseClient] projectUrl vacía");
            return;
        }
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("[SupabaseClient] apiKey vacía");
            return;
        }

        try {
            // Construir endpoint REST para la tabla 'playlists'
            String endpoint = projectUrl;
            if (endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length() - 1);
            // Supabase REST endpoint por defecto: /rest/v1/<table>
            String urlStr = endpoint + "/rest/v1/playlists?select=name,usos,updated_at";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", apiKey);
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            System.out.println("[SupabaseClient] GET " + urlStr + " => HTTP " + code);
            BufferedReader br;
            if (code >= 200 && code < 300) br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            else br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            br.close();

            String body = sb.toString().trim();
            if (code >= 200 && code < 300) {
                System.out.println("\n╔══════════════════════════════════════════════╗");
                System.out.println("║  Playlists (Respuesta REST de Supabase)     ║");
                System.out.println("╠══════════════════════════════════════════════╣");
                if (body.isEmpty() || body.equals("[]")) {
                    System.out.println("║  (No hay registros o la tabla no está expuesta al REST)");
                } else {
                    // Imprimir cuerpo crudo (JSON)
                    // Para simplicidad, mostramos la respuesta JSON tal cual
                    System.out.println(body);
                }
                System.out.println("╚══════════════════════════════════════════════╝\n");
            } else {
                System.out.println("[SupabaseClient] Error en la consulta: " + body);
            }

        } catch (Exception e) {
            System.out.println("[SupabaseClient] Error llamando al REST: " + e.getMessage());
        }
    }
}

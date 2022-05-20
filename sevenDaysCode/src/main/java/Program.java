
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import apiKey.ApiKey;
import entities.Filme;

public class Program {

	private static final Logger log = LoggerFactory.getLogger(Program.class);

	public static void main(String[] args) throws Exception {

		try {

			HttpClient client = HttpClient.newBuilder().build();

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://imdb-api.com/en/API/Top250Movies/" + ApiKey.getApiKey()))
					.timeout(Duration.ofMinutes(1)).header("Content-Type", "application/json").GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200)
				throw new Exception("uri n�o encontrada");

			String body = response.body();
			JSONObject j = new JSONObject(body);
			String items = j.getString("items");

			Gson g = new Gson();

			Filme[] f = g.fromJson(items, Filme[].class);

			for (Filme filme : f) {
				System.out.println(filme);
			}

		} catch (Exception e) {
			throw new Exception("ERRO: " + e);
		}
	}

	public static List<String> campoFilter(List<String> objetos, String campo) {

		return objetos.stream().filter(s -> s.startsWith("\"" + campo + "\""))
				.map((s -> s.replace("\"" + campo + "\":", ""))).map(s -> s.replace("\"", ""))
				.map(s -> s.replace("}", "")).collect(Collectors.toList());
	}
}

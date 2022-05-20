
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apiKey.ApiKey;

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

			String bode = response.body();
			List<String> objetos = new ArrayList<>(List.of(bode.split(",")));

			List<String> titulo = campoFilter(objetos, "fullTitle");
			List<String> imagens = campoFilter(objetos, "image");
			List<String> imDbRatings = campoFilter(objetos, "imDbRating");
			List<String> years = campoFilter(objetos, "year");

			titulo.forEach(System.out::println);
			imagens.forEach(System.out::println);
			imDbRatings.forEach(System.out::println);
			years.forEach(System.out::println);

		} catch (Exception e) {
			throw new Exception("ERRO: " + e);
		}
	}

	public static List<String> campoFilter(List<String> objetos, String campo) {
		return objetos
				.stream()
				.filter(s -> s.startsWith("\"" + campo + "\""))
				.map((s -> s.replace("\"" + campo + "\":", "")))
				.map(s -> s.replace("\"", ""))
				.map(s -> s.replace("}", ""))
				.collect(Collectors.toList());
	}
}

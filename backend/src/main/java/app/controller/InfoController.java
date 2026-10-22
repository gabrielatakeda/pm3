package app.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint publico usado na demonstracao do balanceamento de carga no Kubernetes:
// cada resposta informa qual Pod atendeu a requisicao.
@RestController
@RequestMapping("/api/info")
public class InfoController {

	@GetMapping
	public ResponseEntity<Map<String, Object>> info() {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("pod", env("POD_NAME", env("HOSTNAME", "local")));
		body.put("podIp", env("POD_IP", "-"));
		body.put("node", env("NODE_NAME", "-"));
		body.put("hora", LocalDateTime.now().toString());
		return ResponseEntity.ok(body);
	}

	private static String env(String name, String fallback) {
		String value = System.getenv(name);
		return value == null || value.isBlank() ? fallback : value;
	}
}

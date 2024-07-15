package com.example.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController 
public class RestReqController {

	@Autowired
	private WebClient webClient;  // 브라우저 객체

	@RequestMapping("/java_service")
	public String testRestRequestImage(MultipartFile file, String data) {
		MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();  // 파이썬 AI 서버에 요청 객체로 전달해야 하므로
		bodyBuilder.part("data", data);
		bodyBuilder.part("file", file.getResource());

		String result = webClient.post()
								.uri("/ai_service")
								.contentType(MediaType.MULTIPART_FORM_DATA)
								.body(BodyInserters.fromMultipartData(bodyBuilder.build()))
								.retrieve()
								.bodyToMono(String.class)
								.block();

		return result;
	}
}

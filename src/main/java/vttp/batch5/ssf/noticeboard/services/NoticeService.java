package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepo;

	@Value("${server.name}")
	private String SERVER_URL;

	private static final String PUB_URL = "https://publishing-production-d35a.up.railway.app/notice";
	// TODO: Task 3
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	public String postToNoticeServer(String notice) {
		RequestEntity<String> req = RequestEntity.post(PUB_URL)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(notice);

		RestTemplate template = new RestTemplate();
		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);
			String payload = resp.getBody();
			noticeRepo.insertNotices(payload);
			return getPostId(payload);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return "I/O error on POST request for \"%s/notice\": Connection refused".formatted(SERVER_URL);
		}
	}

	private String getPostId(String payload) {
		JsonReader reader = Json.createReader(new StringReader(payload));
		return reader.readObject().getString("id");
	}

	public ResponseEntity<String> checkHealth() {
		try {
            noticeRepo.checkHealth();
            return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");

        } catch (Exception ex) {
            return ResponseEntity.status(503)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}");
        }
	}

	public String noticeToJsonString(Notice notice) {
		JsonArrayBuilder catArrBuilder = Json.createArrayBuilder();
        for(String category : notice.getCategories()) {
            catArrBuilder.add(category);
        }
        JsonObject noticeObj = Json.createObjectBuilder()
            .add("title", notice.getTitle())
            .add("poster", notice.getPoster())
            .add("postDate", notice.getPostDate().getTime())
            .add("categories", catArrBuilder.build())
            .add("text", notice.getText())
            .build();
		return noticeObj.toString();
	}
}

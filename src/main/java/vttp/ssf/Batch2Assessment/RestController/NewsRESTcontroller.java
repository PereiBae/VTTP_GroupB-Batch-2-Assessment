package vttp.ssf.Batch2Assessment.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vttp.ssf.Batch2Assessment.services.NewsService;

@RestController
public class NewsRESTcontroller {

    @Autowired
    private NewsService newsSvc;

    @GetMapping(value = "/news/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNewsById(@PathVariable int id) {
        JsonObject newsArticle = newsSvc.getArticlesById(id);

        if (newsArticle == null) {
            // Return Not Found with the custom message
            JsonObject notFoundResponse = Json.createObjectBuilder()
                    .add("error", "Cannot find News Article with ID: " + id)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse.toString());
        }

        // Return the news article as Json with ok status
        return ResponseEntity.status(200).body(newsArticle.toString());
    }

}

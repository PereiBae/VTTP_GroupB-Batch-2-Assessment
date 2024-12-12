package vttp.ssf.Batch2Assessment.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vttp.ssf.Batch2Assessment.models.News;
import vttp.ssf.Batch2Assessment.repositories.NewsRepository;
import vttp.ssf.Batch2Assessment.utils.DateConverter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepo;

    @Value("${my.api.key}")
    private static String apiKey;

    private static final String API_PATH = "https://data-api.cryptocompare.com/news/v1/article/list";

    public List<News> getArticles(String lang, Integer limit) {
        String url = UriComponentsBuilder.fromUriString(API_PATH)
                .queryParam("lang",lang)
                .queryParam("limit",limit)
                .build()
                .toUriString();
        System.out.println("URL: " + url);

        // Create the GET request
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .header("x-api-key", apiKey)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // Use RestTemplate to send the request
        RestTemplate restTemplate = new RestTemplate();

        // Create the list
        List<News> news = new ArrayList<>();

        try{
            // fetch the response
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String payload = response.getBody();
            System.out.println("payload: " + payload);

            // Parse JsonString to JsonObject to JsonArray(DATA)
            JsonReader jsonReader = Json.createReader(new StringReader(payload));
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("Data");

            System.out.println("Processing JsonArray");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject articles = jsonArray.getJsonObject(i);
                News article = new News();
                article.setId(articles.getJsonNumber("ID").intValue());
                article.setPublishedDate(DateConverter.longToDate(articles,"PUBLISHED_ON"));
                article.setTitle(articles.getString("TITLE"));
                article.setUrl(articles.getString("URL"));
                article.setImageURL(articles.getString("IMAGE_URL"));
                article.setBody(articles.getString("BODY"));
                List<String> temp = new ArrayList<>();
                // keywords are special
                String[] tags = articles.getString("KEYWORDS").split("\\|");
                Collections.addAll(temp, tags);
                article.setKeyWords(temp);
                JsonArray categories = articles.getJsonArray("CATEGORY_DATA");
                List<String> temp2 = new ArrayList<>();
                for( int j = 0; j < categories.size(); j++){
                    JsonObject category2 = categories.getJsonObject(j);
                    String categoryData = category2.getString("CATEGORY");
                    temp2.add(categoryData);
                }
                article.setCategory(temp2);
                news.add(article);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return news;
    }

    public void saveArticles(List<News> articles) {
        // Save the articles to Redis
        for (News article : articles) {
            newsRepo.saveNewsArticles(article);
        }
    }

    public JsonObject getArticlesById(int id) {
        JsonObject jsonObject = null;
        try {
            News article = newsRepo.getNewsArticlesById(id);
            jsonObject = Json.createObjectBuilder()
                    .add("id", article.getId())
                    .add("title", article.getTitle())
                    .add("body", article.getBody())
                    .add("published_on", DateConverter.dateTolong(article.getPublishedDate()))
                    .add("url", article.getUrl())
                    .add("imageurl", article.getImageURL())
                    .add("tags", String.valueOf(article.getKeyWords()))
                    .add("categories", String.valueOf(article.getCategory()))
                    .build();
        } catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}

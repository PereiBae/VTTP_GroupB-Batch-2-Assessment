package vttp.ssf.Batch2Assessment.models;

import java.util.Date;
import java.util.List;

public class News {

    private int id;
    private Date publishedDate;
    private String title;
    private String url;
    private String imageURL;
    private String body;
    private List<String> keyWords;
    private List<String> category;
    private boolean saveArticle;

    public News() {
    }

    public News(int id, Date publishedDate, String title, String url, String imageURL, String body, List<String> keyWords, List<String> category) {
        this.id = id;
        this.publishedDate = publishedDate;
        this.title = title;
        this.url = url;
        this.imageURL = imageURL;
        this.body = body;
        this.keyWords = keyWords;
        this.category = category;
    }

    public News(int id, Date publishedDate, String title, String url, String imageURL, String body, List<String> keyWords, List<String> category, boolean saveArticle) {
        this.id = id;
        this.publishedDate = publishedDate;
        this.title = title;
        this.url = url;
        this.imageURL = imageURL;
        this.body = body;
        this.keyWords = keyWords;
        this.category = category;
        this.saveArticle = saveArticle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public boolean isSaveArticle() {
        return saveArticle;
    }

    public void setSaveArticle(boolean saveArticle) {
        this.saveArticle = saveArticle;
    }
}

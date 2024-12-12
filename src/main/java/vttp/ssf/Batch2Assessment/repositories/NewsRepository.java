package vttp.ssf.Batch2Assessment.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vttp.ssf.Batch2Assessment.models.News;

@Repository
public class NewsRepository {

    @Autowired
    @Qualifier("redisObjectTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public void saveNewsArticles(News article){
        redisTemplate.opsForHash().put("Articles",String.valueOf(article.getId()),article);
    }

    public News getNewsArticlesById(int id){
        return (News) redisTemplate.opsForHash().get("Articles",String.valueOf(id));
    }
}

package vttp.ssf.Batch2Assessment.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vttp.ssf.Batch2Assessment.models.News;
import vttp.ssf.Batch2Assessment.services.NewsService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class NewsController {

    @Autowired
    private NewsService newsSvc;

    @GetMapping("/")
    public String getNews(Model model, HttpSession session) {

        // Can decide to show how many news articles in 1 View
        String lang = "EN";
        int limit = 10;

        List<News> newsArticles = newsSvc.getArticles(lang, limit);

        // Store the articles in the session
        session.setAttribute("currentArticles", newsArticles);

        model.addAttribute("news", newsArticles);
        return "news";
    }

    @PostMapping("/articles")
    public String saveNews(@RequestParam(value = "savedArticles", required = false) List<Integer> articleIds, HttpSession session, Model model) {
        // Retrieve the cached articles from the session
        @SuppressWarnings("unchecked")
        List<News> currentArticles = (List<News>) session.getAttribute("currentArticles");
        
        if (articleIds == null || articleIds.isEmpty()) {
            model.addAttribute("message", "No articles to save. Please refresh and try again.");
            return "redirect:/";
        }

        // Filter and collect the selected articles
        List<News> articlesToSave = currentArticles.stream()
                .filter(article -> articleIds.contains(article.getId()))
                .collect(Collectors.toList());

        // Save the selected articles to Redis using the service
        newsSvc.saveArticles(articlesToSave);

        // Add a success message
        model.addAttribute("message", "Selected articles have been saved successfully!");

        return "redirect:/";

    }

    // Day 18 - slide 8
    @GetMapping("/health")
    public ModelAndView getHealth() {
        ModelAndView mav = new ModelAndView();

        try {
            checkHealth();

            mav.setViewName("healthy");
            mav.setStatus(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            mav.setViewName("unhealthy");
            mav.setStatus(HttpStatusCode.valueOf(500));
        }
        return mav;
    }

    private void checkHealth() throws Exception {
        // get random number between 0 and 10
        Random random = new Random();
        // if random number is between 0 and 5
        // throw an exception
        if (random.nextInt(10) < 5) {
            throw new Exception("Random number is <5");
        }
        // means there is an exception/error (simulating exception)

        // else do nothing
    }

}

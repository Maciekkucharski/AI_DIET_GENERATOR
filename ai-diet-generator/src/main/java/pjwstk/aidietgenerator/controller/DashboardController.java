package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.service.DashboardService;
import pjwstk.aidietgenerator.view.FeedView;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(exposedHeaders = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public FeedView getFeed(HttpServletResponse response){
        return dashboardService.loadFeed(response);
    }
}

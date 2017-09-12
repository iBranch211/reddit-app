package org.baeldung.web.controller.general;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

    @RequestMapping(value = "/url/original")
    @ResponseBody
    public String getOriginalLink(@RequestParam("url") final String sourceUrl) {
        try {
            final List<String> visited = new ArrayList<String>();
            String currentUrl = sourceUrl;
            while (!visited.contains(currentUrl)) {
                System.out.println(currentUrl + " .....");
                visited.add(currentUrl);
                currentUrl = getOriginalUrl(currentUrl);
            }
            return currentUrl;
        } catch (final Exception ex) {
            logger.error("Error while expanding url " + sourceUrl, ex);
            return sourceUrl;
        }
    }

    //

    private String getOriginalUrl(final String oldUrl) throws IOException {
        final URL url = new URL(oldUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        final String originalUrl = connection.getHeaderField("Location");
        connection.disconnect();
        if (originalUrl == null) {
            return oldUrl;
        }
        if (originalUrl.indexOf("?") != -1) {
            return originalUrl.substring(0, originalUrl.indexOf("?"));
        }
        return originalUrl;
    }

}
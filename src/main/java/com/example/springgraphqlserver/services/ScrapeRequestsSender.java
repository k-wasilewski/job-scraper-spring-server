package com.example.springgraphqlserver.services;

import com.example.springgraphqlserver.types.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Service
public class ScrapeRequestsSender {

    @Autowired
    PageService pageService;

    private final String NODE_SERVER_ENDPOINT = "http://localhost:8080/graphql";

    public Date performScrapeRequest(String host, String path, String jobAnchorSelector, String jobLinkContains, int numberOfPages) {
        try {
            URL url = new URL(this.NODE_SERVER_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "*");
            con.setDoOutput(true);

            String jsonInputString = "{ \"query\": \"mutation { scrape(host: \\\"" + host + "\\\", path: \\\"" + path + "\\\", jobAnchorSelector: \\\"" + jobAnchorSelector + "\\\", jobLinkContains: \\\"" + jobLinkContains + "\\\", numberOfPages: " + numberOfPages + ") { complete } }\" }";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            System.out.println(con.getResponseCode());
            return new Date();
        } catch (Exception e) {
            System.out.println("performScrapeRequest error: " + e);
        }

        return null;
    }
}

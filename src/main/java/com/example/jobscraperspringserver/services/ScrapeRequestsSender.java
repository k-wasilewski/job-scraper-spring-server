package com.example.jobscraperspringserver.services;

import com.example.jobscraperspringserver.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScrapeRequestsSender {

    @Autowired
    PageService pageService;

    private final String NODE_SERVER_ENDPOINT = "http://job-scraper-node-server:8080/graphql";
    private final String SPRING_SCRAPE_EMAIL = "spring_scrape";

    public Date performScrapeRequest(String token, String host, String path, String jobAnchorSelector, String jobLinkContains, int numberOfPages, String userUuid) {
        try {
            URL url = new URL(this.NODE_SERVER_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "*");
            con.setRequestProperty("Origin", "job-scraper-spring-server:8081");
            con.setRequestProperty("Cookie", "authToken=" + token + ";");
            con.setDoOutput(true);

            String _host = host.replaceAll("\"", "&quot");
            String _path = path.replaceAll("\"", "&quot");
            String _jobAnchorSelector = jobAnchorSelector.replaceAll("\"", "&quot");
            String _jobLinkContains = jobLinkContains.replaceAll("\"", "&quot");
            String _uuid = userUuid;

            String jsonInputString = "{ \"query\": \"mutation { scrape(host: \\\"" + _host + "\\\", path: \\\"" + _path + "\\\", jobAnchorSelector: \\\"" + _jobAnchorSelector + "\\\", jobLinkContains: \\\"" + _jobLinkContains + "\\\", numberOfPages: " + numberOfPages + ", userUuid: \\\"" + _uuid + "\\\") { complete } }\" }";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (con.getResponseCode() == 200) return new Date();
        } catch (Exception e) {
            System.out.println("performScrapeRequest error: " + e);
        }

        return null;
    }

    public List<Object> login() {
        try {
            URL url = new URL(this.NODE_SERVER_ENDPOINT);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "*");
            con.setRequestProperty("Origin", "job-scraper-spring-server:8081");
            con.setDoOutput(true);

            String jsonInputString = "{ \"query\": \"mutation { login(email: \\\"" + SPRING_SCRAPE_EMAIL + "\\\", password: \\\"" + JwtTokenUtil.JWT_SECRET + "\\\") { success, error { message }, user { email } } }\" }";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (con.getResponseCode() == 200) return getToken(con);;
        } catch (Exception e) {
            System.out.println("login error: " + e);
        }

        return null;
    }

    private List<Object> getToken(HttpURLConnection con) {
        List<Object> tokenList = new ArrayList<>();

        Map<String, List<String>> headerFields = con.getHeaderFields();
        Set<String> headerFieldsSet = headerFields.keySet();
        Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

        while (hearerFieldsIter.hasNext()) {
            String headerFieldKey = hearerFieldsIter.next();
            if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {
                List<String> headerFieldValue = headerFields.get(headerFieldKey);
                for (String headerValue : headerFieldValue) {
                    String[] fields = headerValue.split(";\\s*");

                    String cookieValue = fields[0];
                    String expires = null;

                    for (int j = 1; j < fields.length; j++) {
                        if (fields[j].indexOf('=') > 0) {
                            String[] f = fields[j].split("=");
                            if ("expires".equalsIgnoreCase(f[0])) {
                                expires = f[1];
                            }
                        }
                    }

                    Pattern tokenPat = Pattern.compile("^authToken=(.*)$");
                    Matcher tokenMat = tokenPat.matcher(cookieValue);
                    String token = null;
                    if (tokenMat.find()) token = tokenMat.group(1);

                    Date expDate = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzz");;
                    try {
                        expDate = formatter.parse(expires);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    tokenList.add(token);
                    tokenList.add(expDate);
                    return token != null ? tokenList : null;
                }
            }
        }

        return null;
    }
}

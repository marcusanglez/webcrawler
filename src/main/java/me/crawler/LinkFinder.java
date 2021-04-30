package me.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class LinkFinder {

    public static final String A_HREF = "a[href]";
    public static final String HREF_ATTRIB_KEY = "href";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    /** Crawling over a url
     * Gets the <a href...></a> elements in the url
     * if they are internal
     * @param url
     * @param domain used to filter the href links tht are internal (start with the same domain)
     * @return
     */
    public static List<String> findLinks(String url, String domain)  {

        if (url == null) return null;
        try {
            Elements aHrefElements = getAHrefElements(url);
            return aHrefElements == null ?
                        Collections.emptyList()
                        :
                        aHrefElements.stream()
                        .map(aHref -> aHref.absUrl(HREF_ATTRIB_KEY))
                        .filter(absUrl -> absUrl.startsWith(domain))
                        .collect(Collectors.toList());
        } catch (IOException ioException) {
            //System.out.println("Error in request " + ioException);
            return Collections.emptyList();
        }
    }

    private static Elements getAHrefElements(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        Document document = connection.get();

        Connection.Response response = connection.response();
        if (response.statusCode() == 200){
            //System.out.println("\tVisiting from: " + url);
        }
        if ( ! response.contentType().contains("text/html")){
           throw new IOException("Non-html response!!!");
        }
        return document.select(A_HREF);
    }
}

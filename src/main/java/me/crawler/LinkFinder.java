package me.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class LinkFinder {

    private static final String A_HREF = "a[href]";
    private static final String IMG = "img";
    private static final String HREF_ATTRIB_KEY = "href";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    private final String domain;

    public LinkFinder(String domain){
        this.domain = domain;
    }

    /** Crawling over a url
     * Gets the <a href...></a> elements in the url
     * if they are internal
     * @param url where new links <a href=...></a> will be found
     * @return list of found internal links
     */
    public List<String> findInternalLinks(String url)  {

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

    public List<String> findImages(String url) {
        if (url == null) return null;
        try {
            Elements images = getImages(url);
            return images == null ?
                    Collections.emptyList()
                    :
                    images.stream()
                            .map(img -> img.absUrl("src"))
                            .filter(Objects::nonNull)
                            .filter(img -> !img.isEmpty())
                            .collect(Collectors.toList());
        } catch (IOException ioException) {
            //System.out.println("Error in request " + ioException);
            return Collections.emptyList();
        }
    }

    private static Elements getImages(String url) throws IOException {
        Document document = getDocument(url);
        return document.select(IMG);
    }

    private static Elements getAHrefElements(String url) throws IOException {
        Document document = getDocument(url);
        return document.select(A_HREF);
    }

    private static Document getDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        Document document = connection.get();

        Connection.Response response = connection.response();

        if ( ! response.contentType().contains("text/html")){
           throw new IOException("Non-html response!!!");
        }
        return document;
    }


}

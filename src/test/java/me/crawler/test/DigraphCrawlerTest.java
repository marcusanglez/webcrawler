package me.crawler.test;

import me.crawler.DiGraphCrawler;
import me.crawler.LinkFinder;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class DigraphCrawlerTest {

    @Mock
    LinkFinder linkFinder = Mockito.mock(LinkFinder.class);

    @Test
    public void testSimple(){
        String domain = "https://www.sedna.com/";
        String pageA = domain + "pageA/";
        String pageB = domain + "pageB/";
        String pageC = domain + "pageC/";
        List<String> indexLinks = List.of(pageA, pageB, pageC);
        when(linkFinder.findInternalLinks(domain)).thenReturn(indexLinks);
        String image1 = "https://imageService.com/image1";
        String image2 = "https://imageService.com/image2";
        List<String> images = List.of(image1, image2);
        when(linkFinder.findImages(domain)).thenReturn(images);


        String pageA1 = pageA + "pageA1/";
        String pageA2 = pageA + "pageA2/";
        String pageA3 = pageA + "pageA3/";
        List<String> pageALinks = List.of(pageA1, pageA2, pageA3);

        when(linkFinder.findInternalLinks(pageA)).thenReturn(pageALinks);
        when(linkFinder.findImages(pageA)).thenReturn(List.of(image1));
        when(linkFinder.findInternalLinks(pageA2)).thenReturn(Collections.emptyList());
        when(linkFinder.findImages(pageA2)).thenReturn(List.of());
        List<String> pageA3Links = List.of(pageA3+"pageA3i/", pageA3+"pageA3ii/");
        when(linkFinder.findInternalLinks(pageA3)).thenReturn(pageA3Links);
        when(linkFinder.findImages(pageA3)).thenReturn(List.of());

        String pageB1 = pageB + "pageB1/";
        when(linkFinder.findInternalLinks(pageB)).thenReturn(List.of(pageB1));
        when(linkFinder.findInternalLinks(pageB1)).thenReturn(Collections.emptyList());
        when(linkFinder.findInternalLinks(pageC)).thenReturn(Collections.emptyList());

        DiGraphCrawler crawler = new DiGraphCrawler(domain, linkFinder);
        try {
            crawler.crawl(crawler.getRoot());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        crawler.traverse(crawler.getRoot(), "");

    }
}

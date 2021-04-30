package me.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * /**
 *  * Write a simple web crawler in a programming language of your choice. The crawler should be limited to one domain
 *  * - so when crawling SEDNA.com it would :
 *  *
 *  *      crawl all pages within the domain, but not follow external links,
 *  *      for example to the LinkedIn and Twitter accounts. Given a URL, it should output a site map,
 *  * showing which static assets each page depends on, and the links between pages.
 *  */

public class Crawler {
    private static final int MILLION_LINKS = 1000000;
    private Set<String> linksVisited = new HashSet<>();
    // using a linked list enables to keep order of the next links to visit
    private List<String> linksToVisit = new LinkedList<>();

    public static void main(String[] args) {

        if (args == null || args.length < 0){
            System.out.println("ERROR: A domain is needed to crawl, ending");
            return;
        }
        String domain = args[0];
        Crawler crawler = new Crawler();
        try {
            crawler.crawl(domain);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String getNextLinkToVisitInTheList()
    {
        String nextLinkToVisit;
        do
        {
            // we get the next link to visit
            if (this.linksToVisit.size() == 0) return null;
            // remove it from the linksToVisit
            nextLinkToVisit = this.linksToVisit.remove(0);
            // if for some reason nextUrl is already visited we continue looping pagesToVisit
        } while(this.linksVisited.contains(nextLinkToVisit));
        // and add to the linksVisited
        this.linksVisited.add(nextLinkToVisit);
        return nextLinkToVisit;
    }

    public void crawl(String domain) throws IOException {
        do{
            String urlToVisit;
            // for the 1st time urlToVisit is the main starting point and add it to linksToVisit
            if (this.linksToVisit.isEmpty()){
                urlToVisit = domain;
                this.linksVisited.add(urlToVisit);
            }else{
                // from the second to Nth time we get linksToVisit linkedList
                urlToVisit = this.getNextLinkToVisitInTheList();
            }
            if (urlToVisit == null) break;

            // finds next links to pages to visit
            this.linksToVisit.addAll(LinkFinder.findLinks(urlToVisit, domain));

        }while (linksToVisit.size() > 0 && this.linksVisited.size() < MILLION_LINKS);

        System.out.format("Visited %s links and resources%n%n", linksVisited.size());
        linksVisited.stream()
               .forEach(System.out::println);
    }


}

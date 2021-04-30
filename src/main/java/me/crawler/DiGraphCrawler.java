package me.crawler;

import java.io.IOException;
import java.util.*;

/**
 * /**
 * * Write a simple web crawler in a programming language of your choice. The crawler should be limited to one domain
 * * - so when crawling SEDNA.com it would :
 * *
 * *      crawl all pages within the domain, but not follow external links,
 * *      for example to the LinkedIn and Twitter accounts. Given a URL, it should output a site map,
 * * showing which static assets each page depends on, and the links between pages.
 */

public class DiGraphCrawler {
    private static final int MILLION_LINKS = 1000000;
    private final String domain;

    private final Set<String> linksVisited = new HashSet<>();
    // using a linked list enables to keep order of the next links to visit
    private final Node root;

    private final LinkFinder linkFinder;

    public DiGraphCrawler(final String domain, LinkFinder linkFinder) {
        this.domain = domain;
        this.linkFinder = linkFinder;
        root = new Node(domain);
    }

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("ERROR: A domain is needed to crawl, ending");
            return;
        }
        String domain = args[0];
        DiGraphCrawler crawler = new DiGraphCrawler(domain, new LinkFinder(domain));
        try {
            System.out.format("Start crawling%n");
            crawler.crawl(crawler.root);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.printf("Start traversing%n");
        crawler.traverse(crawler.root, "");
    }

    public void traverse(Node node, String indentation) {

        System.out.format("%s%s%n", indentation, node.key);
        List<String> images = node.images;
        if (images != null && ! images.isEmpty()) {
            System.out.format("%simages: %s%n", indentation, images);
        }

        indentation += "\t";

        Set<Edge> neighbors = node.getEdges();

        if (neighbors != null) {
            for (Edge neighbor :
                    neighbors) {
                traverse(neighbor.to, indentation);
            }
        }
    }

    public void crawl(Node node) throws IOException {

        System.out.format("crawling: %s%n", node.key);
        linksVisited.add(node.key);

        // finds next links to pages to visit
        List<String> internalLinks = linkFinder.findInternalLinks(node.key);

        List<String> images = linkFinder.findImages(node.key);
        if (images != null && ! images.isEmpty()){
            node.getImages().addAll(images);
        }

        if (internalLinks != null) {
            for (String link :
                    internalLinks) {
                if (!linksVisited.contains(link)) {

                    Node neighbor = new Node(link);
                    node.addEdge(new Edge(node, neighbor));
                    //// !!!!! RECURSIVE (DFS)
                    crawl(neighbor);
                }
            }
        }

    }

    public Node getRoot() {
        return root;
    }

    private static class Node {

        private Set<Edge> edges;
        private String key;
        private List<String> images;

        public Node(String key) {
            this.key = key;
            edges = new LinkedHashSet<>();
            images = new ArrayList<>();
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public Set<Edge> getEdges() {
            return edges;
        }

        public List<String> getImages() {
            return images;
        }
    }

    private static class Edge {

        protected Node from;
        protected Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }
    }
}

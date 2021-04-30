# Web crawler

Simple web crawler. The crawler is limited to one domain - so when crawling https://somedomain.com it crawls all pages within the domain, but not follow external links, for example to the LinkedIn and Twitter accounts. Given a URL, it should output a site map, showing which static assets each page depends on, and the links between pages.

The crawler is called as follows:

DigraphCrawler.main("https://exampleDomain.net")

it will output the internal links and 
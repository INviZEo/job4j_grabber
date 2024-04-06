package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGENUMBER = 5;

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= PAGENUMBER; i++) {
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, PAGENUMBER, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            HabrCareerDateTimeParser hb = new HabrCareerDateTimeParser();
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element titleDate = row.select(".vacancy-card__date").first();
                Element linkElement = titleElement.child(0);
                Element linkDate = titleDate.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                System.out.printf("%s %s %s%n ", vacancyName, hb.parse(linkDate.attr("datetime")), link);
            });
        }
    }
}

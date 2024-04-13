package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGENUMBER = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        return document.select(".faded-content__container").text();
    }

    private Post getPostByElement(Element row) throws IOException {
        Post post = new Post();
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        Element dateElement = row.select(".basic-date").first();
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        post.setTitle(titleElement.text());
        post.setLink(link);
        post.setDescription(retrieveDescription(link));
        post.setCreated(dateTimeParser.parse(dateElement.attr("datetime")));
        return post;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> postList = new ArrayList<>();
        for (int pageNumber = 1; pageNumber < 6; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(link, PREFIX, pageNumber, SUFFIX);
            try {
                Document document = Jsoup.connect(fullLink).get();
                Elements rows = document.select(".vacancy-card__inner");
                for (Element row : rows) {
                    postList.add(getPostByElement(row));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return postList;
    }
}

package com.tbotwebhooks.service.parser;

import com.tbotwebhooks.model.HtmlFile;
import com.tbotwebhooks.service.HtmlService;
import com.tbotwebhooks.util.WeekState;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Log4j2
public class TimeTableParser {

    private final String timetableUrl;

    private final HtmlService htmlService;

    private final static String prefix = "https://www.miit.ru";

    private final String HTML_PATH;

    public TimeTableParser(@Value("${parser.TimetableUrl}") String TimetableUrl,
                           HtmlService htmlService,
                           @Value("${html.path}") String path)
    {
        this.timetableUrl = TimetableUrl;
        this.htmlService = htmlService;
        this.HTML_PATH = path;
    }

    private Document parsePage(String url) {
        try {
            return getOrUploadHtml(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] location = url.split("/");
        String fileName = location[location.length - 1];
        Optional<HtmlFile> htmlFile = htmlService.getHtmlFile(fileName);

        if (htmlFile.isPresent()) {
            if (htmlFile.get().getContent() != null) {
                return Jsoup.parse(htmlFile.get().getContent());
            }
        }

        return null;
    }

    private String getUrlPageTimetableByGroup(String group) {
        Document document = parsePage(timetableUrl);
        Element ref = document.select("a:contains(" + group + ")").first();
        return prefix + ref.attr("href");
    }

    public Element getTimeTableByGroup(String group, WeekState weekState) {
        Document page = parsePage(getUrlPageTimetableByGroup(group));
        Element table = null;
        if (weekState.equals(WeekState.WEEK_ONE)) {
            table = page.getElementById("week-1");
        }
        else {
            table = page.getElementById("week-2");
        }

        return table;
    }

    public boolean findGroup(String group) {
        if (group.length() < 6 | group.length() > 7) {
            return false;
        }
        Document document = parsePage(timetableUrl);
        return document.text().contains(group);
    }

    private Document getOrUploadHtml(String url) throws IOException {
        String[] location = url.split("/");
        String fileName = location[location.length - 1];

        Optional<HtmlFile> htmlFile = htmlService.getHtmlFile(fileName);
        if (htmlFile.isPresent()) {
            if (htmlFile.get().getContent() != null) {
                if (Math.abs(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfMonth() - htmlFile.get().getLastUpdateDate()) < 1) {
                    return Jsoup.parse(htmlFile.get().getContent());
                }
            }
        }
        Document document = Jsoup.connect(url).timeout(30 * 1000).get();

        log.info("Html parsed from url: {}", document.location());

        location = document.location().split("/");
        fileName = location[location.length - 1];

        htmlService.saveHtmlFile(fileName, document);

        return document;
    }
}

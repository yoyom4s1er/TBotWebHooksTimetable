package com.tbotwebhooks.service;

import com.tbotwebhooks.model.HtmlFile;
import com.tbotwebhooks.repository.HtmlRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Log4j2
public class HtmlServiceImpl implements HtmlService{

    private final HtmlRepository htmlRepository;

    public HtmlServiceImpl(HtmlRepository htmlRepository) {
        this.htmlRepository = htmlRepository;
    }

    @Override
    public Optional<HtmlFile> getHtmlFile(String fileName) {
        return htmlRepository.findByFileName(fileName);
    }

    @Override
    @Deprecated
    public void saveHtmlFile(HtmlFile file) {
        htmlRepository.save(file);
    }

    @Override
    public void saveHtmlFile(String fileName, Document html) throws IOException {
        HtmlFile htmlFile = null;
        Optional<HtmlFile> htmlFileFromDB = htmlRepository.findByFileName(fileName);

        if (htmlFileFromDB.isEmpty()) {
            htmlFile = new HtmlFile();
            htmlFile.setFileName(fileName);
            htmlFile.setLastUpdateDate(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfMonth());
            htmlFile.setContent(html.outerHtml());

            htmlRepository.save(htmlFile);
        } else {
            htmlFile = htmlFileFromDB.get();
            htmlFile.setLastUpdateDate(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfMonth());
            htmlFile.setContent(html.outerHtml());

            htmlRepository.save(htmlFile);
        }

        log.info("Created html file with name: {}", htmlFile.getFileName());

    }
}

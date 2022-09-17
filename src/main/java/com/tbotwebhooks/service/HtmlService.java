package com.tbotwebhooks.service;

import com.tbotwebhooks.model.HtmlFile;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface HtmlService {

    public Optional<HtmlFile> getHtmlFile(String fileName);

    public void saveHtmlFile(HtmlFile file);

    public void saveHtmlFile(String fileName, Document html) throws IOException;
}

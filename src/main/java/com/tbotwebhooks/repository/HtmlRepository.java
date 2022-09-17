package com.tbotwebhooks.repository;

import com.tbotwebhooks.model.HtmlFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HtmlRepository extends JpaRepository<HtmlFile, Long> {
    Optional<HtmlFile> findByFileName(String fileName);
}

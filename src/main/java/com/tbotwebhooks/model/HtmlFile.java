package com.tbotwebhooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "htmls")
@Entity
public class HtmlFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;

    private String path;

    @Column(length = 307200)
    private String content;

    private int lastUpdateDate;
}

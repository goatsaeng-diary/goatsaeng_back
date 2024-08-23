package com.example.gotsaeng_back.global.elastic.index;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "study")
@Getter
@Setter
public class Study {
    @Id
    private String id;
    private String title;
    private String content;
}

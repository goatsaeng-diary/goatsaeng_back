package com.example.gotsaeng_back.global.elastic.repository;

import com.example.gotsaeng_back.global.elastic.index.Study;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StudyElsRepository extends ElasticsearchRepository<Study,Long> {

}

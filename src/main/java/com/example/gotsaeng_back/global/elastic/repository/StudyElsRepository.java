package com.example.gotsaeng_back.global.elastic.repository;

import com.example.gotsaeng_back.global.elastic.index.Study;
import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyElsRepository extends ElasticsearchRepository<Study,String> {
    @Query("{\"match\": {\"content\": \"?0\"}}")
    List<Study> findByContent(String content);
}

package com.example.myapp.repository;

import com.example.myapp.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query(value = "SELECT ROWNUM, LOG_DATE, INPUT_TEXT, OUTPUT_TEXT FROM LOG ORDER BY LOG_DATE DESC", nativeQuery = true)
    List<Object[]> findAllWithRowNum();

    Optional<Log> findTopByOrderByLogDateDesc();

    // 날짜 기준으로 내림차순 정렬된 로그를 가져오는 쿼리
    @Query(value = "SELECT * FROM LOG ORDER BY LOG_DATE DESC", nativeQuery = true)
    List<Log> findAllLogsOrderedByDateDesc();
}

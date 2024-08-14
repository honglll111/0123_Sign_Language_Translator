package com.example.myapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.myapp.model.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
	
	 @Query(value = "SELECT ROW_NUMBER() OVER (ORDER BY id) AS rownum, l.* FROM Log l", nativeQuery = true)
	 List<Object[]> findAllWithRowNum();
}

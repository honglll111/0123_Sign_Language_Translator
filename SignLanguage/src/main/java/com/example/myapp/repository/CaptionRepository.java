package com.example.myapp.repository;

import com.example.myapp.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, Long> {
}

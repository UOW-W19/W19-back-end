package com.example.demo.repository;

import com.example.demo.entity.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
}

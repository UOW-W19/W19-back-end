package com.example.demo.repository;

import com.example.demo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, java.util.UUID> {
        Optional<Profile> findByUsername(String username);

        Optional<Profile> findByEmail(String email);

        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        @org.springframework.data.jpa.repository.Query("SELECT DISTINCT p FROM Profile p JOIN p.languages l " +
                        "WHERE (:nativeLanguage IS NULL OR (l.language.code = :nativeLanguage AND l.isLearning = false)) "
                        +
                        "AND (:learningLanguage IS NULL OR (l.language.code = :learningLanguage AND l.isLearning = true))")
        java.util.List<Profile> searchProfiles(
                        @org.springframework.data.repository.query.Param("nativeLanguage") String nativeLanguage,
                        @org.springframework.data.repository.query.Param("learningLanguage") String learningLanguage);
}

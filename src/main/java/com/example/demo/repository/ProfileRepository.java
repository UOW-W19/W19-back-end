package com.example.demo.repository;

import com.example.demo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT p FROM Profile p JOIN p.learningLanguages ll " +
            "WHERE (:nativeLanguage IS NULL OR p.nativeLanguage = :nativeLanguage) " +
            "AND (:learningLanguage IS NULL OR ll.languageCode = :learningLanguage)")
    java.util.List<Profile> searchProfiles(
            @org.springframework.data.repository.query.Param("nativeLanguage") String nativeLanguage,
            @org.springframework.data.repository.query.Param("learningLanguage") String learningLanguage);
}

package com.example.demo.entity;

import com.example.demo.common.BaseEntity;
import com.example.demo.enums.AppRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private Double latitude;
    private Double longitude;

    private String nativeLanguage;

    @ElementCollection
    @CollectionTable(name = "profile_learning_languages", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "language_code")
    private List<String> learningLanguages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> roles = new ArrayList<>();

    public void addRole(AppRole role) {
        UserRole userRole = new UserRole();
        userRole.setUser(this);
        userRole.setRole(role);
        this.roles.add(userRole);
    }
}

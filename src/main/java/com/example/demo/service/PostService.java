package com.example.demo.service;

import com.example.demo.dto.CreatePostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.entity.Post;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Language;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final PostReactionRepository postReactionRepository;
    private final PostCommentRepository postCommentRepository;
    private final LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> getFeed(String language, Pageable pageable, Double lat, Double lon,
            String currentUserEmail) {
        Profile currentUser = profileRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Post> posts;
        if (language != null && !language.equalsIgnoreCase("all")) {
            posts = postRepository.findByOriginalLanguage(language, pageable);
        } else {
            posts = postRepository.findAll(pageable);
        }

        return posts.map(post -> mapToResponse(post, currentUser, lat, lon));
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request, String currentUserEmail) {
        Profile currentUser = profileRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .author(currentUser)
                .content(request.getContent())
                .originalLanguage(request.getOriginalLanguage())
                .imageUrl(request.getImageUrl())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        return mapToResponse(postRepository.save(post), currentUser, request.getLatitude(), request.getLongitude());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(UUID postId, String currentUserEmail) {
        Profile currentUser = profileRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return mapToResponse(post, currentUser, null, null);
    }

    @Transactional
    public void deletePost(UUID postId, String currentUserEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }

        postRepository.delete(post);
    }

    private PostResponse mapToResponse(Post post, Profile currentUser, Double lat, Double lon) {
        String distance = "Unknown";
        if (lat != null && lon != null && post.getLatitude() != null && post.getLongitude() != null) {
            double d = calculateDistance(lat, lon, post.getLatitude(), post.getLongitude());
            distance = String.format("%.1f km", d);
        }

        String userReaction = postReactionRepository.findByPostIdAndProfileId(post.getId(), currentUser.getId())
                .map(reaction -> reaction.getType().name())
                .orElse(null);

        Language langInfo = post.getOriginalLanguage() != null
                ? languageRepository.findById(post.getOriginalLanguage()).orElse(null)
                : null;

        return PostResponse.builder()
                .id(post.getId())
                .author(PostResponse.AuthorDto.builder()
                        .id(post.getAuthor().getId())
                        .username(post.getAuthor().getUsername())
                        .displayName(post.getAuthor().getDisplayName())
                        .avatarUrl(post.getAuthor().getAvatarUrl())
                        .language(langInfo != null ? langInfo.getName() : "Unknown")
                        .flagEmoji(langInfo != null ? langInfo.getFlagEmoji() : "🌍")
                        .build())
                .content(post.getContent())
                .originalLanguage(post.getOriginalLanguage())
                .imageUrl(post.getImageUrl())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .location(post.getLatitude() != null ? "Nearby" : "Unknown")
                .distance(distance)
                .reactions(PostResponse.PostReactionsSummary.builder()
                        .likes(postReactionRepository.countByPostId(post.getId()))
                        .comments(postCommentRepository.countByPostId(post.getId()))
                        .build())
                .userReaction(userReaction)
                .createdAt(post.getCreatedAt() != null ? post.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
                        : java.time.Instant.now())
                .build();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 학습/게임 월드의 기본 메타 정보를 보관한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "world")
public class World {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "world", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Story> stories = new ArrayList<>();

    private World(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static World of(String name, String description) {
        return new World(name, description);
    }

    public void rename(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void addStory(Story story) {
        if (story == null) {
            return;
        }
        stories.add(story);
    }

    public List<Story> getStories() {
        return Collections.unmodifiableList(stories);
    }
}

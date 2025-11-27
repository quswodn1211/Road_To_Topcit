package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 월드 안의 이야기 단위를 표현한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "story",
        indexes = @Index(name = "idx_story_world", columnList = "world_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_story_world_num",
                columnNames = {"world_id", "story_num"}))
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private World world;

    @Column(name = "story_num", nullable = false)
    private int storyNum;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Stage> stages = new ArrayList<>();

    private Story(World world, int storyNum) {
        this.world = Objects.requireNonNull(world, "world must not be null");
        this.storyNum = storyNum;
    }

    public static Story of(World world, int storyNum) {
        return new Story(world, storyNum);
    }

    public void addStage(Stage stage) {
        if (stage == null) {
            return;
        }
        stages.add(stage);
        stage.assignStory(this);
        stage.relocate(world);
    }

    public List<Stage> getStages() {
        return Collections.unmodifiableList(stages);
    }
}

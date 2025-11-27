package com.opsw.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 월드 안에서 진행되는 하나의 스테이지 정보.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stage",
        indexes = @Index(name = "idx_stage_world", columnList = "world_id"),
        uniqueConstraints = @UniqueConstraint(name = "uk_stage_world_number",
                columnNames = {"world_id", "stage_number"}))
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private World world;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Story story;

    @Column(name = "stage_number", nullable = false)
    private int stageNumber;

    @Column(name = "problem_count", nullable = false)
    private int problemCount;

    private Stage(World world, int stageNumber, int problemCount) {
        this.world = Objects.requireNonNull(world, "world must not be null");
        this.stageNumber = stageNumber;
        this.problemCount = problemCount;
    }

    public static Stage of(World world, int stageNumber, int problemCount) {
        return new Stage(world, stageNumber, problemCount);
    }

    public void changeProblemCount(int problemCount) {
        this.problemCount = problemCount;
    }

    public void relocate(World world) {
        this.world = Objects.requireNonNull(world, "world must not be null");
    }

    public void assignStory(Story story) {
        this.story = story;
    }
}

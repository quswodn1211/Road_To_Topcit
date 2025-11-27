package com.opsw.backend.domain.user;

import com.opsw.backend.domain.Stage;
import com.opsw.backend.domain.World;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자의 월드/스토리 진행 현황.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_story_progress",
        uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_world",
                columnNames = {"user_id", "world_id"}))
public class UserStoryProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private World world;

    @Column(name = "world_life", nullable = false)
    private int worldLife;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Stage stage;

    private UserStoryProgress(User user, World world, int worldLife) {
        this.user = user;
        this.world = world;
        this.worldLife = worldLife;
    }

    public static UserStoryProgress start(User user, World world, int worldLife) {
        return new UserStoryProgress(user, world, worldLife);
    }

    public void advance(Stage stage) {
        this.stage = stage;
    }

    public void loseLife() {
        if (worldLife > 0) {
            worldLife--;
        }
    }

    public void refillLife(int life) {
        this.worldLife = life;
    }
}

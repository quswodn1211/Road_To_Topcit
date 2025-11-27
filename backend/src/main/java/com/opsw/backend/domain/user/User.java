package com.opsw.backend.domain.user;

import com.opsw.backend.config.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 서비스 가입 사용자 엔티티.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "app_user",
        indexes = {
                @Index(name = "idx_user_user_id", columnList = "user_id", unique = true),
                @Index(name = "idx_user_email", columnList = "email", unique = true)
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String userId;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "display_name", length = 50, nullable = false)
    private String displayName;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    private User(String userId, String password, Gender gender, String displayName, String email) {
        this.userId = userId;
        this.password = password;
        this.gender = gender;
        this.displayName = displayName;
        this.email = email;
    }

    public static User create(String userId, String password, Gender gender,
                              String displayName, String email) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(email, "email must not be null");
        return new User(userId, password, gender, displayName, email);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfile(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

}

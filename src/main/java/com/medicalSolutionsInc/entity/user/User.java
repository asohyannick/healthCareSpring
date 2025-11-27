package com.medicalSolutionsInc.entity.user;
import com.medicalSolutionsInc.enumerations.user.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "is_account_blocked", nullable = false)
    private boolean accountBlocked = false;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "failed_login_attempts", nullable =true)
    private Integer failedLoginAttempts = 0;

    @Column(name = "two_factor_attempts", nullable = true)
    private Integer twoFactorAttempts = 0;

    @Column(name = "is_two_factor_verified", nullable = true)
    private Boolean isTwoFactorVerified;

    @Column(name = "two_factor_expiry", nullable = true)
    private String twoFactorExpiry;

    @Column(name = "magic_link_token", nullable = true)
    private String magicLinkToken;

    @Column(name = "magic_link_token_expiration", nullable = true)
    private String MagicLinkTokenExpiration;

    @Column(name = "two_factor_code", nullable = true)
    private String twoFactorCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.role == null) {
            this.role = UserRole.DEVOPS_ENGINEER;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

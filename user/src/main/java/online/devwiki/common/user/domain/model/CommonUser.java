package online.devwiki.common.user.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "common_user", schema = "canyon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    private String gender;
    private LocalDate dateOfBirth;
    private String status;
    private Boolean accountVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "common_user_common_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<CommonRole> roleSet;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private CommonUserInfo userInfo;

    public Set<CommonRole> getRoleSet() {
        return roleSet;
    }

    public void setRoles(Set<CommonRole> roleSet) {
        this.roleSet = roleSet;
    }
}
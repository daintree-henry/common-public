package online.devwiki.common.user.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "common_role", schema = "canyon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(length = 60)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "common_role_common_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<CommonPermission> permissionSet;
}
package online.devwiki.common.user.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "common_permission", schema = "canyon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(length = 60)
    private String permissionName;
}
package ssp.marketplace.app.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ssp.marketplace.app.entity.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@RequiredArgsConstructor
public class Role extends BasicEntity {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> users;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        Role role = (Role)obj;
        return super.getId().equals(role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }


}

package org.job.hostfully.owner.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.job.hostfully.common.model.entity.Person;
import org.job.hostfully.property.model.entity.Property;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "owner")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE owner SET deleted = true WHERE id=?")
@SQLRestriction("deleted <> true")
public class Owner extends Person {

    @OneToMany(mappedBy = "owner")
    private List<Property> properties;

    public Owner(String name, String email) {
        super(name, email);
    }
}

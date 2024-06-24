package org.job.hostfully.guest.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.job.hostfully.common.model.entity.Person;

@Getter
@Entity
@Table(name = "guest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE guest SET deleted = true WHERE id=?")
@SQLRestriction("deleted <> true")
public class Guest extends Person {

    public Guest(String name, String email) {
        super(name, email);
    }
}

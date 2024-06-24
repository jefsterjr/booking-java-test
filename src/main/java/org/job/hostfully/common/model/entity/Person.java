package org.job.hostfully.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Data
public abstract class Person extends AbstractEntity {

    @Column(name = "guest_name")
    private String name;

    @Column(name = "guest_email")
    private String email;

}

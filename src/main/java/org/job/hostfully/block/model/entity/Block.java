package org.job.hostfully.block.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.job.hostfully.common.model.entity.AbstractEntity;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.property.model.entity.Property;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "block")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE block SET deleted = true WHERE id=?")
@SQLRestriction("deleted <> true")
public class Block extends AbstractEntity {

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BlockType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;


    public void updateDate(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void cancel() {
        this.status = Status.CANCELED;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }
}

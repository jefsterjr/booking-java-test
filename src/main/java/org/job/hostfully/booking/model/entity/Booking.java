package org.job.hostfully.booking.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.common.model.entity.AbstractEntity;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.guest.model.entity.Guest;

@Getter
@Entity
@Table(name = "booking")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE booking SET deleted = true WHERE id=?")
@SQLRestriction("deleted <> true")
public class Booking extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Setter
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Setter
    @ManyToOne
    @JoinColumn(name = "block_id")
    private Block block;

    public void cancel() {
        this.status = Status.CANCELED;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }
}

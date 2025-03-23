package com.vietjoke.vn.entity.booking;

import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.util.enums.booking.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "booking_status")
public class BookingStatusEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code", length = 20, nullable = false, unique = true)
    private BookingStatus statusCode;

    @Column(name = "status_name", length = 50, nullable = false)
    private String statusName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "bookingStatusEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BookingEntity> bookingEntities;
}

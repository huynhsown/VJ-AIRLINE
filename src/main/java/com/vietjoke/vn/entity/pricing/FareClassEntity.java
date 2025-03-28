package com.vietjoke.vn.entity.pricing;

import com.vietjoke.vn.entity.booking.BookingDetailEntity;
import com.vietjoke.vn.entity.fleet.AirlineEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fare_classes")
public class FareClassEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private String description;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    private Integer baggageAllowance;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean seatSelection;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean mealIncluded;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean changeAllowed;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean refundAllowed;

    private Float changeFee;

    private Float refundFee;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private AirlineEntity airlineEntity;

    @OneToMany(mappedBy = "fareClassEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetailEntity> bookingDetailEntities;

    @OneToMany(mappedBy = "fareClassEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatReservationEntity> seatReservationEntities;
}

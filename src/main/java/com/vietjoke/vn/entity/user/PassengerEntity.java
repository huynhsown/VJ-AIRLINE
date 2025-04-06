package com.vietjoke.vn.entity.user;

import com.vietjoke.vn.entity.booking.BookingDetailEntity;
import com.vietjoke.vn.entity.core.BaseEntity;
import com.vietjoke.vn.entity.location.CountryEntity;
import com.vietjoke.vn.util.enums.user.Gender;
import com.vietjoke.vn.util.enums.user.IdType;
import com.vietjoke.vn.util.enums.user.PassengerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "passengers")
public class PassengerEntity extends BaseEntity {

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(
            name = "country_code",
            referencedColumnName = "country_code"
    )
    private CountryEntity nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", length = 20)
    private IdType idType;

    @Column(name = "id_number", length = 50)
    private String idNumber;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "passenger_type", length = 20, nullable = false)
    private PassengerType passengerType;

    @OneToMany(mappedBy = "passengerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetailEntity> bookingDetailEntities;

}

package az.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_type")
    private String currencyType;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "updatedTimestamp", nullable = false)
    private LocalDate updatedTimestamp;

}
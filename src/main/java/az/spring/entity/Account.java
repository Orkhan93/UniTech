package az.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
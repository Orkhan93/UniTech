package az.spring.repository;

import az.spring.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    List<Currency> findByUpdatedDateBefore(LocalDateTime localDateTime);

}
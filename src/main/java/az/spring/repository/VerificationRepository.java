package az.spring.repository;

import az.spring.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationToken, Long> {



}
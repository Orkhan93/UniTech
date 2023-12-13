package az.spring.repository;

import az.spring.entity.Account;
import az.spring.wrapper.AccountWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select new az.spring.wrapper.AccountWrapper(a.id,a.accountNumber,a.status,a.balance)" +
            " from Account a where a.user.id=:userId and a.status=true")
    List<AccountWrapper> allActiveAccountByUserId(Long userId);

    Optional<Account> findByAccountNumber(String accountNumber);

}
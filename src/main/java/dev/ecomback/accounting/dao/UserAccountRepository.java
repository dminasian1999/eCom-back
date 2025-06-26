package dev.ecomback.accounting.dao;

import dev.ecomback.accounting.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, String> {

}

package dev.ecomback.accounting.dao;

import dev.ecomback.accounting.model.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepository extends CrudRepository<UserToken, String> {

}

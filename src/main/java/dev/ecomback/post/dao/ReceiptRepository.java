package dev.ecomback.post.dao;

import dev.ecomback.post.model.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptRepository extends CrudRepository<Receipt, String> {
}

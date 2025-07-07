package dev.ecomback.post.dao;

import dev.ecomback.post.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {
	Stream<Post> findByCategory(String category);
	Stream<Post> findByCategoryOrderBySellAsc(String category);
	Stream<Post> findByCategoryOrderBySellDesc(String category);
	Stream<Post> findByCategoryOrderByNameAsc(String category);
	Stream<Post> findByCategoryOrderByNameDesc(String category);
	Stream<Post> findByCategoryOrderByDateCreatedAsc(String category);
	Stream<Post> findByCategoryOrderByDateCreatedDesc(String category);
	Stream<Post> findByCategoryAndSellBetween(Double minPrice,	Double maxPrice);
	Stream<Post> findByCategoryContainsIgnoreCase(String query);

	Stream<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);
	Stream<Post> findByType(String type);
	Stream<Post> findByTypeOrderBySellAsc(String type);
	Stream<Post> findByTypeOrderBySellDesc(String type);
	Stream<Post> findByTypeOrderByNameAsc(String type);
	Stream<Post> findByTypeOrderByNameDesc(String type);
	Stream<Post> findByTypeOrderByDateCreatedAsc(String type);
	Stream<Post> findByTypeOrderByDateCreatedDesc(String type);
	Stream<Post> findByTypeAndSellBetween(Double minPrice,	Double maxPrice);
	Stream<Post> findByTypeContainsIgnoreCase(String query);



	Stream<Post> findByOrderByDateCreatedDesc();


	Stream<Post>findByNameIgnoreCase(String title);

	Stream<Post> findByIdIn(String [] ids);        // ← “In”, not “Containing”





}

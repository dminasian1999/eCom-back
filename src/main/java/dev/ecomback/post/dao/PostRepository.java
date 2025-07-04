package dev.ecomback.post.dao;

import dev.ecomback.post.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {

	Stream<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);
	Stream<Post> findByCategory(String category);


	Stream<Post> findByOrderByDateCreatedDesc();


	Stream<Post>findByNameIgnoreCase(String title);

	Stream<Post> findByIdIn(String [] ids);        // ← “In”, not “Containing”





}

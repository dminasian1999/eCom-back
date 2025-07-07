package dev.ecomback.post.service;

import dev.ecomback.post.dto.NewPostDto;
import dev.ecomback.post.dto.PostDto;
import dev.ecomback.post.dto.QueryDto;
import dev.ecomback.post.dto.ReceiptDto;
import dev.ecomback.post.model.Adjustment;
import dev.ecomback.post.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

public interface PostService {

    PostDto addNewPost(String author, NewPostDto newPostDto);


    List<PostDto> getAllPosts();

    List<ReceiptDto> getAllReceipts();

    PostDto removePost(String id);

    PostDto updatePost(String id, NewPostDto newPostDto);

    void deleteFileFromS3(String fileName);

    String saveFiles(MultipartFile file);

    Adjustment adjust(String id, String author, int num, boolean add);

    PostDto findPostById(String id);

    List<PostDto> findPostsByIds(String[] ids);

    List<PostDto> findPostsWithCriteriaAndSort(QueryDto query);

    List<PostDto> findPostByCategory(String category);



    List<PostDto> findByCategoryAndSellBetween(Double minPrice, Double maxPrice);

    List<PostDto> findByCategoryContainsIgnoreCase(String query);

    List<PostDto> findByCriteriaAndSort(String criteria,String sort,Boolean asc);
    List<PostDto> findByTypeAndSort(String criteria,String sort,Boolean asc);


}

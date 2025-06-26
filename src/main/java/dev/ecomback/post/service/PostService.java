package dev.ecomback.post.service;

import dev.ecomback.post.dto.NewPostDto;
import dev.ecomback.post.dto.PostDto;
import dev.ecomback.post.dto.QueryDto;
import dev.ecomback.post.dto.ReceiptDto;
import dev.ecomback.post.model.Adjustment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostDto addNewPost(String author, NewPostDto newPostDto);


    PostDto findPostById(String id);


    List<PostDto> findPostsWithCriteriaAndSort(String field, Boolean asc, QueryDto query);

    List<PostDto> getAllPosts();
    List<ReceiptDto> getAllReceipts();

    PostDto removePost(String id);

    PostDto updatePost(String id, NewPostDto newPostDto);

    void deleteFileFromS3(String fileName);

    String saveFiles(MultipartFile file);

    Adjustment adjust(String id, String author, int num, boolean add);


}

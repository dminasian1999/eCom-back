package dev.ecomback.post.controller;

import dev.ecomback.post.dto.NewPostDto;
import dev.ecomback.post.dto.PostDto;
import dev.ecomback.post.dto.QueryDto;
import dev.ecomback.post.dto.ReceiptDto;
import dev.ecomback.post.model.Adjustment;
import dev.ecomback.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    final PostService postService;

    @DeleteMapping("/post/file/delete/{file}")
    public void deleteFile(@PathVariable String file) {
        System.out.println("delete");
         postService.deleteFileFromS3(file);
    }

    @PostMapping("/post/file/upload")
    public String uploadFiles(@RequestParam("file") MultipartFile file) {
        return postService.saveFiles(file);
    }


    @PostMapping("/post/{author}")
    public PostDto addNewPost(@PathVariable String author, @RequestBody NewPostDto newPostDto) {

        return postService.addNewPost(author, newPostDto);
    }




    @GetMapping("/post/{id}")
    public PostDto findPostById(@PathVariable String id) {
        return postService.findPostById(id);
    }




    @PostMapping("/post/search/{field}/{asc}")
    public List<PostDto> findPostsWithCriteriaAndSort(@PathVariable String field, @PathVariable Boolean asc, @RequestBody QueryDto query) {
        return postService.findPostsWithCriteriaAndSort(field, asc, query);
    }

    @GetMapping("/posts/receipts")
    public List<ReceiptDto> getAllReceipts() {
        return postService.getAllReceipts();
    }

    @GetMapping("/posts")
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @DeleteMapping("/post/{id}")
    public PostDto removePost(@PathVariable String id) {

        return postService.removePost(id);
    }

    @PutMapping("/post/{id}")
    public PostDto updatePost(@PathVariable String id, @RequestBody NewPostDto newPostDto) {
        return postService.updatePost(id, newPostDto);
    }
    @PostMapping("/post/{id}/adjust/{add}/number/{num}/{author}")
    public Adjustment adjust(@PathVariable String id, @PathVariable String author, @PathVariable Boolean add, @PathVariable int num) {
        return  postService.adjust(id,author,num,add);

    }

//    @PutMapping("/post/{id}/comment/{author}")
//    public PostDto addComment(@PathVariable String id, @PathVariable String author,
//                              @RequestBody NewCommentDto newCommentDto) {
//        return postService.addComment(id, author, newCommentDto);
//    }
}

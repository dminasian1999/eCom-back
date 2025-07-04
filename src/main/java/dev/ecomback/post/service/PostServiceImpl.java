package dev.ecomback.post.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import dev.ecomback.post.dao.PostRepository;
import dev.ecomback.post.dao.ReceiptRepository;
import dev.ecomback.post.dto.NewPostDto;
import dev.ecomback.post.dto.PostDto;
import dev.ecomback.post.dto.QueryDto;
import dev.ecomback.post.dto.ReceiptDto;
import dev.ecomback.post.dto.exceptions.PostNotFoundException;
import dev.ecomback.post.model.Adjustment;
import dev.ecomback.post.model.Post;
import dev.ecomback.post.model.Receipt;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final PostRepository postRepository;
    private final ReceiptRepository receiptRepository;

    @Value("${aws.s3.access.key}")
    private String accessKey;

    @Value("${aws.s3.secret.key}")
    private String secretKey;

    @Override
    public PostDto addNewPost(String author, NewPostDto newPostDto) {
        Post post = modelMapper.map(newPostDto, Post.class);


        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    private String saveFilePicToS3(MultipartFile file) {
        try {
            String s3FileName = file.getOriginalFilename();
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            String bucketName = "file-upload-dav";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, s3FileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public void deleteFileFromS3(String fileName) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
        String bucketName = "file-upload-dav";
        amazonS3Client.deleteObject(bucketName, fileName);
    }

    @Override
    public PostDto findPostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> findPostByCategory(String category) {
        return postRepository.findByCategory(category)
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }

    @Override
    public List<PostDto> findPostsWithCriteriaAndSort(String sortField, Boolean asc, QueryDto queryDto) {
        Query mongoQuery = new Query();

        if (queryDto.getTitle() != null && !queryDto.getTitle().trim().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("title").regex(queryDto.getTitle().trim(), "i"));
        }
        if (queryDto.getDateFrom() != null) {
            mongoQuery.addCriteria(Criteria.where("dateFrom").gte(queryDto.getDateFrom()));
        }
        if (queryDto.getDateTo() != null) {
            mongoQuery.addCriteria(Criteria.where("dateFrom").lte(queryDto.getDateTo()));
        }
        if (queryDto.getCategory() != null && !queryDto.getCategory().trim().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("category").is(queryDto.getCategory().trim()));
        }
        if (queryDto.getCreator() != null && !queryDto.getCreator().trim().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("creator").regex(queryDto.getCreator().trim(), "i"));
        }
        if (queryDto.getProvenance() != null && !queryDto.getProvenance().trim().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("provenance").regex(queryDto.getProvenance().trim(), "i"));
        }
        if (queryDto.getMaterials() != null && !queryDto.getMaterials().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("materials").in(queryDto.getMaterials()));
        }

        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        mongoQuery.with(Sort.by(direction, sortField));

        List<Post> posts = mongoTemplate.find(mongoQuery, Post.class);
        return posts.stream()
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getAllPosts() {
        return StreamSupport.stream(postRepository.findAll().spliterator(), false)
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReceiptDto> getAllReceipts() {
        return StreamSupport.stream(receiptRepository.findAll().spliterator(), false)
                .map(p -> modelMapper.map(p, ReceiptDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto removePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.delete(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(String id, NewPostDto newPostDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        modelMapper.map(newPostDto, post);


        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public Adjustment adjust(String id, String author, int num, boolean add) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException());
        Adjustment adjustment = new Adjustment(num, add, author);
        post.adjust(adjustment);
        postRepository.save(post);
        double totalSell = post.getSell() * num;
        double totalbuy = post.getBuy() * num;
        double income = post.getSell() - post.getBuy();

        Receipt receipt = new Receipt(post.getName(), post.getImageUrl(), num, totalSell, totalbuy, totalSell - totalbuy, author, post.getCategory());
        receiptRepository.save(receipt);
        return adjustment;
    }

    @Override
    public List<PostDto> findPostsByIds(String[] ids) {
        return postRepository.findByIdIn(ids)
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }


    @Override
//    public String saveFiles(MultipartFile file) {
    public String saveFiles(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || file.isEmpty()) {
                throw new IllegalArgumentException("Invalid file");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed.");
            }

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(file.getSize());

            String bucketName = "file-upload-dav";
            String key = "stationery/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("S3 upload failed: " + e.getMessage(), e);
        }
    }


}

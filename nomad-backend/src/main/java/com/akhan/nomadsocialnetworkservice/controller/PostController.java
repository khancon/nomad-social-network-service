package com.akhan.nomadsocialnetworkservice.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Comment;
import com.akhan.nomadsocialnetworkservice.model.Post;
import com.akhan.nomadsocialnetworkservice.repository.PostRepository;
import com.akhan.nomadsocialnetworkservice.service.CommentService;
import com.akhan.nomadsocialnetworkservice.service.EventService;
import com.akhan.nomadsocialnetworkservice.service.PostService;
// import com.akhan.nomadsocialnetworkservice.service.UserService;
import com.akhan.nomadsocialnetworkservice.service.UserService;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PostController {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private EventService eventService;

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        LOGGER.info("Retrieving all posts...");
        return new ResponseEntity<>(postService.getAllPosts(),HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") String id){
        LOGGER.info("Retrieving post for id {}", id);
        return new ResponseEntity<Post>(postService.getPostById(id), HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) throws FieldDoesNotExistException{
        LOGGER.info("Creating new post object...");
        postService.checkForMandatoryMissingFields(post);
        post.setEventId(eventService.getEventById(post.getEventId()).getId());
        post.setUserCreatorId(userService.getUserById(post.getUserCreatorId()).getId());
        return new ResponseEntity<Post>(postService.createPost(post), HttpStatus.OK);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updateEventFully(@PathVariable("id") String id, @RequestBody Post postDetails) {
        LOGGER.info("Updating post {} with new details: {}", id, postDetails);
        return new ResponseEntity<Post>(postService.updatePostFully(id, postDetails), HttpStatus.OK);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Post> updatePostPartially(@PathVariable("id") String postId, @RequestBody Map<String, Object> changes){
        LOGGER.info("Updating post {} partially with new details: {}", postId, changes);
        return new ResponseEntity<Post>(postService.updatePostPartially(postId, changes), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") String id){
        //delete comments
        Post post = postService.getPostById(id);
        post.getComments().forEach(commentId -> commentService.deleteComment(commentId));
        postService.deletePost(id);
        if(postRepository.findById(id).isEmpty()){
            LOGGER.info("Post {} successfully deleted", id);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @PostMapping("/posts/comment/{id}")
    public ResponseEntity<Post> addCommentToPost(@PathVariable("id") String postId, @RequestBody Comment commentDetails){
        LOGGER.info("Adding comment to post {}", postId);
        Comment comment = commentService.createComment(commentDetails);
        return new ResponseEntity<Post>(postService.addCommentToPost(postId, comment.getId()), HttpStatus.OK);
    }

}


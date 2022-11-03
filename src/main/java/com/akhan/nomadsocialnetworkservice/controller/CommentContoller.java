package com.akhan.nomadsocialnetworkservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Comment;
import com.akhan.nomadsocialnetworkservice.model.Post;
import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.repository.CommentRepository;
import com.akhan.nomadsocialnetworkservice.service.CommentService;
import com.akhan.nomadsocialnetworkservice.service.PostService;
import com.akhan.nomadsocialnetworkservice.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CommentContoller {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(){
        LOGGER.info("Retrieving all comments...");
        return new ResponseEntity<>(commentService.getAllComments(),HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") String id){
        LOGGER.info("Retrieving comment for id {}", id);
        return new ResponseEntity<Comment>(commentService.getCommentById(id), HttpStatus.OK);
    }

    @GetMapping("/comments/post/allComments/{postId}")
    public ResponseEntity<List<Comment>> getAllCommentsForPost(@PathVariable("postId") String postId){
        LOGGER.info("Retrieving all comments for Post {}", postId);
        List<Comment> comments = postService.getPostById(postId).getComments().stream().map(commentId -> commentService.getCommentById(commentId)).collect(Collectors.toList());
        return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) throws FieldDoesNotExistException{
        LOGGER.info("Creating new comment object...");
        commentService.checkForMandatoryMissingFields(comment);
        Post post = postService.getPostById(comment.getPostId());
        comment.setPostId(post.getId());
        User user = userService.getUserById(comment.getUserId());
        comment.setUserId(user.getId());
        comment.setUserFirstName(user.getFirstName());
        comment.setUserLastName(user.getLastName());
        comment.setUserProfileImage(user.getProfileImage());
        comment = commentService.createComment(comment);
        postService.addCommentToPost(comment.getPostId(), comment.getId());
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateEventFully(@PathVariable("id") String id, @RequestBody Comment commentDetails) {
        LOGGER.info("Updating comment {} with new details: {}", id, commentDetails);
        return new ResponseEntity<Comment>(commentService.updateCommentFully(id, commentDetails), HttpStatus.OK);
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Comment> updateCommentPartially(@PathVariable("id") String commentId, @RequestBody Map<String, Object> changes){
        LOGGER.info("Updating comment {} partially with new details: {}", commentId, changes);
        return new ResponseEntity<Comment>(commentService.updateCommentPartially(commentId, changes), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") String id){
        commentService.deleteComment(id);
        if(commentRepository.findById(id).isEmpty()){
            LOGGER.info("Comment {} successfully deleted", id);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @PostMapping("/comments/likes/{id}")
    public ResponseEntity<Comment> addLikes(@PathVariable("id") String id, @RequestParam("userIds") List<String> userIds){
        userIds = userIds.stream().map(userId -> userService.getUserById(userId).getId()).collect(Collectors.toList());
        Comment comment = commentService.addLikes(userIds, id);
        LOGGER.info("Likes added to comment {}", comment.getId());
        return new ResponseEntity<Comment>(commentRepository.save(comment), HttpStatus.OK);
    }

    @DeleteMapping("/comments/likes/{id}")
    public ResponseEntity<Comment> removeLikes(@PathVariable("id") String id, @RequestParam("userId") String userId){
        userId = userService.getUserById(userId).getId();
        Comment comment = commentService.removeLikes(userId, id);
        LOGGER.info("Like removed from comment {}", comment.getId());
        return new ResponseEntity<Comment>(commentRepository.save(comment), HttpStatus.OK);
    }

}

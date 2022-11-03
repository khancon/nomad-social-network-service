package com.akhan.nomadsocialnetworkservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EntityNotFoundException;
import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Comment;
import com.akhan.nomadsocialnetworkservice.model.User;
import com.akhan.nomadsocialnetworkservice.repository.CommentRepository;
import com.akhan.nomadsocialnetworkservice.repository.PostRepository;
import com.akhan.nomadsocialnetworkservice.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public Comment getCommentById(String id) throws EntityNotFoundException{
        Optional<Comment> _comment = commentRepository.findById(id);
        if(_comment.isEmpty()){
            LOGGER.error("Comment with id \'{}\' does not exist", id);
            throw new EntityNotFoundException("Comment with id \'" + id + "\' does not exist");
        }
        return _comment.get();
    }

    public Comment createComment(Comment comment) throws FieldDoesNotExistException{
        comment.setCreatedAt(Instant.now());
        LOGGER.info("Comment details: {}", comment);
        Comment _comment = commentRepository.save(comment);
        if(_comment == null){
            LOGGER.error("Comment {} could not be saved to repository", _comment);
            throw new RuntimeException("Comment " + comment + " could be saved to repository");
        }
        return _comment;
    }

    public void checkForMandatoryMissingFields(Comment comment){
        List<String> missingFields = new ArrayList<>();
        if(comment.getPostId() == null){
            missingFields.add("postId");
        }
        if(comment.getContent() == null){
            missingFields.add("content");
        }
        if(comment.getUserId() == null){
            missingFields.add("userId");
        }
        // if(comment.getUserId() == null){
        //     missingFields.add("userId");
        // }
        if(missingFields.size() > 0){
            String errString = String.join(", ", missingFields);
            LOGGER.error("Following fields missing from input: {}", errString);
            throw new FieldDoesNotExistException("Following fields missing from input --> {" + errString + "}");
        }
    }

    public Comment updateCommentFully(String commentId, Comment commentDetails){
        Comment comment = getCommentById(commentId);
        comment.setContent(commentDetails.getContent());
        User user = userService.getUserById(comment.getUserId());
        comment.setUserId(user.getId());
        comment.setUserFirstName(user.getFirstName());
        comment.setUserLastName(user.getLastName());
        comment.setUserProfileImage(user.getProfileImage());
        // comment.setUserProfileImage(commentDetails.getUserProfileImage());
        return commentRepository.save(comment);
    }

    public Comment updateCommentPartially(String id, Map<String, Object> changes){
        Comment comment = getCommentById(id);
        changes.forEach((change, value) -> {
            LOGGER.info("Changing field \'{}\' for comment {}", change, id);
            switch(change){
                case "content": comment.setContent((String) value); break;
                case "userId": 
                    User user = userService.getUserById((String) value);
                    comment.setUserId(user.getId());
                    comment.setUserFirstName(user.getFirstName());
                    comment.setUserLastName(user.getLastName());
                    comment.setUserProfileImage(user.getProfileImage());
                    break;
                default: throw new FieldDoesNotExistException("Field \'" + change + "\' cannot be changed in comment model");
            }
        });
        return commentRepository.save(comment);
    }

    public void deleteComment(String id){
        commentRepository.deleteById(id);
    }

    public Comment addLikes(List<String> userIds, String commentId){
        Comment comment = getCommentById(commentId);
        userIds.forEach(userId -> {
            if(!comment.addLike(userId)){
                LOGGER.warn("User {} already liked comment {}", userId, commentId);
            }
        });
        return comment;
    }

    public Comment removeLikes(String userId, String commentId){
        Comment comment = getCommentById(commentId);
        if(!comment.removeLike(userId)){
            LOGGER.warn("User {} already removed like from comment {}", userId, commentId);
        }
        return comment;
    }
    
}

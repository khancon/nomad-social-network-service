package com.akhan.nomadsocialnetworkservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EntityNotFoundException;
import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Comment;
import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.model.Post;
import com.akhan.nomadsocialnetworkservice.repository.CommentRepository;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.repository.PostRepository;

@Service
public class PostService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EventRepository eventRepository;

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPostById(String id) throws EntityNotFoundException{
        Optional<Post> _post = postRepository.findById(id);
        if(_post.isEmpty()){
            LOGGER.error("Post with id \'{}\' does not exist", id);
            throw new EntityNotFoundException("Post with id \'" + id + "\' does not exist");
        }
        return _post.get();
    }

    public void deletePost(String id){
        postRepository.deleteById(id);
    }

    public void checkForMandatoryMissingFields(Post post) throws FieldDoesNotExistException{
        LOGGER.info("Post details: {}", post);
        List<String> missingFields = new ArrayList<>();
        if(post.getEventId() == null){
            missingFields.add("eventId");
        }
        if(post.getUserCreatorId() == null){
            missingFields.add("userCreatorId");
        }
        if(missingFields.size() > 0){
            String errString = String.join(", ", missingFields);
            LOGGER.error("Following fields missing from input: {}", errString);
            throw new FieldDoesNotExistException("Following fields missing from input --> {" + errString + "}");
        }
    }

    public Post createPost(Post post) throws FieldDoesNotExistException {
        //process event id to actual Event object
        Event event = eventRepository.findById(post.getEventId()).get();
        if(!event.isPostable()){
            LOGGER.error("Event {} cannot be posted; event.postable set to false", event.getId());
            throw new RuntimeException("Event " + event.getId() + " cannot be posted; event.postable set to false");
        }
        post.setEventId(event.getId());
        post.setCreatedAt(Instant.now());
        Post _post = postRepository.save(post);
        if(_post == null){
            LOGGER.error("Post {} could not be saved to repository", post);
            throw new RuntimeException("Post " + post + " could not be saved to repository");
        }
        return _post;
    }

    public Post updatePostFully(String postId, Post postDetails){
        Post post = getPostById(postId);
        post.setEventId(postDetails.getEventId());
        post.setUserCreatorId(postDetails.getUserCreatorId());
        // post.setUserProfileImage(postDetails.getUserProfileImage());
        return postRepository.save(post);
    }

    public Post updatePostPartially(String id, Map<String, Object> changes){
        Post post = getPostById(id);
        changes.forEach((change, value) -> {
            LOGGER.info("Changing field \'{}\' for post {}", change, id);
            switch(change){
                case "eventId": post.setEventId((String) value); break;
                case "userCreatorId": post.setUserCreatorId((String) value); break;
                default: throw new FieldDoesNotExistException("Field \'" + change + "\' cannot be changed in post model");
            }
        });
        return postRepository.save(post);
    }

    public Post addCommentToPost(String postId, String commentId){
        Post post = getPostById(postId);
        if(post.addComment(commentId)){
            LOGGER.info("Post {} added comment {}", postId, commentId);
        } else {
            LOGGER.error("Post {} could not add comment {}", postId, commentId);
        }
        return postRepository.save(post);
    }

    // public List<Comment> retrieveAllCommentsForPost(String postId){
    //     return getPostById(postId).getComments().stream().map(commentId -> commentRepository.findById(commentId).get()).collect(Collectors.toList());
    // }

    public List<Comment> retrieveAllCommentsForPost(String postId){
        return getPostById(postId).getComments().stream().map(commentId -> commentRepository.findById(commentId).get()).collect(Collectors.toList());
    }

}

package com.akhan.nomadsocialnetworkservice.controller;

import java.util.List;
import java.util.Optional;

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

import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.TagRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TagController {
    
    @Autowired
    TagRepository tagRepository;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags(){
        try {
            List<Tag> tags = tagRepository.findAll();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") String id){
        Optional<Tag> _tag = tagRepository.findById(id);
        return _tag.isPresent() ? new ResponseEntity<>(_tag.get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag){
        System.out.println("hit endpoint");
        try {
            System.out.println("attempting to save tag...");
            Tag _tag = tagRepository.save(tag);
            System.out.println("saved tag");
            return new ResponseEntity<>(_tag, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("UH OH");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTagFully(@PathVariable("id") String id, @RequestBody Tag tagDetails){
        try {
            Optional<Tag> _tag = tagRepository.findById(id);
            if(_tag.isPresent()){
                Tag tag = _tag.get();
                tag.setLabel(tagDetails.getLabel());
                tag.setLabelDescription(tagDetails.getLabel());
                return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTagPartially(@PathVariable("id") String id, @RequestBody Tag tagDetails){
        try {
            Optional<Tag> _tag = tagRepository.findById(id);
            if(_tag.isPresent()){
                Tag tag = _tag.get();
                if(tagDetails.getLabel() != null){
                    tag.setLabel(tagDetails.getLabel());
                }
                if(tagDetails.getLabelDescription() != null){
                    tag.setLabelDescription(tagDetails.getLabelDescription());
                }
                return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") String id){
        try{
            tagRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @DeleteMapping("/tags")
    // public ResponseEntity<HttpStatus> deleteAllTags(@PathVariable("id") String id){
    //     try{
    //         tagRepository.deleteAll();
    //         return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}

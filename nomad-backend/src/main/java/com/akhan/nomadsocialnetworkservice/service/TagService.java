package com.akhan.nomadsocialnetworkservice.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EntityNotFoundException;
import com.akhan.nomadsocialnetworkservice.error.TagAlreadyExistsException;
import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.repository.TagRepository;

@Service
public class TagService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    TagRepository tagRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    public Tag save(Tag tag) throws TagAlreadyExistsException{
        if(tagRepository.findByLabel(tag.getLabel()) != null){
            LOGGER.error("Input tag with label \'{}\' already exists", tag.getLabel());
            throw new TagAlreadyExistsException("Input tag already exists");
        }
        return tagRepository.save(tag);
    }

    public Tag findById(String id) throws EntityNotFoundException{
        if(tagRepository.findById(id).isEmpty()){
            LOGGER.error("Input tag with id \'{}\' does not exist", id);
            throw new EntityNotFoundException("Input tag id \'"+ id+ "\' does not exist");
        }
        return tagRepository.findById(id).get();
    }

    public Tag findByLabel(String label) throws EntityNotFoundException{
        if(tagRepository.findByLabel(label) == null){
            LOGGER.error("Input tag with label \'{}\' does not exist", label);
            throw new EntityNotFoundException("Input tag label does not exist");
        }
        return tagRepository.findByLabel(label);
    }

    public Tag findOrCreateTagByLabel(String label){
        if(tagRepository.findByLabel(label) == null){
            LOGGER.info("Input tag with label \'{}\' does not exist. Creating...", label);
            tagRepository.save(new Tag(label));
        }
        return tagRepository.findByLabel(label);
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    //delete tag
    public void delete(Tag tag) throws RuntimeException{
        tagRepository.delete(tag);
    }


    //add event ids to a tag
    public Tag addEventsToTag(List<String> eventIds, String tagId){
        LOGGER.info("Attemping to add {} events to tag {}", eventIds.size(), tagId);
        Tag tag = findById(tagId);
        Set<String> tagEvents = tag.getEventIds();
        eventIds.forEach(id -> {
            if(eventRepository.findById(id).isEmpty()){
                LOGGER.error("Event {} does not exist in event repository. Could not be added to tag {}", id, tagId);
            } else {
                if(tagEvents.contains(id)){
                    LOGGER.warn("Event {} already in Tag {}. Skipping...", id, tagId);
                } else {
                    tagEvents.add(id);
                    Tag tempTag = tagRepository.findById(tagId).get();
                    tempTag.addEventId(id);
                    tagRepository.save(tempTag);
                    LOGGER.info("Event {} added to tag {}", id, tagId);
                }
            }
        });
        tag.setEventIds(tagEvents);
        return tagRepository.save(tag);
    }

}

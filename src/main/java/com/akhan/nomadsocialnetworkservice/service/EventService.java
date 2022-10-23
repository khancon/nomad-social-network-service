package com.akhan.nomadsocialnetworkservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EventNotFoundException;
import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EventService {
    
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    EventRepository eventRepository;

    // public Event addTagToEvent(Tag tag, String eventId) throws RuntimeException{
    //     Optional<Event> _event = eventRepository.findById(eventId);
    //     LOGGER.info("Tag {} added to event {}", tag.getId(), eventId);
    //     if(!_event.isPresent()){
    //         throw new EventNotFoundException("Event with id \'" + eventId + "\' does not exist");
    //     }
    //     Event event = _event.get();
    //     if(event.getTags().contains(tag)){
    //         throw new TagAlreadyExistWithinEventException("Tag already attached to Event with id \'" + eventId + "\'");
    //     }
    //     List<Tag> eventTags = event.getTags();
    //     eventTags.add(tag);
    //     event.setTags(eventTags);
    //     // eventRepository.save(event);
    //     // Map<String, List<Tag>> map = new HashMap<>();
    //     // map.put(event.getId(), event.getTags());
    //     return event;
    // }

    public Event addTagsToEvent(List<Tag> tags, String eventId) throws EventNotFoundException{
        Optional<Event> _event = eventRepository.findById(eventId);
        if(!_event.isPresent()){
            throw new EventNotFoundException("Event with id \'" + eventId + "\' does not exist");
        }
        Event event = _event.get();
        Map<String, String> eventTags = event.getTags();
        tags.forEach(tag -> {
            if(eventTags.containsKey(tag.getId())){
                LOGGER.warn("Tag {} already attached to Event {}. Skipping...",tag.getId(), eventId);
            } else {
                eventTags.put(tag.getId(), tag.getLabel());
                LOGGER.info("Tag {} added to event {}", tag.getId(), eventId);
            }
        });
        event.setTags(eventTags);
        return event;
    }


}

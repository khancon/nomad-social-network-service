package com.akhan.nomadsocialnetworkservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akhan.nomadsocialnetworkservice.error.EntityNotFoundException;
import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.repository.TagRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Autowired
    TagRepository tagRepository;

    // @Autowired
    // TagService tagService;

    // public Event addTagToEvent(Tag tag, String eventId) throws RuntimeException{
    //     Optional<Event> _event = eventRepository.findById(eventId);
    //     LOGGER.info("Tag {} added to event {}", tag.getId(), eventId);
    //     if(!_event.isPresent()){
    //         throw new EntityNotFoundException("Event with id \'" + eventId + "\' does not exist");
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

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Event getEventById(String id) throws EntityNotFoundException{
        Optional<Event> _event = eventRepository.findById(id);
        if(_event.isEmpty()){
            LOGGER.error("Event with id \'{}\' does not exist", id);
            throw new EntityNotFoundException("Event with id \'" + id + "\' does not exist");
        }
        return _event.get();
    }

    public Event createEvent(Event event) throws FieldDoesNotExistException{
        List<String> missingFields = new ArrayList<String>();
        if(event.getName() == null){
            missingFields.add("name");
        }
        if(event.getLocation() == null){
            missingFields.add("location");
        }
        if(event.getDate() == null){
            missingFields.add("date");
        }
        if(missingFields.size() > 0){
            String errString = String.join(", ", missingFields);
            LOGGER.error("Following fields missing from input: {}", errString);
            throw new FieldDoesNotExistException("Following fields missing from input --> {" + errString + "}");
        }
        return eventRepository.save(event);
    }

    public void deleteEvent(String id){
        id = getEventById(id).getId();
        eventRepository.deleteById(id);
    }

    public Event updateEventFully(String eventId, Event eventDetails){
        Event event = getEventById(eventId);
        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setLocation(eventDetails.getLocation());
        event.setDate(eventDetails.getDate());
        event.setTime(eventDetails.getTime());
        event.setPublic(eventDetails.isPublic());
        event.setPostable(eventDetails.isPostable());
        event.setUserCreatorId(eventDetails.getUserCreatorId());
        return eventRepository.save(event);
    }

    public Event updateEventPartially(String eventId, Map<String, Object> changes) throws FieldDoesNotExistException {
        Event event = getEventById(eventId);
        changes.forEach((change, value) -> {
            LOGGER.info("Changing field \'{}\' for event {}", change, eventId);
            switch(change){
                case "name": event.setName((String) value); break;
                case "description": event.setDescription((String) value); break;
                case "location": event.setLocation((String) value); break;
                case "date": event.setDate(LocalDate.parse((String) value)); break; //yyyy-MM-dd
                case "time": event.setTime(LocalTime.parse((String) value)); break; //HH:mm:ss
                case "isPublic": event.setPublic(Boolean.parseBoolean((String) value)); break;
                case "isPostable": event.setPostable(Boolean.parseBoolean((String) value)); break;
                default: throw new FieldDoesNotExistException("Field \'" + change + "\' does not exist in event model");
            }
        });
        return eventRepository.save(event);
    }

    public Event addTagsToEvent(List<Tag> tags, String eventId){
        Event event = getEventById(eventId);
        Map<String, String> eventTags = event.getTags();
        tags.forEach(tag -> {
            if(eventTags.containsKey(tag.getId())){
                LOGGER.warn("Tag {} already attached to Event {}. Skipping...",tag.getId(), eventId);
            } else {
                LOGGER.info("Tag {} added to event {}", tag.getId(), eventId);
            }
        });
        event.setTags(eventTags);
        return event;
    }

    public Event addUsersAttending(List<String> userIds, String eventId){
        Event event = getEventById(eventId);
        userIds.forEach(userId -> {
            if(!event.addUserAttending(userId)){
                LOGGER.warn("Event {} already has user {} set as attending. Skipping...",event.getId(), userId);
            } else {
                LOGGER.info("User {} added to event {} as attending.", userId, event.getId());
            }
        });
        return event;
    }

    public Event addUserLiked(List<String> userIds, String eventId){
        Event event = getEventById(eventId);
        userIds.forEach(userId -> {
            if(!event.addUserLiked(userId)){
                LOGGER.warn("Event {} already has user {} set as attending. Skipping...",event.getId(), userId);
            } else {
                LOGGER.info("User {} added to event {} as attending.", userId, event.getId());
            }
        });
        return event;
    }
}

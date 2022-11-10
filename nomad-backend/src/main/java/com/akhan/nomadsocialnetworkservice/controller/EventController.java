package com.akhan.nomadsocialnetworkservice.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.service.EventService;
// import com.akhan.nomadsocialnetworkservice.service.ResponseObjectService;
import com.akhan.nomadsocialnetworkservice.service.TagService;
import com.akhan.nomadsocialnetworkservice.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EventController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    // @Autowired
    // private ResponseObjectService rService;

    // @Autowired
    // private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(){
        LOGGER.info("Retrieving all events...");
        return new ResponseEntity<>(eventService.getAllEvents(),HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") String id){
        LOGGER.info("Retrieving event for id {}", id);
        return new ResponseEntity<Event>(eventService.getEventById(id), HttpStatus.OK);
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws FieldDoesNotExistException{
        LOGGER.info("Creating new event object...");
        return new ResponseEntity<Event>(eventService.createEvent(event), HttpStatus.CREATED);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Event> updateEventFully(@PathVariable("id") String eventId, @RequestBody Event eventDetails){
        LOGGER.info("Updating event {} with new details: {}", eventId, eventDetails);
        return new ResponseEntity<Event>(eventService.updateEventFully(eventId, eventDetails), HttpStatus.OK);
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<Event> updateEventPartially(@PathVariable("id") String eventId, @RequestBody Map<String, Object> changes) throws FieldDoesNotExistException{
        LOGGER.info("Updating event {} partially with new details: {}", eventId, changes);
        return new ResponseEntity<Event>(eventService.updateEventPartially(eventId, changes), HttpStatus.OK);
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") String id){
        LOGGER.info("Deleting event with id {}...", id);
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/events/addTags/{eventId}")
    public ResponseEntity<Event> addTag(@PathVariable("eventId") String id, @RequestBody List<Tag> tags){
        // processTagsLisxt(tags);
        tags = tags.stream().map(tag -> processTag(tag)).collect(Collectors.toList());
        final Event event = eventService.addTagsToEvent(tags, id);
        return new ResponseEntity<Event>(eventRepository.save(event), HttpStatus.OK);
    }

    @PostMapping("/events/addUsers/{eventId}")
    public ResponseEntity<Event> addUsers(@PathVariable("eventId") String eventId, @RequestParam("userIds") List<String> userIds, @RequestParam("status") String status) throws FieldDoesNotExistException{ //status can be either "attending" or "liked"
        userIds = userIds.stream().map(userId -> userService.getUserById(userId).getId()).collect(Collectors.toList());
        Event event = null;
        switch(status){
            case "attending": event = eventService.addUsersAttending(userIds, eventId); break;
            case "liked": event = eventService.addUserLiked(userIds, eventId); break;
            default: throw new FieldDoesNotExistException("status=\'" + status +"\' is invalid. Can only be \'attending\' or \'liked\'");
        }
        return new ResponseEntity<Event>(eventRepository.save(event), HttpStatus.OK);
    }

    private Tag processTag(Tag tag) throws RuntimeException{
        tag = tag.getId() != null ? tagService.findById(tag.getId()) : tag.getLabel() != null ? tagService.findOrCreateTagByLabel(tag.getLabel()) : null;
        if(tag == null){
            LOGGER.error("Issue adding tag {}. Tag missing valid id and/or label.", tag);
            throw new RuntimeException("Issue processing tag \'" + tag + "\'. Tag missing valid id and/or label.");
        }
        return tag;
    }

    // @DeleteMapping("/events")
    // public ResponseEntity<HttpStatus> deleteAllEvents(@PathVariable("id") String id){
    //     try{
    //         eventRepository.deleteAll();
    //         return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}

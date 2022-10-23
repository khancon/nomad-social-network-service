package com.akhan.nomadsocialnetworkservice.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Field;
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
import org.springframework.web.util.TagUtils;

import com.akhan.nomadsocialnetworkservice.error.FieldDoesNotExistException;
import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.model.Tag;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.repository.TagRepository;
import com.akhan.nomadsocialnetworkservice.service.EventService;
// import com.akhan.nomadsocialnetworkservice.service.ResponseObjectService;

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

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(){
        try {
            List<Event> events = eventRepository.findAll();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") String id){
        Optional<Event> _event = eventRepository.findById(id);
        return _event.isPresent() ? new ResponseEntity<>(_event.get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws FieldDoesNotExistException{
        LOGGER.info("Creating new event object...");
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

        Event _event = eventRepository.save(event);
        return new ResponseEntity<>(_event, HttpStatus.CREATED);
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Event> updateEventFully(@PathVariable("id") String id, @RequestBody Event eventDetails){
        try {
            Optional<Event> _event = eventRepository.findById(id);
            if(_event.isPresent()){
                Event event = _event.get();
                event.setName(eventDetails.getName());
                event.setDescription(eventDetails.getDescription());
                event.setLocation(eventDetails.getLocation());
                event.setDate(eventDetails.getDate());
                event.setTime(eventDetails.getTime());
                event.setPublic(eventDetails.isPublic());
                event.setPostable(eventDetails.isPostable());
                event.setUserCreatorId(eventDetails.getUserCreatorId());
                event.setTags(eventDetails.getTags());
                return new ResponseEntity<>(event, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<Event> updateEventPartially(@PathVariable("id") String id, @RequestBody Map<String, Object> changes) throws FieldDoesNotExistException{
        try {
            Optional<Event> _event = eventRepository.findById(id);
            if(_event.isPresent()){
                Event event = _event.get();
                changes.forEach((change, value) -> {
                    LOGGER.info("Changing field {} for event {}", change, id);
                    switch(change){
                        case "name": event.setName((String) value); break;
                        case "description": event.setDescription((String) value); break;
                        case "location": event.setLocation((String) value); break;
                        case "date": event.setDate(LocalDate.parse((String) value)); break; //yyyy-MM-dd
                        case "time": event.setTime(LocalTime.parse((String) value)); break; //HH:mm:ss
                        case "isPublic": event.setPublic(Boolean.parseBoolean((String) value));
                        case "isPostable": event.setPostable(Boolean.parseBoolean((String) value));
                        default: throw new FieldDoesNotExistException("Field \'" + change + "\' does not exist in event model");
                    }
                });
                return new ResponseEntity<>(eventRepository.save(event), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") String id){
        try{
            eventRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/events/addTags/{id}")
    public ResponseEntity<Event> addTag(@PathVariable("id") String id, @RequestBody List<Tag> tags){
        tags.forEach(tag -> {
            if(tagRepository.findByLabel(tag.getLabel()) == null){
                LOGGER.info("Creating Tag with label \'{}\'", tag.getLabel());
                tagRepository.save(tag);
            }
        });
        final Event event = eventService.addTagsToEvent(tags, id);
        return new ResponseEntity<Event>(eventRepository.save(event), HttpStatus.OK);
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

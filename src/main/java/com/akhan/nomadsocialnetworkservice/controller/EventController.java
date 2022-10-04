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

import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EventController {
    
    @Autowired
    EventRepository eventRepository;

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
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        System.out.println("hit endpoint");
        try {
            System.out.println("attempting to save event...");
            Event _event = eventRepository.save(event);
            System.out.println("saved event");
            return new ResponseEntity<>(_event, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("UH OH");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                event.setDateTime(eventDetails.getDateTime());
                event.setCreator(eventDetails.getCreator());
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
    public ResponseEntity<Event> updateEventPartially(@PathVariable("id") String id, @RequestBody Event eventDetails){
        try {
            Optional<Event> _event = eventRepository.findById(id);
            if(_event.isPresent()){
                Event event = _event.get();
                if(eventDetails.getName() != null){
                    event.setName(eventDetails.getName());
                }
                if(eventDetails.getDescription() != null){
                    event.setDescription(eventDetails.getDescription());
                }
                if(eventDetails.getLocation() != null){
                    event.setLocation(eventDetails.getLocation());
                }
                if(eventDetails.getDateTime() != null){
                    event.setDateTime(eventDetails.getDateTime());
                }
                if(eventDetails.getCreator() != null){
                    event.setCreator(eventDetails.getCreator());
                }
                if(eventDetails.getTags() != null){
                    event.setTags(eventDetails.getTags());
                }
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

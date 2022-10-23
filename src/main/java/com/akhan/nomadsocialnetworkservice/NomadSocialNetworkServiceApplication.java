package com.akhan.nomadsocialnetworkservice;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.akhan.nomadsocialnetworkservice.model.Event;
import com.akhan.nomadsocialnetworkservice.repository.EventRepository;
import com.akhan.nomadsocialnetworkservice.repository.TagRepository;

@SpringBootApplication
public class NomadSocialNetworkServiceApplication implements CommandLineRunner {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	TagRepository tagRepository;

	public static void main(String[] args) {
		System.out.println("SOME CHANGE 11");
		SpringApplication.run(NomadSocialNetworkServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		System.out.println("Running commandline...");
		System.out.println(eventRepository.findAll().size());
		// if(!eventRepository.findAll().stream().anyMatch(e -> e.getName().equals("Volleyball"))){
		// 	System.out.println("saving event...");
		// 	Date d1 = (Date) new SimpleDateFormat("MM-dd-yyyy").parse("08-12-2022");
		// 	eventRepository.save(new Event("Volleyball","Beginner volleyball. All welcome!!!", "3000 Some Street, Chantilly, VA 20152", d1));
		// 	System.out.println("saved!");
		// }
	}

}

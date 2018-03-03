package book.catalogue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import book.catalogue.services.PublisherService;

@RestController
public class PublisherController {

	@Autowired
	private PublisherService publisherService;

}

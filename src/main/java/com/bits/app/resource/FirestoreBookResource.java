package com.bits.app.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bits.app.entity.Book;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

/**
 * 
 * @author a73s
 *
 */
@RestController
@RequestMapping(value = "/book")
public class FirestoreBookResource {

	private static final String BOOKS = "books";

	protected Logger logger = Logger.getLogger(FirestoreBookResource.class.getName());

	@Autowired
	Firestore firestore;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Book model) {
		try {
			WriteResult writeResult = this.firestore.collection(BOOKS).document(model.getId() != null ? model.getId() : UUID.randomUUID().toString()).set(model).get();
			return new ResponseEntity(writeResult, new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable String id) {

		try {
			ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.collection(BOOKS).document(id).get();
			Book payload = documentSnapshotApiFuture.get().toObject(Book.class);
			return new ResponseEntity(payload, new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> findAll() {

		try {
			List<Map<String, Object>> payload = new ArrayList<>();
			System.out.println("TESTING HERE 1");
			ApiFuture<QuerySnapshot> results = this.firestore.collection(BOOKS).get();
			System.out.println("TESTING HERE 1.1" + results);
			results.get().getDocuments().stream().forEach(action -> {
				System.out.println("TESTING HERE 1.1.1");
				payload.add(action.getData());
			});
			System.out.println("TESTING HERE 2 --- " + payload);
			return new ResponseEntity(payload, new HttpHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}

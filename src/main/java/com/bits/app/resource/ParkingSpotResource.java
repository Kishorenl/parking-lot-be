package com.bits.app.resource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.app.entity.ParkingSpot;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;



@RestController
@RequestMapping(value = "/parking-spot")
public class ParkingSpotResource {

    private static final String PARKING_SPOTS = "parking-spots";

    protected Logger logger = Logger.getLogger(FirestoreBookResource.class.getName());

    @Autowired
    Firestore firestore;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody ParkingSpot model) {
        try {
            WriteResult writeResult = this.firestore.collection(PARKING_SPOTS).document(model.getId() != null ? model.getId() : UUID.randomUUID().toString()).set(model).get();
            return new ResponseEntity(writeResult, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable String id) {

        try {
            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = this.firestore.collection(PARKING_SPOTS).document(id).get();
            ParkingSpot payload = documentSnapshotApiFuture.get().toObject(ParkingSpot.class);
            return new ResponseEntity(payload, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //?start=2012-01-01&end=2012-01-31
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(@RequestParam(value="date", required=false) String date) throws ParseException {

        System.out.println("Start date" + date);
        DateFormat f = new SimpleDateFormat("HH:mm:ss");
        String currentTime = f.format(new Date());
       
        String start = date + " "+currentTime;
        String end = date  + " "+ "23:59:00";
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date  startDt = dateFormat.parse(start);
        Date endDt  = dateFormat.parse(end);
        
        System.out.println("Between " + startDt  + " end" + endDt);
        
        try {
            List<Map<String, Object>> payload = new ArrayList<>();
            //ApiFuture<QuerySnapshot> results = this.firestore.collection(PARKING_SPOTS).get();
            ApiFuture<QuerySnapshot> results = this.firestore.collection(PARKING_SPOTS)
                    .whereGreaterThanOrEqualTo("startTime", startDt)
                    .whereLessThan("startTime", endDt)
                    .get();
            results.get().getDocuments().stream().forEach(action -> {
                payload.add(action.getData());
            });
            return new ResponseEntity(payload, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    
}



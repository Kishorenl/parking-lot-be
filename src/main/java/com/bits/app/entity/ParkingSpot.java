package com.bits.app.entity;

import com.google.cloud.firestore.annotation.DocumentId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot {
    
    @DocumentId
    String id;

    String size;

    boolean available;

    Date startTime;
    
    //Timestamp timestamp = (Timestamp) documentSnapshot.getData().get("last_login_date");
    //Date date = timestamp.toDate();
    Date endTime;
}

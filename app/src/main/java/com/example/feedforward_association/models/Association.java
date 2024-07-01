package com.example.feedforward_association.models;

import com.example.feedforward_association.models.server.object.CreatedBy;
import com.example.feedforward_association.models.server.object.Location;
import com.example.feedforward_association.models.server.object.ObjectBoundary;
import com.example.feedforward_association.models.server.object.ObjectId;
import com.google.gson.Gson;

import java.util.Map;

public class Association {
    private ObjectId associationId;
    private String associationName;
    private String associationAddress;
    private String associationPhone;
    private String associationEmail;
    private Location associationLocation;

    public Association(){

    }
    public Association(ObjectId associationId, String associationName, String associationAddress, String associationPhone, String associationEmail, Location associationLocation) {
        this.associationId = associationId;
        this.associationName = associationName;
        this.associationAddress = associationAddress;
        this.associationPhone = associationPhone;
        this.associationEmail = associationEmail;
        this.associationLocation = associationLocation;
    }
    public Association(ObjectBoundary objectBoundary) {
        Gson gson = new Gson();
        Association temp = gson.fromJson((String) objectBoundary.getObjectDetails().get("Association"), Association.class);
        this.associationId = objectBoundary.getObjectId();
        this.associationName = temp.getAssociationName();
        this.associationAddress = temp.getAssociationAddress();
        this.associationPhone = temp.getAssociationPhone();
        this.associationEmail = temp.getAssociationEmail();
        this.associationLocation = temp.getAssociationLocation();
    }

    public ObjectId getAssociationId() {
        return associationId;
    }

    public Location getAssociationLocation() {
        return associationLocation;
    }

    public void setAssociationLocation(Location associationLocation) {
        this.associationLocation = associationLocation;
    }

    public void setAssociationId(ObjectId associationId) {
        this.associationId = associationId;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getAssociationAddress() {
        return associationAddress;
    }

    public void setAssociationAddress(String associationAddress) {
        this.associationAddress = associationAddress;
    }

    public String getAssociationPhone() {
        return associationPhone;
    }

    public void setAssociationPhone(String associationPhone) {
        this.associationPhone = associationPhone;
    }

    public String getAssociationEmail() {
        return associationEmail;
    }

    public void setAssociationEmail(String associationEmail) {
        this.associationEmail = associationEmail;
    }
    public ObjectBoundary toObjectBoundary(){
        ObjectBoundary objectBoundary = new ObjectBoundary();
        Gson gson = new Gson();
        objectBoundary.setObjectId(this.associationId);
        objectBoundary.setType("Association");
        objectBoundary.setAlias(this.associationEmail);
        objectBoundary.setActive(true);
        objectBoundary.setLocation(this.associationLocation);
        objectBoundary.setCreatedBy(new CreatedBy("2024b.gal.said", associationEmail));
        Map<String, Object> objectDetails = Map.of("Association", gson.toJson(this, Association.class));
        objectBoundary.setObjectDetails(objectDetails);
        return objectBoundary;
    }

}

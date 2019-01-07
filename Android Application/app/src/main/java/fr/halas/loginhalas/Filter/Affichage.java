package fr.halas.loginhalas.Filter;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@IgnoreExtraProperties
public class Affichage {


    private String CreatorName;
    private String Id;
    private String Module;
    private String UserID;
    private String Year;
    private int Section;
    private int Groupe;
    private String Image;
    private Timestamp Dat;

    public Affichage(String CreatorName,String Id,String UserID,String Module, String Year, int Section, int Groupe, String Image,Timestamp Dat) {
        this.CreatorName = CreatorName;
        this.Id = Id;
        this.UserID = UserID;
        this.Module = Module;
        this.Year = Year;
        this.Section = Section;
        this.Groupe = Groupe;
        this.Image = Image;
        this.Dat = Dat;
    }

    public Affichage() {}

    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getModule() {
        return Module;
    }

    public void setModule(String module) {
        Module = module;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getSection() {
        return Section;
    }

    public void setSection(int section) {
        Section = section;
    }

    public int getGroupe() {
        return Groupe;
    }

    public void setGroupe(int groupe) {
        Groupe = groupe;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public com.google.firebase.Timestamp getDate() {
        return Dat;
    }

    public void setDate(com.google.firebase.Timestamp date) {
        Dat = date;
    }

}

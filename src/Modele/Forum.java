package Modele;

import java.time.LocalDateTime;

public class Forum {
    private int IdForum;
    private String sujet;
    private LocalDateTime DateCreation;




    public Forum(String sujet, LocalDateTime DateCreation){
        this.sujet = sujet;
        this.DateCreation = DateCreation;
    }


    public void AvoirInfo(){

    }
}

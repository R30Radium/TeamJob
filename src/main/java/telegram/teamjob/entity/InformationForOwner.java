package telegram.teamjob.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InformationForOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public InformationForOwner() {}

    public String rules; //правила знакомства с собакой

    public String docs;//список документов

    public String transpartation;//рекомендации по транспортировке животного

    public String arrangementPuppy;//рекомендации по обустройству дома для щенка

    public String arrangementDog;//рекомендации по обустройству дома для взрослой собаки


    public String arrangementDogInvalid;//рекомендации по обустройству дома для собаки-инвалида

    public String cynologist;//советы кинолога по первичному общению с собакой


    public String goodCynologists;// кинологи к которым можно обратиться

    public String reject;//причины отказа (почему могут не дать собаку)

    public String getRules() {return rules;}

    public String getDocs() {return docs;}

    public String getTranspartation() {return transpartation;}

    public String getArrangementPuppy() {return arrangementPuppy;}

    public  String getArrangementDog() {return arrangementDog;}

    public  String getArrangementDogInvalid() {return arrangementDog;}

    public String getCynologist() {return cynologist;}

    public String getGoodCynologists() {
        return goodCynologists;
    }
    public String getReject() {return reject;}

}

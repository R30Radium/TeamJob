package telegram.teamjob.constants;

public enum BotMessageVolunteer {

    MESSAGE_FOR_ADOPTER("Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
            "Пожалуйста подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично " +
            "проверять уловия содеражния собаки.");

    BotMessageVolunteer(String message){
        this.message = message;
    }
    private final String message;

    public String getMessage (){
        return message;
    }
}

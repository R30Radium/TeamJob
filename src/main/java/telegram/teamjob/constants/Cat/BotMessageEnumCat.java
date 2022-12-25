package telegram.teamjob.constants.Cat;

public enum BotMessageEnumCat {
    START_MESSAGE_CAT("Здравствуй!\n" +
            "Рады приветствовать Вас в нашем Приюте для кошек\n" +
            "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать кошку домой\n" +
            "Выберите пункт меню, который вас интересует"),

    ANSWER_FOR_MENU_INFORMATION_CAT("Вы выбрали раздел \n" +
            "\"Как взять кошку из приюта \n"+
            "Ознакомьтесь пожалуйста с меню и \n"+
            "выберите интересующий вас пункт"),
    USER_CHOOSE_INSTRUCTION_CAT("Вы выбрали раздел " + "\n" + "Как взять кошку из приюта\". " + "\n"
            + "Ознакомьтесь пожалуйста " +
            "с меню и выберите интересующий вас пункт");

    private String message;

     BotMessageEnumCat(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}

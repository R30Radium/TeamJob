package telegram.teamjob.constants;



public enum BotButtonEnum {
    BUTTON_INFO("Узнать информацию о приюте"),
    BUTTON_INSTRUCTION("Как взять собаку из приюта"),
    BUTTON_RECORD("Прислать отчет о питомце"),
    BUTTON_HELP("Позвать волонтера");

    private final String message;

    BotButtonEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}


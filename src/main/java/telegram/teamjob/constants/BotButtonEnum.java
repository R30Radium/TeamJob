package telegram.teamjob.constants;



public enum BotButtonEnum {
    BUTTON_GUEST("Пользователь"),
    BUTTON_EMPLOYEE("Волонтер"),
    BUTTON_INFO("Узнать информацию о приюте"),
    BUTTON_INSTRUCTION("Как взять собаку из приюта"),
    BUTTON_RECORD("Прислать отчет о питомце"),
    BUTTON_HELP("Позвать волонтера"),


    BUTTON_KNOW (" Правила знакомства с животным "),
    BUTTON_DOC ( " Список необходимых документов "),
    BUTTON_TRANSPORTATION ( " Рекомендация по транспортировке "),
    BUTTON_PUPPY ( " Как подготовить дом для щенка "),
    BUTTON_ARRANGEMENT_DOG( " Как подготовить дом для взрослой собаки "),
    BUTTON_ARRANGEMENT_DOG_INVALID( " Как подготовить дом для собаки с " +
            " ограниченными возможностями "),
    BUTTON_CYNOLOGIST( " Рекомендации от кинологов "),
    BUTTON_GOOD_CYNOLOGIST( " Хорошие кинологи "),
    BUTTON_REJECT( " Спиоск причин отказа "),
    BUTTON_CONTACT( " Контактные данные "),

    BUTTON_CHECK_CONTACT( " Проверить контакты "),

    BUTTON_ADD_USER( " Добавить усыновителя "),
    BUTTON_CHECK_REPORTS( " Проверить отчеты "),
    BUTTON_BED_REPORT( " Сообщить о плохо заполняемом отчете "),
    BUTTON_MAKE_DECISION_ON_PROBATION( " Принять решение об испытательном сроке ");

    private final String message;

    BotButtonEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

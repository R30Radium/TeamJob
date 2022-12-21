package telegram.teamjob.constants.Cat;

public enum BotButtonForCat {

    BUTTON_INFO_CAT("Узнать информацию о приюте"),
    BUTTON_INSTRUCTION_CAT("Как взять кошку из приюта"),
    BUTTON_RECORD_CAT("Прислать отчет о питомце"),
    BUTTON_HELP_CAT("Позвать волонтера"),


    COMMAND_SECURITY_CONTACT_CAT("Контакты охраны для оформления пропуска"),

    COMMAND_CREATE_USER_CAT("\n" + "Если Вы хотите оставить свои КОНТАКТНЫЕ ДАННЫЕ для связи, " +
            "введите их строго в соотвествии с шаблоном: "
            + "\n" +  "Пушок(кот) 89061855572 Иванов Иван Иванович\n" +
            "  " +
            "если питомца у вас нет, то напишите слово нет"
    ),
    BOT_ANSWER_NOT_SAVED_INFO_CAT("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: \n" +
            "Пушок(кот) 89061855572 Иванов Иван Иванович\n" +
            " Попробуйте еще раз."),
    COMMAND_CALL_VOLUNTEER_CAT("\n"+ "Связаться с волонтером"),




    BUTTON_KNOW_CAT(" Правила знакомства с животным "),
    BUTTON_DOC_CAT( " Список необходимых документов "),
    BUTTON_TRANSPORTATION_CAT( " Рекомендация по транспортировке "),
    BUTTON_KITTY( " Как подготовить дом для котенка "),
    BUTTON_ARRANGEMENT_CAT( " Как подготовить дом для взрослой кошки "),
    BUTTON_ARRANGEMENT_CAT_INVALID( " Как подготовить дом для животного с " +
                                            " ограниченными возможностями "),
    BUTTON_REJECT_CAT( " Спиоск причин отказа "),
    BUTTON_CONTACT_CAT( " Контактные данные "),





    BUTTON_CHECK_CONTACT_CAT( " Проверить контакты "),

    BUTTON_ADD_USER_CAT( " Добавить усыновителя "),
    BUTTON_CHECK_REPORTS_CAT( " Проверить отчеты "),
    BUTTON_SEND_WARNING_ABOUT_BED_REPORT_CAT( " Сообщить о плохо заполняемом отчете "),
    BUTTON_MAKE_DECISION_ON_PROBATION_CAT( " Принять решение об испытательном сроке ");

    private String message;

    BotButtonForCat(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}


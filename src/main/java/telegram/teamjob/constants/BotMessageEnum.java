package telegram.teamjob.constants;


public enum BotMessageEnum {
    START_MESSAGE("\"Здравствуй @!\n" +
            "Я бот приюта для животных. В качестве кого вы хотели бы продолжить общение?\n" ),
    START_MESSAGE_VOLUNTEER("Готов к работе. С чего начнем? Выбери раздле меню"),
    START_MESSAGE_USER("Рады приветствовать Вас в нашем Приюте для Животных\n" +
            "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать собаку или кошку домой\n" +
            "Выберите пункт меню, который вас интересует"),
    ANSWER_FOR_MENU_SHELTER("Вы выбрали раздел \n" +
            "Узнать информацию о приюте\n"+
            "Ознакомьтесь пожалуйста с меню и \n" +
            "выберите интересующий вас пункт"),
    ANSWER_FOR_MENU_INFORMATION("Вы выбрали раздел \n" +
            "\"Как взять собаку из приюта \n"+
            "Ознакомьтесь пожалуйста с меню и \n"+
            "выберите интересующий вас пункт"),
    ANSWER_FOR_MENU_REPORT("Вы выбрали раздел меню для отправки отчета о вашем питомце\n" +
            " \n" +
            "ВАЖНО! Отчет необходимо присылать каждый день,\n" +
            " ограничений в сутках по времени сдачи отчета нет"),

    RECORD("Пожалуйста пришлите текстовый отчет о вашем питомце. \n" +
            "Текст должен соответствовать шаблону вида: \n" +
            "  \n" +
            "/record\n" +
            " рацион: мясо, молочные продукты, крупы \n" +
            " самочувствие: нос прохладный и влажный, но спит тревожно \n" +
            " поведение: правильно реагирует на команды, пока пугается от громких звуков"),

    RECORD_DIETA("Пожалуйста в следующий раз укажите\n" +
            "информацию об адаптации питомца более подробно"),
    RECORD_ADAPTATION("Пожалуйста укажите в следующий раз\n" +
            "информацию об адаптации питомца более подробно"),
    RECORD_BEHAVIOR("Пожалуйста укажите в следующий раз\n" +
            "информацию о самочувствии питомца более подробно"),
    PHOTO("Пожалуйста пришлите сегодняшнее фото вашего питомца. " +
            "Размер файла должен быть не более 2Мб." +
            "Фото необходимо отправить следующим образом: \n" +
            "  \n" +
            "/photo в этом же сообщении вставить фото питомца"),

    CREATE_USER_INFO("\"Введите данные в формате: Имя мобильный имя петомца (при наличии)\""),
    USER_NOT_FOUND_MESSAGE("Вас нет в базе\n" +
            "Введите данные в формате: Имя мобильный имя петомца (при наличии)\n" +
            "И пришлите отчет ещё раз"),
    ASK_HELP("Вы можете обратиться к волонтеру @LnBgrn"),

    SAVE_INFORMATION("Я сохранил ваши данные в базе данных."),

    SAVE_INFORMATION_CONTACT("Я сохранил ваши данные в базе данных."),

    HAVE_CONTACT("Вы уже отправляли свои данные. Выберите кнопку СВЯЗАТЬСЯ С ВОЛОНТЕРОМ"),
    REPORT_REMEMBER("Вы не прислали отчет о питомце. Пришлите пожалуйста отчет или свяжитесь с волонтером." +
            " Форма для отчета находится в разделе \"Прислать отчет о питомце\" в главном меню бота "+
            " Дата, за которую нет отчета: "),

    FIND_CONTACTS_FOR_VOLUNTEER("Найдены следующие контакты: "),

    DELETE_ALL_CONTACTS("Если вы хотите удалить все контакты из базы, введите команду /delete_contacts"),

    DELETE_COMMAND("/delete_contacts"),
    DONE("Все контакты удалены"),
    NOT_FIND_CONTACTS_FOR_VOLUNTEER("В базе нет контактов");





    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}


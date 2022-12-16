package telegram.teamjob.constants;


public enum BotMessageEnum {
    START_MESSAGE("Здравствуй!\n" +
            "Рады приветствовать Вас в нашем Приюте для Животных\n" +
            "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать собаку или кошку домой\n" +
            "Выберите пункт меню, который вас интересует"),
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

    RECORD("Отчёт за 12.15.2022\n" +
    "Диета: мясо, молочные продукты, крупы\n" +
    "Адаптация: нос прохладный и влажный, но спит тревожно\n" +
    "Изменение в поведении: правильно реагирует на команды, пока пугается от громких звуков"),

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
    DAILY_RECORD_INFO("В ежедневный отчет входит следующая информация:\n" +
            "- Фото животного;\n" +
            "- Рацион животного;\n" +
            "- Общее самочувствие и привыкание к новому месту;\n" +
            "- Изменение в поведении: отказ от старых привычек, приобретение новых;\n" +
            "- Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет.\n" +
            "\nИспользуйте данную форму в одном сообщении:\n" + "Отчёт за \"01.01.2011\"\n" +
            "Диета:\n" + "Адаптация:\n" + "Изменение в поведении:\n"),
    USER_NOT_FOUND_MESSAGE("Вас нет в базе\n" +
            "Введите данные в формате: Имя мобильный имя петомца (при наличии)\n" +
            "И пришлите отчет ещё раз"),
    USER_CHOOSE_SHELTER_INFO("Вы выбрали раздел " + "\n" + "Узнать информацию о приюте\". " + "\n"
            + "Ознакомьтесь пожалуйста " +
            "с меню и выберите интересующий вас пункт"),
    USER_CHOOSE_DOG_INSTRUCTION("Вы выбрали раздел " + "\n" + "Как взять собаку из приюта\". " + "\n"
            + "Ознакомьтесь пожалуйста " +
            "с меню и выберите интересующий вас пункт"),
    ASK_HELP("Вы можете обратиться к волонтеру @LnBgrn"),
    SAVE_INFORMATION("Информация сохранена"),

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



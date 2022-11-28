package telegram.teamjob.constants;

public enum BotMessageEnum {
    START_MESSAGE("\"Здравствуй @!\n" +
            "Рады приветствовать Вас в нашем Приюте для Животных\n" +
            "Данный бот поможет вам, если вы задумываетесь о том, чтобы забрать собаку или кошку домой\n" +
            "Выберите пункт меню, который вас интересует"),
    CREATE_USER_INFO("\"Введите данные в формате: Имя мобильный имя петомца (при наличии)\""),
    DAILY_RECORD_INFO("\"В ежедневный отчет входит следующая информация: \\n\" +\n" +
            "                        \"\\n\" +\n" +
            "                        \"- Фото животного.\\n\" +\n" +
            "                        \"- Рацион животного.\\n\" +\n" +
            "                        \"- Общее самочувствие и привыкание к новому месту.\\n\" +\n" +
            "                        \"- Изменение в поведении: отказ от старых привычек, приобретение новых.\\n\" +\n" +
            "                        \"\\n\" +\n" +
            "                        \"Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет.\""),
    USER_NOT_FOUND_MESSAGE("Вас нет в базе\n" +
            "Введите данные в формате: Имя мобильный имя петомца (при наличии)\n" +
            "И пришлите отчет ещё раз"),
    ASK_HELP("Вы можете обратиться к волонтеру @LnBgrn");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

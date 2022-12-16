package telegram.teamjob.constants;



/**
 * @author shulga_ea <br>
 * Enum - перечисление пунктов меню для получени информции о приюте, записи контакта пользователя, вызова волонтера<br>
 * @see TelegramBotUpdatesListener
 *
 */
public enum BotButtonForShelterMenuEnum {
    COMMAND_INFORMATION_ABOUT_SHELTER("Информация о приюте"),
    COMMAND_WORK_SCHEDULE_SHELTER("Время работы приюта"),
    COMMAND_ADDRESS_SHELTER("Адрес приюта"),
    COMMAND_DRIVING_DIRECTIONS("Схема проезда"),
    COMMAND_SAFETY_SHELTER("Правила поведения в приюте"),

    COMMAND_LEAVE_DATA_FOR_COMMUNICATION("Оставить данные для связи"),
    COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION("Если Вы хотите оставить свои КОНТАКТНЫЕ ДАННЫЕ для связи, " +
            "введите их строго в соотвествии с шаблоном: "
            + "\n" +  "89061877772 Иванов Иван Иванович"),

    COMMAND_CREATE_USER_("\n" + "Если Вы хотите оставить свои КОНТАКТНЫЕ ДАННЫЕ для связи, " +
            "введите их строго в соотвествии с шаблоном: "
            + "\n" +  "Шарик(пес) 89061877772 Иванов Иван Иванович\n" +
            "  " +
            "если питомца у вас нет, то напишите слово нет"
    ),
    BOT_ANSWER_NOT_SAVED_INFO("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: \n" +
            "Шарик(пес) 89061877772 Иванов Иван Иванович\n" +
            " Попробуйте еще раз."),
    BOT_ANSWER_NOT_SAVED_CONTACT("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону: \n" +
            "89061877772 Иванов Иван Иванович\n" +
            " Попробуйте еще раз."),

    BOT_ANSWER_NOT_SAVED_INFO_LOG("Не смогу сохранить запись. Введенное сообщение не соотствует шаблону"),

    BOT_ANSWER_NOT_SAVED_INFO_NEGATIVE("Информация введена не корректно. Попробуйте еще раз"),
    COMMAND_CALL_VOLUNTEER("\n"+ "Связаться с волонтером");
    private final String text;

    BotButtonForShelterMenuEnum(String text){
        this.text=text;
    }

    public String getText() {
        return text;
    }
}

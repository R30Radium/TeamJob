package telegram.teamjob.constants;




/**
 * @author shulga_ea <br>
 * Enum - перечисление пунктов меню для получени информции о приюте, записи контакта пользователя, вызова волонтера<br>
 * @see telegram.teamjob.service.TelegramBotUpdatesListener
 *
 */
public enum ServiceConstantsMenu {
    COMMAND_INFORMATION_ABOUT_SHELTER("Информация о приюте"),
    COMMAND_WORK_SCHEDULE_SHELTER("Время работы приюта"),
    COMMAND_ADDRESS_SHELTER("Адрес приюта"),
    COMMAND_DRIVING_DIRECTIONS("Схема проезда"),
    COMMAND_SAFETY_SHELTER("Правила поведения в приюте"),
    COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION("Если Вы хотите оставить свои КОНТАКТНЫЕ ДАННЫЕ для связи, введите их строго в соотвествии с шаблоном: "
            + "\n" +  "89061855572 Иванов Иван Иванович"),
    COMMAND_CALL_VOLUNTEER("\n"+ "Связаться с волонтером");
    private final String text;

    ServiceConstantsMenu(String text){
        this.text=text;
    }

    public String getText() {
        return text;
    }
}


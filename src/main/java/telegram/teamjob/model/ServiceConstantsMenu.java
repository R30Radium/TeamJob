package telegram.teamjob.model;




/**
 * @author shulga_ea <br>
 * Enum - перечисление пунктов меню для получени информции о приюте, записи контакта пользователя, вызова волонтера<br>
 * @see pro.sky.java.course6.animal_shelter_bot.service.AnimalShelterBotService
 *
 */
public enum ServiceConstantsMenu {
    COMMAND_INFORMATION_ABOUT_SHELTER("/information . Информация о приюте."),
    COMMAND_WORK_SCHEDULE_SHELTER("/workSchedule . Время работы приюта."),
    COMMAND_ADDRESS_SHELTER("/address . Адрес приюта."),
    COMMAND_DRIVING_DIRECTIONS("/way . Схема проезда."),
    COMMAND_SAFETY_SHELTER("/safety . Правила поведения на территории приюта."),
    COMMAND_SAFE_CONTACT_DETAILS_FOR_COMMUNICATION("\n" + "Если Вы хотите оставить свои контактные данные для связи, введите в соотвествии с шаблоном: "
            + "\n" +  "89061877772 Иванов Иван Иванович"),
    COMMAND_CALL_VOLUNTEER("\n"+ "/volunteer . Если вы не нашли нужной для вас информации в разделах меню или у вас остались вопросы, " +
            "выберите эту команду для связи с волонтером");
    private final String text;

    ServiceConstantsMenu(String text){
        this.text=text;
    }

    public String getText() {
        return text;
    }
}


package com.tsecho.bots.api;

import com.tsecho.bots.database.*;
import com.tsecho.bots.repos.*;
import com.tsecho.bots.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@ConfigurationProperties("telegram.bot")
public class PlasmaTelegramBot extends TelegramWebhookBot {

    String token;

    String name;

    String webhook;


    //Идентификаторы всех пользователей
    @Autowired
    UsersProfile usersProfile;

    //Идентификаторы системных программистов
    @Autowired
    SystemRepository systemRepository;

    //Идентификаторы прикладных программистов
    @Autowired
    PrikladRepository prikladRepository;

    //Идентификаторы веб программистов
    @Autowired
    WebRepository webRepository;

    //Идентификаторы разработчиков игр
    @Autowired
    GameRepository gameRepository;

    //Идентификаторы мобильных разработчиков
    @Autowired
    PhoneRepository phoneRepository;

    //Идентификаторы для заданий работодателей
    @Autowired
    WorkSend workSend;

    //Идентификатор работы удаление
    @Autowired
    WorkDelete workDelete;

    //Список для кнопки response отклик
    @Autowired
    WorkResponse workResponse;


    @Autowired
    Keyboard kb;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    Integer lastUpdateId = 0;

    Keyboard k = new Keyboard();

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        //Все пользователи
        DBService db = new DBService(usersProfile);
        //Системники
        SystemService system = new SystemService(systemRepository);
        //Приклады
        PrikladService priklad = new PrikladService(prikladRepository);
        //Веб
        WebService web = new WebService(webRepository);
        //Геймдевы
        GameService game = new GameService(gameRepository);
        //Мобиьные
        PhoneService phone = new PhoneService(phoneRepository);
        //Подготовка работы
        SendService send = new SendService(workSend);
        //Отправка работы
        DeleteService delete = new DeleteService(workDelete);
        //Отклик на работу
        ResponseService response = new ResponseService(workResponse);

        SendMessage replyMsg = new SendMessage();

        replyMsg.enableWebPagePreview();

        //Обработка Текста
        if (update.hasMessage()) {

/////////////////////
            //Сохранение
            //Identification idInTelegram = new Identification();
            //idInTelegram.setIdInTelegram(update.getMessage().getFrom().getId().toString());
            //db.saveUsers(idInTelegram);

            //Удаление
            //db.deleteUsers(update.getMessage().getFrom().getId().toString());

            //Поиск
            //db.getAllProfiles();
/////////////////////


            //Запуск
            if(update.getMessage().getText().equals("/start")){


                //проверяем есть ли пользователь вообще в списке
                Identification id = new Identification();
                id.setIdInTelegram(update.getMessage().getFrom().getId().toString());

                usersProfile.saveAndFlush(id);
                int k = 0;
                for(int i=0; i<db.getAllProfiles().size(); i++) {
                    if(db.getAllProfiles().get(i) == id) {
                        k = 1;
                        break;
                    }
                }
                if(k == 0){
                    db.saveUsers(id);
                }
                System.out.println(db.getAllProfiles());

                replyMsg.setText("Здравствуйте. Я Телеграмм-Бот, который представляет из себя Фриланс-Сервис" +
                        " для программистов.\n" +
                        "Вы можете начать выполнять или публиковать работы соответствующей кнопкой ниже.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceStart());
                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                return replyMsg;
            }
            //Удаление
            if(update.getMessage().getText().startsWith("/delete")){
                if(update.getMessage().getText().equals("/delete")){
                    replyMsg.setText("Выберите действие.");
                    replyMsg.setReplyMarkup(kb.getIkmGetServiceStart());
                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                    return replyMsg;
                }else {
                    if(!delete.getAllProfiles().isEmpty()) {
                        for (WorkDeleteId s : delete.getAllProfiles()) {
                            if (update.getMessage().getText().replace("/delete", "").equals(s.getIdDeleteWork().lines().findFirst().get().replace(update.getMessage().getFrom().getId() + "_", "")) &
                                    s.getIdDeleteWork().lines().findFirst().get().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                if (s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Системное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Системное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nСистемное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nсистемное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Системное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Системное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nСистемное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nсистемное программирование.")) {
                                    if (!system.getAllProfiles().isEmpty()) {
                                        for (SystemID d : system.getAllProfiles()) {
                                            try {
                                                DeleteMessage delete2 = new DeleteMessage();
                                                delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                System.out.println(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                delete2.setChatId(d.getIdSystem());
                                                execute(delete2);
                                            }catch (TelegramApiException e) {
                                                replyMsg.setText("Ваша работа удалена.");
                                            } catch (NumberFormatException e) {
                                                replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                            }catch (NullPointerException e) {
                                                replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                            }
                                        }
                                        if (!delete.getAllProfiles().isEmpty()) {
                                            for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                                if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                    delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                    break;
                                                }
                                            }
                                        }
                                        if(replyMsg.getText() == null) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        }
                                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    } else {
                                        try {
                                            DeleteMessage delete2 = new DeleteMessage();
                                            delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                            delete2.setChatId(update.getMessage().getFrom().getId().toString());
                                            execute(delete2);
                                        }catch (TelegramApiException e) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        } catch (NumberFormatException e) {
                                            replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                        }catch (NullPointerException e) {
                                            replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                        }
                                    }
                                    if (!delete.getAllProfiles().isEmpty()) {
                                        for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                            if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                break;
                                            }
                                        }
                                    }
                                    if(replyMsg.getText() == null) {
                                        replyMsg.setText("Ваша работа удалена.");
                                    }
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;


                                } else if (s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Прикладное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Прикладное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nПрикладное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nприкладное программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Прикладное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Прикладное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nПрикладное программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nприкладное программирование.")) {
                                    if (!priklad.getAllProfiles().isEmpty()) {
                                        for (PrikladID d : priklad.getAllProfiles()) {
                                            try {
                                                DeleteMessage delete2 = new DeleteMessage();
                                                delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                delete2.setChatId(d.getIdPriklad());
                                                execute(delete2);
                                            }catch (TelegramApiException e) {
                                                replyMsg.setText("Ваша работа удалена.");
                                            } catch (NumberFormatException e) {
                                                replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                            }catch (NullPointerException e) {
                                                replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                            }
                                        }
                                        if (!delete.getAllProfiles().isEmpty()) {
                                            for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                                if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                    delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                    break;
                                                }
                                            }
                                        }
                                        if(replyMsg.getText() == null) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        }
                                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    } else {
                                        try {
                                            DeleteMessage delete2 = new DeleteMessage();
                                            delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                            delete2.setChatId(update.getMessage().getFrom().getId().toString());
                                            execute(delete2);
                                        }catch (TelegramApiException e) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        } catch (NumberFormatException e) {
                                            replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                        }catch (NullPointerException e) {
                                            replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                        }
                                    }
                                    if (!delete.getAllProfiles().isEmpty()) {
                                        for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                            if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                break;
                                            }
                                        }
                                    }
                                    if(replyMsg.getText() == null) {
                                        replyMsg.setText("Ваша работа удалена.");
                                    }
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;

                                } else if (s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Веб-программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Веб-программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nВеб-программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nвеб-программирование") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Веб-программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Веб-программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nВеб-программирование.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nвеб-программирование.")) {
                                    if (!web.getAllProfiles().isEmpty()) {
                                        for (WebID d : web.getAllProfiles()) {
                                            try {
                                                DeleteMessage delete2 = new DeleteMessage();
                                                delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                delete2.setChatId(d.getIdWeb());
                                                execute(delete2);
                                            }catch (TelegramApiException e) {
                                                replyMsg.setText("Ваша работа удалена.");
                                            } catch (NumberFormatException e) {
                                                replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                            }catch (NullPointerException e) {
                                                replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                            }
                                        }
                                        if (!delete.getAllProfiles().isEmpty()) {
                                            for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                                if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                    delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                    break;
                                                }
                                            }
                                        }
                                        if(replyMsg.getText() == null) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        }
                                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    } else {
                                        try {
                                            DeleteMessage delete2 = new DeleteMessage();
                                            delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                            delete2.setChatId(update.getMessage().getFrom().getId().toString());
                                            execute(delete2);
                                        }catch (TelegramApiException e) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        } catch (NumberFormatException e) {
                                            replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                        }catch (NullPointerException e) {
                                            replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                        }
                                    }
                                    if (!delete.getAllProfiles().isEmpty()) {
                                        for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                            if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                break;
                                            }
                                        }
                                    }
                                    if(replyMsg.getText() == null) {
                                        replyMsg.setText("Ваша работа удалена.");
                                    }
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;

                                } else if (s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Разработка игр") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Разработка игр") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nРазработка игр") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nразработка игр") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Разработка игр.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Разработка игр.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nРазработка игр.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nразработка игр.")) {
                                    if (!game.getAllProfiles().isEmpty()) {
                                        for (GameID d : game.getAllProfiles()) {
                                            try {
                                                DeleteMessage delete2 = new DeleteMessage();
                                                delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                delete2.setChatId(d.getIdGame());
                                                execute(delete2);
                                            }catch (TelegramApiException e) {
                                                replyMsg.setText("Ваша работа удалена.");
                                            } catch (NumberFormatException e) {
                                                replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                            }catch (NullPointerException e) {
                                                replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                            }
                                        }
                                        if (!delete.getAllProfiles().isEmpty()) {
                                            for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                                if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                    delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                    break;
                                                }
                                            }
                                        }
                                        if(replyMsg.getText() == null) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        }
                                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    } else {
                                        try {
                                            DeleteMessage delete2 = new DeleteMessage();
                                            delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                            delete2.setChatId(update.getMessage().getFrom().getId().toString());
                                            execute(delete2);
                                        }catch (TelegramApiException e) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        } catch (NumberFormatException e) {
                                            replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                        }catch (NullPointerException e) {
                                            replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                        }
                                    }
                                    if (!delete.getAllProfiles().isEmpty()) {
                                        for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                            if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                break;
                                            }
                                        }
                                    }
                                    if(replyMsg.getText() == null) {
                                        replyMsg.setText("Ваша работа удалена.");
                                    }
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;

                                } else if (s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Разработка мобильных приложений") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Разработка мобильных приложений") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nРазработка мобильных приложений") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nразработка мобильных приложений") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1. Разработка мобильных приложений.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\n1.Разработка мобильных приложений.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nРазработка мобильных приложений.") |
                                        s.getIdDeleteWork().replace(s.getIdDeleteWork().lines().findFirst().get(), "").equals("\nразработка мобильных приложений.")) {
                                    if (!phone.getAllProfiles().isEmpty()) {
                                        for (PhoneID d : phone.getAllProfiles()) {
                                            try {
                                                DeleteMessage delete2 = new DeleteMessage();
                                                delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                                delete2.setChatId(d.getIdPhone());
                                                execute(delete2);
                                            }catch (TelegramApiException e) {
                                                replyMsg.setText("Ваша работа удалена.");
                                            } catch (NumberFormatException e) {
                                                replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                            }catch (NullPointerException e) {
                                                replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                            }
                                        }
                                        if (!delete.getAllProfiles().isEmpty()) {
                                            for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                                if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                    delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                    break;
                                                }
                                            }
                                        }
                                        if(replyMsg.getText() == null) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        }
                                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    } else {
                                        try {
                                            DeleteMessage delete2 = new DeleteMessage();
                                            delete2.setMessageId(Integer.parseInt(update.getMessage().getText().substring(7)));
                                            delete2.setChatId(update.getMessage().getFrom().getId().toString());
                                            execute(delete2);
                                        }catch (TelegramApiException e) {
                                            replyMsg.setText("Ваша работа удалена.");
                                        } catch (NumberFormatException e) {
                                            replyMsg.setText("Неверно введена команда.\nПример: \"/delete***\".");
                                        }catch (NullPointerException e) {
                                            replyMsg.setText("Нельзя удалить работу, которая опубликована более 48 часов.");
                                        }
                                    }
                                    if (!delete.getAllProfiles().isEmpty()) {
                                        for (int i = 0; i < delete.getAllProfiles().size(); i++) {
                                            if (delete.getAllProfiles().get(i).getIdDeleteWork().startsWith(update.getMessage().getFrom().getId() + "_")) {
                                                delete.deleteUsers(delete.getAllProfiles().get(i).getIdDeleteWork());
                                                break;
                                            }
                                        }
                                    }
                                    if(replyMsg.getText() == null) {
                                        replyMsg.setText("Ваша работа удалена.");
                                    }
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;

                                }
                            }
                        }replyMsg.setText("Нет такой работы.");
                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                        return replyMsg;
                    }else{
                        replyMsg.setText("Вы вошли как работодатель.");
                        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                        return replyMsg;
                    }
                }
            }
            if(update.getMessage().getText().startsWith("/response")){
                if(response.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы вошли как исполнитель.");
                    replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                    return replyMsg;
                }else {
                    if(update.getMessage().getFrom().getUserName() == null){
                        replyMsg.setText("У Вашего аккаунта отсутствует имя пользователя. Задайте имя пользователя и отправьте " +
                                "сообщение снова.");
                        replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                        return replyMsg;
                    }else {
                        for (WorkResponseId s: response.getAllProfiles()) {
                            if(s.getIdResponseWork().lines().findFirst().get().equals(update.getMessage().getFrom().getId().toString())) {
                                try {
                                    SendMessage mes = new SendMessage();
                                    mes.setText("Отклик:\n" + update.getMessage().getText().replace("/response", "")
                                            + "\nКандидат: @" + update.getMessage().getFrom().getUserName());
                                    mes.setChatId(s.getIdResponseWork().replace(s.getIdResponseWork().lines().findFirst().get() + "\n", ""));
                                    response.deleteUsers(s.getIdResponseWork());
                                    execute(mes);
                                }catch(TelegramApiException | NullPointerException e){
                                    replyMsg.setText("Пользователь удалился.");
                                }
                                if(replyMsg.getText() == null){
                                    replyMsg.setText("Ваш отклик отправлен.");
                                }
                                replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                return replyMsg;
                            }
                        }
                    }
                }
            }

            String resultWork = null;
            //Заменить на команду "/send"
            if(update.getMessage().getText().startsWith("1. Системное программирование") |
                    update.getMessage().getText().startsWith("1.Системное программирование") |
                    update.getMessage().getText().startsWith("Системное программирование") |
                    update.getMessage().getText().startsWith("системное программирование") |
                    update.getMessage().getText().startsWith("1. Прикладное программирование") |
                    update.getMessage().getText().startsWith("1.Прикладное программирование") |
                    update.getMessage().getText().startsWith("Прикладное программирование") |
                    update.getMessage().getText().startsWith("прикладное программирование") |
                    update.getMessage().getText().startsWith("1. Веб-программирование") |
                    update.getMessage().getText().startsWith("1.Веб-программирование") |
                    update.getMessage().getText().startsWith("Веб-программирование") |
                    update.getMessage().getText().startsWith("веб-программирование") |
                    update.getMessage().getText().startsWith("1. Разработка игр") |
                    update.getMessage().getText().startsWith("1.Разработка игр") |
                    update.getMessage().getText().startsWith("Разработка игр") |
                    update.getMessage().getText().startsWith("разработка игр") |
                    update.getMessage().getText().startsWith("1. Разработка мобильных приложений") |
                    update.getMessage().getText().startsWith("1.Разработка мобильных приложений") |
                    update.getMessage().getText().startsWith("Разработка мобильных приложений") |
                    update.getMessage().getText().startsWith("разработка мобильных приложений") |
                    update.getMessage().getText().startsWith("1. Системное программирование.") |
                    update.getMessage().getText().startsWith("1.Системное программирование.") |
                    update.getMessage().getText().startsWith("Системное программирование.") |
                    update.getMessage().getText().startsWith("системное программирование.") |
                    update.getMessage().getText().startsWith("1. Прикладное программирование.") |
                    update.getMessage().getText().startsWith("1.Прикладное программирование.") |
                    update.getMessage().getText().startsWith("Прикладное программирование.") |
                    update.getMessage().getText().startsWith("прикладное программирование.") |
                    update.getMessage().getText().startsWith("1. Веб-программирование.") |
                    update.getMessage().getText().startsWith("1.Веб-программирование.") |
                    update.getMessage().getText().startsWith("Веб-программирование.") |
                    update.getMessage().getText().startsWith("веб-программирование.") |
                    update.getMessage().getText().startsWith("1. Разработка игр.") |
                    update.getMessage().getText().startsWith("1.Разработка игр.") |
                    update.getMessage().getText().startsWith("Разработка игр.") |
                    update.getMessage().getText().startsWith("разработка игр.") |
                    update.getMessage().getText().startsWith("1. Разработка мобильных приложений.") |
                    update.getMessage().getText().startsWith("1.Разработка мобильных приложений.") |
                    update.getMessage().getText().startsWith("Разработка мобильных приложений.") |
                    update.getMessage().getText().startsWith("разработка мобильных приложений.")) {
                if (!send.getAllProfiles().isEmpty()) {
                    for (int i = 0; i < send.getAllProfiles().size(); i++) {
                        if (update.getMessage().getFrom().getId().toString().equals(send.getAllProfiles().get(i).getIdSendWork())) {

                            //системное
                            if (update.getMessage().getText().startsWith("1. Системное программирование") |
                                    update.getMessage().getText().startsWith("1.Системное программирование") |
                                    update.getMessage().getText().startsWith("Системное программирование") |
                                    update.getMessage().getText().startsWith("системное программирование") |
                                    update.getMessage().getText().startsWith("1. Системное программирование.") |
                                    update.getMessage().getText().startsWith("1.Системное программирование.") |
                                    update.getMessage().getText().startsWith("Системное программирование.") |
                                    update.getMessage().getText().startsWith("системное программирование.")) {
                                if (!system.getAllProfiles().isEmpty()) {
                                    for (SystemID s : system.getAllProfiles()) {
                                        if (update.getMessage().getFrom().getUserName() == null) {
                                            resultWork = "У Вашего аккаунта отсутствует имя пользователя.";
                                        } else {
                                            //Разослать работу всем тем, кто подписан на определенную сферу
                                            SendMessage mes = new SendMessage();
                                            mes.setText("Работа: \n" + update.getMessage().getText());
                                            mes.setChatId(s.getIdSystem());
                                            mes.setReplyMarkup(setButtons("response" + update.getMessage().getFrom().getId()));
                                            WorkDeleteId id = new WorkDeleteId();
                                            id.setIdDeleteWork(update.getMessage().getFrom().getId() + "_" + execute(mes).getMessageId() + "\n" +
                                                    update.getMessage().getText());
                                            delete.saveUsers(id);
                                            resultWork = "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов.";
                                            send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                        }
                                    }
                                } else {
                                    replyMsg.setText("На площадке нет занимающихся системным программированием.");
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            } else if (update.getMessage().getText().startsWith("1. Прикладное программирование") |
                                    update.getMessage().getText().startsWith("1.Прикладное программирование") |
                                    update.getMessage().getText().startsWith("Прикладное программирование") |
                                    update.getMessage().getText().startsWith("прикладное программирование") |
                                    update.getMessage().getText().startsWith("1. Прикладное программирование.") |
                                    update.getMessage().getText().startsWith("1.Прикладное программирование.") |
                                    update.getMessage().getText().startsWith("Прикладное программирование.") |
                                    update.getMessage().getText().startsWith("прикладное программирование.")) {
                                if (!priklad.getAllProfiles().isEmpty()) {
                                    for (PrikladID s : priklad.getAllProfiles()) {
                                        if (update.getMessage().getFrom().getUserName() == null) {
                                            resultWork = "У Вас не определено имя пользователя.";
                                        } else {
                                            //Разослать работу всем тем, кто подписан на определенную сферу
                                            SendMessage mes = new SendMessage();
                                            mes.setText("Работа: \n" + update.getMessage().getText());
                                            mes.setChatId(s.getIdPriklad());
                                            mes.setReplyMarkup(setButtons("response" + update.getMessage().getFrom().getId()));
                                            WorkDeleteId id = new WorkDeleteId();
                                            id.setIdDeleteWork(update.getMessage().getFrom().getId() + "_" + execute(mes).getMessageId() + "\n" +
                                                    update.getMessage().getText());
                                            delete.saveUsers(id);
                                            resultWork = "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов.";
                                            send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                        }
                                    }
                                } else {
                                    replyMsg.setText("На площадке нет занимающихся прикладным программированием.");
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            } else if (update.getMessage().getText().startsWith("1. Веб-программирование") |
                                    update.getMessage().getText().startsWith("1.Веб-программирование") |
                                    update.getMessage().getText().startsWith("Веб-программирование") |
                                    update.getMessage().getText().startsWith("веб-программирование") |
                                    update.getMessage().getText().startsWith("1. Веб-программирование.") |
                                    update.getMessage().getText().startsWith("1.Веб-программирование.") |
                                    update.getMessage().getText().startsWith("Веб-программирование.") |
                                    update.getMessage().getText().startsWith("веб-программирование.")) {
                                if (!web.getAllProfiles().isEmpty()) {
                                    for (WebID s : web.getAllProfiles()) {
                                        if (update.getMessage().getFrom().getUserName() == null) {
                                            resultWork = "У Вас не определено имя пользователя.";
                                        } else {
                                            //Разослать работу всем тем, кто подписан на определенную сферу
                                            SendMessage mes = new SendMessage();
                                            mes.setText("Работа: \n" + update.getMessage().getText());
                                            mes.setChatId(s.getIdWeb());
                                            mes.setReplyMarkup(setButtons("response" + update.getMessage().getFrom().getId()));
                                            WorkDeleteId id = new WorkDeleteId();
                                            id.setIdDeleteWork(update.getMessage().getFrom().getId() + "_" + execute(mes).getMessageId() + "\n" +
                                                    update.getMessage().getText());
                                            delete.saveUsers(id);
                                            resultWork = "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов.";
                                            send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                        }
                                    }
                                } else {
                                    replyMsg.setText("На площадке нет занимающихся веб-программированием.");
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            } else if (update.getMessage().getText().startsWith("1. Разработка игр") |
                                    update.getMessage().getText().startsWith("1.Разработка игр") |
                                    update.getMessage().getText().startsWith("Разработка игр") |
                                    update.getMessage().getText().startsWith("разработка игр") |
                                    update.getMessage().getText().startsWith("1. Разработка игр.") |
                                    update.getMessage().getText().startsWith("1.Разработка игр.") |
                                    update.getMessage().getText().startsWith("Разработка игр.") |
                                    update.getMessage().getText().startsWith("разработка игр.")) {
                                if (!game.getAllProfiles().isEmpty()) {
                                    for (GameID s : game.getAllProfiles()) {
                                        if (update.getMessage().getFrom().getUserName() == null) {
                                            resultWork = "У Вас не определено имя пользователя.";
                                        } else {
                                            //Разослать работу всем тем, кто подписан на определенную сферу
                                            SendMessage mes = new SendMessage();
                                            mes.setText("Работа: \n" + update.getMessage().getText());
                                            mes.setChatId(s.getIdGame());
                                            mes.setReplyMarkup(setButtons("response" + update.getMessage().getFrom().getId()));
                                            WorkDeleteId id = new WorkDeleteId();
                                            id.setIdDeleteWork(update.getMessage().getFrom().getId() + "_" + execute(mes).getMessageId() + "\n" +
                                                    update.getMessage().getText());
                                            delete.saveUsers(id);
                                            resultWork = "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов.";
                                            send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                        }
                                    }
                                } else {
                                    replyMsg.setText("На площадке нет занимающихся разработкой игр.");
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            } else if (update.getMessage().getText().startsWith("1. Разработка мобильных приложений") |
                                    update.getMessage().getText().startsWith("1.Разработка мобильных приложений") |
                                    update.getMessage().getText().startsWith("Разработка мобильных приложений") |
                                    update.getMessage().getText().startsWith("разработка мобильных приложений") |
                                    update.getMessage().getText().startsWith("1. Разработка мобильных приложений.") |
                                    update.getMessage().getText().startsWith("1.Разработка мобильных приложений.") |
                                    update.getMessage().getText().startsWith("Разработка мобильных приложений.") |
                                    update.getMessage().getText().startsWith("разработка мобильных приложений.")) {
                                if (!phone.getAllProfiles().isEmpty()) {
                                    for (PhoneID s : phone.getAllProfiles()) {
                                        if (update.getMessage().getFrom().getUserName() == null) {
                                            resultWork = "У Вас не определено имя пользователя.";
                                        } else {
                                            //Разослать работу всем тем, кто подписан на определенную сферу
                                            SendMessage mes = new SendMessage();
                                            mes.setText("Работа: \n" + update.getMessage().getText());
                                            mes.setChatId(s.getIdPhone());
                                            mes.setReplyMarkup(setButtons("response" + update.getMessage().getFrom().getId()));
                                            WorkDeleteId id = new WorkDeleteId();
                                            id.setIdDeleteWork(update.getMessage().getFrom().getId() + "_" + execute(mes).getMessageId() + "\n" +
                                                    update.getMessage().getText());
                                            delete.saveUsers(id);
                                            resultWork = "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов.";
                                            send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                        }
                                    }
                                } else {
                                    replyMsg.setText("На площадке нет занимающихся разработкой мобильных приложений.");
                                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    send.deleteUsers(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            } else {
                                replyMsg.setText("Некорректно написано задание.");
                                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                return replyMsg;
                            }
                        }
                    }
                }
            }

            if(resultWork == null){
                resultWork = "Выберите действие.";
            }else if(resultWork == "Ваша работа опубликована. Ожидайте откликов исполнителей. \nРаботу можно удалить, если она была опубликована не более 48 часов."){
                replyMsg.setText(resultWork);
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                return replyMsg;
            }
            else{
                resultWork = resultWork + "\nВыберите действие.";
            }
            replyMsg.setText(resultWork);
            replyMsg.setReplyMarkup(kb.getIkmGetServiceStart());
            replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("startCustomer")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как работодатель.\nВыберите действие.");
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как работодатель.\nВыберите действие.");
            replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Кнопка опубликовать работу
        if(update.getCallbackQuery().getData().equals("workCustomer")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                String workText = """
                        Шаблон:

                        1. Укажите один из разделов:\s
                        Системное программирование,\s
                        Прикладное программирование,\s
                        Веб-программирование,\s
                        Разработка игр,\s
                        Разработка мобильных приложений.
                        2. Укажите тему.
                        3. Укажите предпочитаемый язык программирования.
                        4. Дополнительная информация.
                        5. Укажите бюджет.
                        6. Укажите срок.

                        Будьте внимательны:
                        1. Задание нужно писать после этого сообщения.
                        2. Раздел должен быть указан один и с соблюдением орфографии, верхнего и нижнего регистра.
                            \n
                            Нажмите назад, чтобы отменить действие.
                        """;
                if(!send.getAllProfiles().isEmpty()) {
                    for (WorkSendId s : send.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSendWork())) {
                            replyMsg.setText("");
                            break;
                        }else{
                            replyMsg.setText(workText);
                        }
                    }
                    if(!replyMsg.getText().isEmpty()) {
                        WorkSendId work = new WorkSendId();
                        work.setIdSendWork(update.getCallbackQuery().getFrom().getId().toString());
                        send.saveUsers(work);
                    }
                }else{
                    WorkSendId work = new WorkSendId();
                    work.setIdSendWork(update.getCallbackQuery().getFrom().getId().toString());
                    send.saveUsers(work);
                }
                replyMsg.setText(workText);
                replyMsg.setReplyMarkup(kb.getIkmGetMessage());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            String workText = """
                    Шаблон:

                    1. Укажите один из разделов:\s
                    Системное программирование,\s
                    Прикладное программирование,\s
                    Веб-программирование,\s
                    Разработка игр,\s
                    Разработка мобильных приложений.
                    2. Укажите тему.
                    3. Укажите предпочитаемый язык программирования.
                    4. Дополнительная информация.
                    5. Укажите бюджет.
                    6. Укажите срок.

                    Будьте внимательны:
                    1. Задание нужно писать после этого сообщения.
                    2. Раздел должен быть указан один и с соблюдением орфографии, верхнего и нижнего регистра.
                    \n
                    Нажмите назад, чтобы отменить действие.
                    """;
            if(!send.getAllProfiles().isEmpty()) {
                for (WorkSendId s : send.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSendWork())) {
                        replyMsg.setText("");
                        break;
                    }else{
                        replyMsg.setText(workText);
                    }
                }
                if(!replyMsg.getText().isEmpty()) {
                    WorkSendId work = new WorkSendId();
                    work.setIdSendWork(update.getCallbackQuery().getFrom().getId().toString());
                    send.saveUsers(work);
                }
            }else{
                WorkSendId work = new WorkSendId();
                work.setIdSendWork(update.getCallbackQuery().getFrom().getId().toString());
                send.saveUsers(work);
            }
            replyMsg.setText(workText);
            replyMsg.setReplyMarkup(kb.getIkmGetMessage());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("MessageBack")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как работодатель.");
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как работодатель.");
            replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Кнопка работодателя список работ
        if(update.getCallbackQuery().getData().equals("my_worksCustomer")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(delete.getAllProfiles().isEmpty()){
                    replyMsg.setText("У Вас нет опубликованных работ.");
                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                    replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                    return replyMsg;
                }else {
                    for (WorkDeleteId s : delete.getAllProfiles()) {
                        if (s.getIdDeleteWork().startsWith(update.getCallbackQuery().getFrom().getId() + "_")) {
                            replyMsg.setText("Идентификатор: " + s.getIdDeleteWork().replace(update.getCallbackQuery().getFrom().getId().toString() + "_", ""));
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            execute(replyMsg);
                        }
                    }
                    replyMsg.setText("Выберите действие.");
                    replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                    return replyMsg;
                }
            }
            if(delete.getAllProfiles().isEmpty()){
                replyMsg.setText("У Вас нет опубликованных работ.");
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }else {
                for (WorkDeleteId s : delete.getAllProfiles()) {
                    if (s.getIdDeleteWork().startsWith(update.getCallbackQuery().getFrom().getId() + "_")) {
                        replyMsg.setText("Идентификатор: " + s.getIdDeleteWork().replace(update.getCallbackQuery().getFrom().getId().toString() + "_", ""));
                        replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                        execute(replyMsg);
                    }
                }
                replyMsg.setText("Выберите действие.");
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                return replyMsg;
            }
        }

        if(update.getCallbackQuery().getData().equals("deletework")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Чтобы удалить работу, введите команду \"/delete\", а затем идентификатор работы, которую хотите удалить." +
                        "\nБудьте внимательны: между командой \"/delete\" и идентификатором не должно быть пробелов.");
                replyMsg.setReplyMarkup(kb.getIkmGetDeleteBack());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Чтобы удалить работу, введите команду \"/delete\", а затем идентификатор работы, которую хотите удалить." +
                    "\nБудьте внимательны: между командой \"/delete\" и идентификатором не должно быть пробелов.");
            replyMsg.setReplyMarkup(kb.getIkmGetDeleteBack());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("deleteBack")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как работодатель.");
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как работодатель.");
            replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        /*
        if(update.getCallbackQuery().getData().equals("rating")){

        }

        if(update.getCallbackQuery().getData().equals("leave")){
            replyMsg.setText("Выберите оценку.");
            replyMsg.setReplyMarkup(kb.getIkmGetGrade());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if()

        if(update.getCallbackQuery().getData().equals("back")){
            replyMsg.setText("Выберите действие.");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Кнопка работодателя список разработчиков
        if(update.getCallbackQuery().getData().equals("topCustomer")){
            replyMsg.setText("Вы желаете посмотреть отзывы разработчиков или же оставить свой?");
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            replyMsg.setReplyMarkup(kb.getIkmGetTop());
            return replyMsg;
        }
        */

        //Кнопка работодателя войти как исполнитель
        if(update.getCallbackQuery().getData().equals("executorCustomer")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как исполнитель.");
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как исполнитель.");
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
            return replyMsg;
        }

        //Кнопка исполнителя войти как работодатель
        if(update.getCallbackQuery().getData().equals("customerExecutor")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как работодатель.");
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как работодатель.");
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("startExecutor")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как исполнитель.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как исполнитель.");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Раздел
        if(update.getCallbackQuery().getData().equals("chapter")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы хотите добавить раздел или удалить?");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы хотите добавить раздел или удалить?");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("backChapter")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы вошли как исполнитель.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы вошли как исполнитель.");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("backIKM")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Вы хотите добавить раздел или удалить?");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Вы хотите добавить раздел или удалить?");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Добавить
        if(update.getCallbackQuery().getData().equals("add")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Выберите раздел, который хотите добавить.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAdd());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Выберите раздел, который хотите добавить.");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAdd());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Добавить системное программирование
        if(update.getCallbackQuery().getData().equals("systemadd")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if (!system.getAllProfiles().isEmpty()) {
                    for (SystemID s : system.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSystem())) {
                            replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Системное программирование\".");
                            break;
                        }
                    }
                    if (replyMsg.getText().isEmpty()) {
                        replyMsg.setText("Раздел \"Системное программирование\" добавлен.");
                        SystemID sys = new SystemID();
                        sys.setIdSystem(update.getCallbackQuery().getFrom().getId().toString());
                        system.saveUsers(sys);
                    }
                }else{
                    replyMsg.setText("Раздел \"Системное программирование\" добавлен.");
                    SystemID sys = new SystemID();
                    sys.setIdSystem(update.getCallbackQuery().getFrom().getId().toString());
                    system.saveUsers(sys);
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }

            if (!system.getAllProfiles().isEmpty()) {
                for (SystemID s : system.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSystem())) {
                        replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Системное программирование\".");
                        break;
                    }
                }
                if (replyMsg.getText().isEmpty()) {
                    replyMsg.setText("Раздел \"Системное программирование\" добавлен.");
                    SystemID sys = new SystemID();
                    sys.setIdSystem(update.getCallbackQuery().getFrom().getId().toString());
                    system.saveUsers(sys);
                }
            }else{
                replyMsg.setText("Раздел \"Системное программирование\" добавлен.");
                SystemID sys = new SystemID();
                sys.setIdSystem(update.getCallbackQuery().getFrom().getId().toString());
                system.saveUsers(sys);
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Добавить прикладное программирование
        if(update.getCallbackQuery().getData().equals("prikladadd")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if (!priklad.getAllProfiles().isEmpty()) {
                    for (PrikladID s : priklad.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPriklad())) {
                            replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Прикладное программирование\".");
                            break;
                        }
                    }
                    if (replyMsg.getText().isEmpty()) {
                        replyMsg.setText("Раздел \"Прикладное программирование\" добавлен.");
                        PrikladID pri = new PrikladID();
                        pri.setIdPriklad(update.getCallbackQuery().getFrom().getId().toString());
                        priklad.saveUsers(pri);
                    }
                }else{
                    replyMsg.setText("Раздел \"Прикладное программирование\" добавлен.");
                    PrikladID pri = new PrikladID();
                    pri.setIdPriklad(update.getCallbackQuery().getFrom().getId().toString());
                    priklad.saveUsers(pri);
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }

            if (!priklad.getAllProfiles().isEmpty()) {
                for (PrikladID s : priklad.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPriklad())) {
                        replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Прикладное программирование\".");
                        break;
                    }
                }
                if (replyMsg.getText().isEmpty()) {
                    replyMsg.setText("Раздел \"Прикладное программирование\" добавлен.");
                    PrikladID pri = new PrikladID();
                    pri.setIdPriklad(update.getCallbackQuery().getFrom().getId().toString());
                    priklad.saveUsers(pri);
                }
            }else{
                replyMsg.setText("Раздел \"Прикладное программирование\" добавлен.");
                PrikladID pri = new PrikladID();
                pri.setIdPriklad(update.getCallbackQuery().getFrom().getId().toString());
                priklad.saveUsers(pri);
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Добавить веб программирование
        if(update.getCallbackQuery().getData().equals("webadd")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if (!web.getAllProfiles().isEmpty()) {
                    for (WebID s : web.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdWeb())) {
                            replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Веб-программирование\".");
                            break;
                        }
                    }
                    if (replyMsg.getText().isEmpty()) {
                        WebID we = new WebID();
                        we.setIdWeb(update.getCallbackQuery().getFrom().getId().toString());
                        web.saveUsers(we);
                        replyMsg.setText("Раздел \"Веб-программирование\" добавлен.");
                    }
                }else{
                    WebID we = new WebID();
                    we.setIdWeb(update.getCallbackQuery().getFrom().getId().toString());
                    web.saveUsers(we);
                    replyMsg.setText("Раздел \"Веб-программирование\" добавлен.");
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }

            if (!web.getAllProfiles().isEmpty()) {
                for (WebID s : web.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdWeb())) {
                        replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Веб-программирование\".");
                        break;
                    }
                }
                if (replyMsg.getText().isEmpty()) {
                    WebID we = new WebID();
                    we.setIdWeb(update.getCallbackQuery().getFrom().getId().toString());
                    web.saveUsers(we);
                    replyMsg.setText("Раздел \"Веб-программирование\" добавлен.");
                }
            }else{
                WebID we = new WebID();
                we.setIdWeb(update.getCallbackQuery().getFrom().getId().toString());
                web.saveUsers(we);
                replyMsg.setText("Раздел \"Веб-программирование\" добавлен.");
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Добавить разработку игр
        if(update.getCallbackQuery().getData().equals("gameadd")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if (!game.getAllProfiles().isEmpty()) {
                    for (GameID s : game.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdGame())) {
                            replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Разработка игр\".");
                            break;
                        }
                    }
                    if (replyMsg.getText().isEmpty()) {
                        GameID ga = new GameID();
                        ga.setIdGame(update.getCallbackQuery().getFrom().getId().toString());
                        game.saveUsers(ga);
                        replyMsg.setText("Раздел \"Разработка игр\" добавлен.");
                    }
                }else{
                    GameID ga = new GameID();
                    ga.setIdGame(update.getCallbackQuery().getFrom().getId().toString());
                    game.saveUsers(ga);
                    replyMsg.setText("Раздел \"Разработка игр\" добавлен.");
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }

            if (!game.getAllProfiles().isEmpty()) {
                for (GameID s : game.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdGame())) {
                        replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Разработка игр\".");
                        break;
                    }
                }
                if (replyMsg.getText().isEmpty()) {
                    GameID ga = new GameID();
                    ga.setIdGame(update.getCallbackQuery().getFrom().getId().toString());
                    game.saveUsers(ga);
                    replyMsg.setText("Раздел \"Разработка игр\" добавлен.");
                }
            }else{
                GameID ga = new GameID();
                ga.setIdGame(update.getCallbackQuery().getFrom().getId().toString());
                game.saveUsers(ga);
                replyMsg.setText("Раздел \"Разработка игр\" добавлен.");
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Добавить разработку мобильных приложений
        if(update.getCallbackQuery().getData().equals("phoneadd")) {
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if (!phone.getAllProfiles().isEmpty()) {
                    for (PhoneID s : phone.getAllProfiles()) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPhone())) {
                            replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Разработка мобильных приложений\".");
                            break;
                        }
                    }
                    if (replyMsg.getText().isEmpty()) {
                        PhoneID ph = new PhoneID();
                        ph.setIdPhone(update.getCallbackQuery().getFrom().getId().toString());
                        phone.saveUsers(ph);
                        replyMsg.setText("Раздел \"Разработка мобильных приложений\" добавлен.");
                    }
                }else{
                    PhoneID ph = new PhoneID();
                    ph.setIdPhone(update.getCallbackQuery().getFrom().getId().toString());
                    phone.saveUsers(ph);
                    replyMsg.setText("Раздел \"Разработка мобильных приложений\" добавлен.");
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            if (!phone.getAllProfiles().isEmpty()) {
                for (PhoneID s : phone.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPhone())) {
                        replyMsg.setText("Вы уже добавили в свой фильтр раздел \"Разработка мобильных приложений\".");
                        break;
                    }
                }
                if (replyMsg.getText().isEmpty()) {
                    PhoneID ph = new PhoneID();
                    ph.setIdPhone(update.getCallbackQuery().getFrom().getId().toString());
                    phone.saveUsers(ph);
                    replyMsg.setText("Раздел \"Разработка мобильных приложений\" добавлен.");
                }
            }else{
                PhoneID ph = new PhoneID();
                ph.setIdPhone(update.getCallbackQuery().getFrom().getId().toString());
                phone.saveUsers(ph);
                replyMsg.setText("Раздел \"Разработка мобильных приложений\" добавлен.");
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Удалить
        if(update.getCallbackQuery().getData().equals("delete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                replyMsg.setText("Выберите раздел, который хотите удалить.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            replyMsg.setText("Выберите раздел, который хотите удалить.");
            replyMsg.setReplyMarkup(kb.getIkmGetServiceDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Удалить системное программирование
        if(update.getCallbackQuery().getData().equals("systemdelete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(system.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы не добавляли раздел \"Системное программирование\".");
                }else {
                    for (int i = 0; i < system.getAllProfiles().size(); i++) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(system.getAllProfiles().get(i).getIdSystem().toString())) {
                            replyMsg.setText("Раздел \"Системное программирование\" удален.");
                            system.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                            break;
                        }else {
                            replyMsg.setText("Вы уже удалили раздел \"Системное программирование\".");
                        }
                    }
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }

            if(system.getAllProfiles().isEmpty()){
                replyMsg.setText("Вы не добавляли раздел \"Системное программирование\".");
            }else {
                for (int i = 0; i < system.getAllProfiles().size(); i++) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(system.getAllProfiles().get(i).getIdSystem())) {
                        replyMsg.setText("Раздел \"Системное программирование\" удален.");
                        system.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                        break;
                    }else {
                        replyMsg.setText("Вы уже удалили раздел \"Системное программирование\".");
                    }
                }
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Удалить прикладное программирование
        if(update.getCallbackQuery().getData().equals("prikladdelete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(priklad.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы не добавляли раздел \"Прикладное программирование\".");
                }else {
                    for (int i = 0; i < priklad.getAllProfiles().size(); i++) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(priklad.getAllProfiles().get(i).getIdPriklad())) {
                            replyMsg.setText("Раздел \"Прикладное программирование\" удален.");
                            priklad.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                            break;
                        } else {
                            replyMsg.setText("Вы уже удалили раздел \"Прикладное программирование\".");
                        }
                    }
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            if(priklad.getAllProfiles().isEmpty()){
                replyMsg.setText("Вы не добавляли раздел \"Прикладное программирование\".");
            }else {
                for (int i = 0; i < priklad.getAllProfiles().size(); i++) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(priklad.getAllProfiles().get(i).getIdPriklad())) {
                        replyMsg.setText("Раздел \"Прикладное программирование\" удален.");
                        priklad.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                        break;
                    } else {
                        replyMsg.setText("Вы уже удалили раздел \"Прикладное программирование\".");
                    }
                }
            }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
        }
        //Удалить веб программирование
        if(update.getCallbackQuery().getData().equals("webdelete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(web.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы не добавляли раздел \"Веб-программирование\".");
                }else {
                    for (int i = 0; i < web.getAllProfiles().size(); i++) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(web.getAllProfiles().get(i).getIdWeb())) {
                            replyMsg.setText("Раздел \"Веб-программирование\" удален.");
                            web.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                            break;
                        } else {
                            replyMsg.setText("Вы уже удалили раздел \"Веб-программирование\".");
                        }
                    }
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            if(web.getAllProfiles().isEmpty()){
                replyMsg.setText("Вы не добавляли раздел \"Веб-программирование\".");
            }else {
                for (int i = 0; i < web.getAllProfiles().size(); i++) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(web.getAllProfiles().get(i).getIdWeb())) {
                        replyMsg.setText("Раздел \"Веб-программирование\" удален.");
                        web.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                        break;
                    } else {
                        replyMsg.setText("Вы уже удалили раздел \"Веб-программирование\".");
                    }
                }
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Удален разработку игр
        if(update.getCallbackQuery().getData().equals("gamedelete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(game.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы не добавляли раздел \"Разработка игр\".");
                }else {
                    for (int i = 0; i < game.getAllProfiles().size(); i++) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(game.getAllProfiles().get(i).getIdGame())) {
                            replyMsg.setText("Раздел \"Разработка игр\" удален.");
                            game.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                            break;
                        } else {
                            replyMsg.setText("Вы уже удалили раздел \"Разработка игр\".");
                        }
                    }
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            if(game.getAllProfiles().isEmpty()){
                replyMsg.setText("Вы не добавляли раздел \"Разработка игр\".");
            }else {
                for (int i = 0; i < game.getAllProfiles().size(); i++) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(game.getAllProfiles().get(i).getIdGame())) {
                        replyMsg.setText("Раздел \"Разработка игр\" удален.");
                        game.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                        break;
                    } else {
                        replyMsg.setText("Вы уже удалили раздел \"Разработка игр\".");
                    }
                }
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }
        //Удалить разработку мобильных приложений
        if(update.getCallbackQuery().getData().equals("phonedelete")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                if(phone.getAllProfiles().isEmpty()){
                    replyMsg.setText("Вы не добавляли раздел \"Разработка мобильных приложений\".");
                }else {
                    for (int i = 0; i < phone.getAllProfiles().size(); i++) {
                        if (update.getCallbackQuery().getFrom().getId().toString().equals(phone.getAllProfiles().get(i).getIdPhone())) {
                            replyMsg.setText("Раздел \"Разработка мобильных приложений\" удален.");
                            phone.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                            break;
                        } else {
                            replyMsg.setText("Вы уже удалили раздел \"Разработка мобильных приложений\".");
                        }
                    }
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            if(phone.getAllProfiles().isEmpty()){
                replyMsg.setText("Вы не добавляли раздел \"Разработка мобильных приложений\".");
            }else {
                for (int i = 0; i < phone.getAllProfiles().size(); i++) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(phone.getAllProfiles().get(i).getIdPhone())) {
                        replyMsg.setText("Раздел \"Разработка мобильных приложений\" удален.");
                        phone.deleteUsers(update.getCallbackQuery().getFrom().getId().toString());
                        break;
                    } else {
                        replyMsg.setText("Вы уже удалили раздел \"Разработка мобильных приложений\".");
                    }
                }
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //Список моих разделов
        if(update.getCallbackQuery().getData().equals("mylist")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e){
                for (SystemID s : system.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSystem())) {
                        if(replyMsg.getText() == null) {
                            replyMsg.setText("Ваши разделы:\nСистемное программирование.");
                        }else{
                            replyMsg.setText(replyMsg.getText() + "Системное программирование.");
                        }
                        break;
                    }
                }
                for (PrikladID s : priklad.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPriklad())) {
                        if(replyMsg.getText() == null) {
                            replyMsg.setText("Ваши разделы:\nПрикладное программирование.");
                        }else {
                            replyMsg.setText(replyMsg.getText() + "\nПрикладное программирование.");
                        }
                        break;
                    }
                }
                for (WebID s : web.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdWeb())) {
                        if(replyMsg.getText() == null) {
                            replyMsg.setText("Ваши разделы:\nВеб-программирование.");
                        }else {
                            replyMsg.setText(replyMsg.getText() + "\nВеб-программирование.");
                        }
                        break;
                    }
                }
                for (GameID s : game.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdGame())) {
                        if(replyMsg.getText() == null) {
                            replyMsg.setText("Ваши разделы:\nРазработка игр.");
                        }else {
                            replyMsg.setText(replyMsg.getText() + "\nРазработка игр.");
                        }
                        break;
                    }
                }
                for (PhoneID s : phone.getAllProfiles()) {
                    if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPhone())) {
                        if(replyMsg.getText() == null) {
                            replyMsg.setText("Ваши разделы:\nРазработка мобильных приложений.");
                        }else {
                            replyMsg.setText(replyMsg.getText() + "\nРазработка мобильных приложений.");
                        }
                        break;
                    }
                }
                if(replyMsg.getText() == null) {
                    replyMsg.setText("У Вас нет добавленных разделов.\nВыберите действие.");
                }else{
                    replyMsg.setText(replyMsg.getText() + "\nВыберите действие.");
                }
                replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
            for (SystemID s : system.getAllProfiles()) {
                if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdSystem())) {
                    if(replyMsg.getText() == null) {
                        replyMsg.setText("Ваши разделы:\nСистемное программирование.");
                    }else{
                        replyMsg.setText(replyMsg.getText() + "Системное программирование.");
                    }
                    break;
                }
            }
            for (PrikladID s : priklad.getAllProfiles()) {
                if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPriklad())) {
                    if(replyMsg.getText() == null) {
                        replyMsg.setText("Ваши разделы:\nПрикладное программирование.");
                    }else {
                        replyMsg.setText(replyMsg.getText() + "\nПрикладное программирование.");
                    }
                    break;
                }
            }
            for (WebID s : web.getAllProfiles()) {
                if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdWeb())) {
                    if(replyMsg.getText() == null) {
                        replyMsg.setText("Ваши разделы:\nВеб-программирование.");
                    }else {
                        replyMsg.setText(replyMsg.getText() + "\nВеб-программирование.");
                    }
                    break;
                }
            }
            for (GameID s : game.getAllProfiles()) {
                if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdGame())) {
                    if(replyMsg.getText() == null) {
                        replyMsg.setText("Ваши разделы:\nРазработка игр.");
                    }else {
                        replyMsg.setText(replyMsg.getText() + "\nРазработка игр.");
                    }
                    break;
                }
            }
            for (PhoneID s : phone.getAllProfiles()) {
                if (update.getCallbackQuery().getFrom().getId().toString().equals(s.getIdPhone())) {
                    if(replyMsg.getText() == null) {
                        replyMsg.setText("Ваши разделы:\nРазработка мобильных приложений.");
                    }else {
                        replyMsg.setText(replyMsg.getText() + "\nРазработка мобильных приложений.");
                    }
                    break;
                }
            }
            if(replyMsg.getText() == null) {
                replyMsg.setText("У Вас нет добавленных разделов.\nВыберите действие.");
            }else{
                replyMsg.setText(replyMsg.getText() + "\nВыберите действие.");
            }
            replyMsg.setReplyMarkup(kb.getIkmGetServiceAddAndDelete());
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
            }


        if(update.getCallbackQuery().getData().startsWith("response")){

            if(!response.getAllProfiles().isEmpty()) {
                for(WorkResponseId s: response.getAllProfiles()) {
                    if (s.getIdResponseWork().startsWith(update.getCallbackQuery().getFrom().getId() + "\n")) {
                        replyMsg.setText("Вы уже откликнулись.");
                        replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                        replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                        return replyMsg;
                    }
                }
                WorkResponseId work = new WorkResponseId();
                work.setIdResponseWork(update.getCallbackQuery().getFrom().getId() + "\n" +
                        update.getCallbackQuery().getData().replace("response", ""));
                response.saveUsers(work);
                replyMsg.setText("""
                        1. Отклик нужно писать после этого сообщения.
                        2. Между командой "/response" и первым символом сообщения не должно быть пробелов.
                        3. У Вашего аккаунта обязательно должно быть имя пользователя, чтобы работодатель смог с  Вами связаться.""");
                replyMsg.setReplyMarkup(kb.getIkmGetresponse());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }else{
                WorkResponseId work = new WorkResponseId();
                work.setIdResponseWork(update.getCallbackQuery().getFrom().getId() + "\n" +
                        update.getCallbackQuery().getData().replace("response", ""));
                response.saveUsers(work);
                replyMsg.setText("""
                        Введите "/response", затем напишите сообщение, чтобы бот отправил его автору работы.
                        Будьте внимательны:\s
                        1. Отклик нужно писать после этого сообщения.
                        2. Между командой "/response" и первым символом сообщения не должно быть пробелов.
                        3. У Вашего аккаунта обязательно должно быть имя пользователя, чтобы работодатель смог с  Вами связаться.""");
                replyMsg.setReplyMarkup(kb.getIkmGetresponse());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
        }

        if(update.getCallbackQuery().getData().equals("BackResponse")){
            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                execute(deleteMessage);
            }catch(TelegramApiException e) {
                if (!response.getAllProfiles().isEmpty()) {
                    for (int i = 0; i < response.getAllProfiles().size(); i++) {
                        if (response.getAllProfiles().get(i).getIdResponseWork().startsWith(update.getCallbackQuery().getFrom().getId() + "\n")) {
                            response.deleteUsers(response.getAllProfiles().get(i).getIdResponseWork());
                            replyMsg.setText("Вы отменили отправку.");
                            replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }
                    }
                } else {
                    replyMsg.setText("Вы не откликались.");
                    replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                    replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                    return replyMsg;
                }
            }
            if (!response.getAllProfiles().isEmpty()) {
                for (int i = 0; i < response.getAllProfiles().size(); i++) {
                    if (response.getAllProfiles().get(i).getIdResponseWork().startsWith(update.getCallbackQuery().getFrom().getId() + "\n")) {
                        response.deleteUsers(response.getAllProfiles().get(i).getIdResponseWork());
                        replyMsg.setText("Вы отменили отправку.");
                        replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                        replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                        return replyMsg;
                    }
                }
            } else {
                replyMsg.setText("Вы не откликались.");
                replyMsg.setReplyMarkup(kb.getIkmGetServiceConvert());
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
        }

        replyMsg.setReplyMarkup(kb.getIkmGetCustomer());
        return replyMsg;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    public InlineKeyboardMarkup setButtons(String string){
        InlineKeyboardMarkup inline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lst2 = new ArrayList<>();
        List<InlineKeyboardButton> lst1 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТКЛИКНУТЬСЯ");
        button2.setCallbackData(string);
        lst1.add(button2);
        lst2.add(lst1);
        inline.setKeyboard(lst2);
        return inline;
    }
}
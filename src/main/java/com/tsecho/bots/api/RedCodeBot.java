package com.tsecho.bots.api;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@ConfigurationProperties("telegram.bot")
public class RedCodeBot extends TelegramWebhookBot {

    String token;

    String name;

    String webhook;

    String welcome = "Добрый день!\nВы можете осуществить регистрацию нажав на соответствующую кнопку ниже.";
    String aboutUs = "ООО «Ред Код». Компания основана в августе 2021 года. Занимается разработкой программного обеспечения.\nПартнеры:\n1. Битрикс24\n2. 1С\n3. TAXCOM";
    String present = "Укажите Ваши имя и фамилию.";
    String result = "Вы зарегистрировались как слушатель. Чтобы зарегистрироваться как спикер или участник, нажмите на кнопку «Перейти.";
    String sendPhoneNumber = "Отправьте номер телефона, нажав на соответствующую кнопку ниже.";

    List<String> idUser = new ArrayList<>();
    List<String> nameUser = new ArrayList<>();

    @Autowired
    Keyboard kb;

    Integer lastUpdateId = 0;

    Keyboard k = new Keyboard();

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        System.out.println(idUser);
        System.out.println(nameUser);

        SendMessage replyMsg = new SendMessage();

        replyMsg.enableWebPagePreview();

        if (update.getUpdateId() <= lastUpdateId) return replyMsg;

        this.lastUpdateId = update.getUpdateId();

        //Обработка Текста
        if(update.hasMessage()) {
            if (update.getMessage().hasContact()) {//Тут нужно перехватить номер телефона
                if(!idUser.isEmpty()) {
                    for (int i = 0; i < idUser.size(); i++) {
                        if (idUser.get(i).equals(update.getMessage().getFrom().getId().toString() + "_")) {
                            break;
                        }
                        if (i == idUser.size() - 1) {
                            for(int k = 0; k < nameUser.size(); k++){
                                if(nameUser.get(k).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                    if(nameUser.get(k).split("_").length == 1) {
                                        if(!idUser.isEmpty()){
                                            int j = 0;
                                            for(int s = 0; s < idUser.size(); s++){
                                                if(idUser.get(s).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                                    j = 1;
                                                    break;
                                                }
                                            }
                                            if(j == 0){
                                                idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                            }
                                        }else{
                                            idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                        }
                                        replyMsg.setText("Отправьте имя и фамилию.");
                                        replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    }else if(nameUser.get(k).split("_").length == 2) {
                                        replyMsg.setText(sendPhoneNumber);
                                        replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    }else if(nameUser.get(k).split("_").length == 3){
                                        replyMsg.setText("Ваши имя и фамилия: " + nameUser.get(k).split("_")[1] + "\nВаш номер телефона: " + nameUser.get(k).split("_")[2] + "\n" +result);
                                        replyMsg.setReplyMarkup(kb.ikmOpen);
                                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                        return replyMsg;
                                    }
                                }
                            }
                        }
                    }
                }else{
                    if(nameUser.isEmpty()){
                        idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                        replyMsg.setText(present);
                        replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                        replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                        return replyMsg;
                    }else {
                        for(int i = 0; i < nameUser.size(); i++){
                            if(nameUser.get(i).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                if(nameUser.get(i).split("_").length == 1) {
                                    if(!idUser.isEmpty()){
                                        int f = 0;
                                        for(int s = 0; s < idUser.size(); s++){
                                            if(idUser.get(s).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                                f = 1;
                                                break;
                                            }
                                        }
                                        if(f == 0){
                                            idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                        }
                                    }else{
                                        idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                    }
                                    replyMsg.setText("Отправьте имя и фамилию.");
                                    replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }else if(nameUser.get(i).split("_").length == 2) {
                                    replyMsg.setText(sendPhoneNumber);
                                    replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }else if(nameUser.get(i).split("_").length == 3){
                                    replyMsg.setText("Ваши имя и фамилия: " + nameUser.get(i).split("_")[1] + "\nВаш номер телефона: " + nameUser.get(i).split("_")[2] + "\n" +result);
                                    replyMsg.setReplyMarkup(kb.ikmOpen);
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                            }
                        }
                    }
                }
                if(!nameUser.isEmpty()){
                    int j = 0;
                    for (int i = 0; i < nameUser.size(); i++) {
                        if(nameUser.get(i).split("_")[0].equals(update.getMessage().getFrom().getId().toString())){
                            if(nameUser.get(i).split("_")[1] == null){
                                //Случай если нажал на кнопку отправить номер, не отправив имя и фамилию
                                if(!idUser.isEmpty()){
                                    int f = 0;
                                    for(int s = 0; s < idUser.size(); s++){
                                        if(idUser.get(s).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                            f = 1;
                                            break;
                                        }
                                    }
                                    if(f == 0){
                                        idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                    }
                                }else{
                                    idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                                }
                                replyMsg.setText("Вы не ввели имя и фамилию." + "\n" + present);
                                replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                            }
                            else{
                                j = 1;
                                if(nameUser.get(i).split("_").length == 3){
                                    deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                                    replyMsg.setText("Ваши имя и фамилия: " + nameUser.get(i).split("_")[1] + "\nВаш номер телефона: " + nameUser.get(i).split("_")[2] + "\n" + result);
                                    replyMsg.setReplyMarkup(kb.ikmOpen);
                                    replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                    return replyMsg;
                                }
                                String s = nameUser.get(i) + update.getMessage().getContact().getPhoneNumber();
                                nameUser.set(i, s);
                                replyMsg.setText("Ваши имя и фамилия: " + nameUser.get(i).split("_")[1] + "\nВаш номер телефона: " + nameUser.get(i).split("_")[2] + "\n" + result);
                                replyMsg.setReplyMarkup(kb.ikmOpen);
                            }
                        }
                    }
                    if(j == 0){
                        if(!idUser.isEmpty()){
                            int f = 0;
                            for(int i = 0; i < idUser.size(); i++){
                                if(idUser.get(i).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                    f = 1;
                                    break;
                                }
                            }
                            if(f == 0){
                                idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                            }
                        }else{
                            idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                        }
                        replyMsg.setText("Вы не ввели имя и фамилию." + "\n" + present);
                        replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                    }
                }else{
                    if(!idUser.isEmpty()){
                        int j = 0;
                        for(int i = 0; i < idUser.size(); i++){
                            if(idUser.get(i).equals(update.getMessage().getFrom().getId().toString() + "_")){
                                j = 1;
                                break;
                            }
                        }
                        if(j == 0){
                            idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                        }
                    }else{
                        idUser.add(update.getMessage().getFrom().getId().toString() + "_");
                    }
                    replyMsg.setText("Вы не ввели имя и фамилию." + "\n" + present);
                    replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                }
                deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                return replyMsg;
            } else {//Перехватываем имя и фамилию
                if(!idUser.isEmpty()){//Если пользователь нажал кнопку регистрация
                    for(int i = 0; i < idUser.size(); i++){//проверяем среди всех пользователей нажавших кнопку регистрация
                        if(idUser.get(i).startsWith(update.getMessage().getFrom().getId().toString() + "_")){
                            if(!nameUser.isEmpty()){
                                for(int k = 0; k < nameUser.size(); k++) {
                                    if(nameUser.get(k).startsWith(update.getMessage().getFrom().getId().toString() + "_")) {
                                        if (nameUser.get(k).split("_").length == 3) {
                                            deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                                            replyMsg.setText(result);
                                            replyMsg.setReplyMarkup(kb.ikmOpen);
                                            replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                            return replyMsg;
                                        }
                                        if (nameUser.get(k).split("_").length == 2) {
                                            deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                                            replyMsg.setText(sendPhoneNumber);
                                            replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                                            replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                                            return replyMsg;
                                        }
                                    }
                                }
                            }
                            deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                            replyMsg.setText(sendPhoneNumber);
                            nameUser.add(update.getMessage().getFrom().getId() + "_" + update.getMessage().getText() + "_");
                            replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                            replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                            idUser.remove(i);
                            return replyMsg;
                        }
                    }
                }
                //ПЕРВОЕ СООБЩЕНИЕ
                deleteMessage(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getMessageId());
                replyMsg.setText(welcome);
                replyMsg.setReplyMarkup(kb.ikmMain);
                replyMsg.setChatId(update.getMessage().getFrom().getId().toString());
                //1330710829
                return replyMsg;
            }
        }

        if(update.getCallbackQuery().getData().equals("open")){

            if(!nameUser.isEmpty()){
                for(int i = 0; i < nameUser.size(); i++){
                    if(nameUser.get(i).startsWith(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                        if(nameUser.get(i).split("_").length == 1) {
                            if(!idUser.isEmpty()){
                                int j = 0;
                                for(int s = 0; s < idUser.size(); s++){
                                    if(idUser.get(s).equals(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                                        j = 1;
                                        break;
                                    }
                                }
                                if(j == 0){
                                    idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                                }
                            }else{
                                idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                            }
                            replyMsg.setText("Отправьте имя и фамилию.");
                            replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }else if(nameUser.get(i).split("_").length == 2) {
                            replyMsg.setText(sendPhoneNumber);
                            replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }else if(nameUser.get(i).split("_").length == 3){
                            replyMsg.setText("Тут будет переход на сайт\nhttps://что-тотам.ru/" + update.getCallbackQuery().getFrom().getId().toString());
                            replyMsg.setReplyMarkup(kb.ikmBack);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }
                    }
                }
            }else {
                if(!idUser.isEmpty()){
                    int j = 0;
                    for(int i = 0; i < idUser.size(); i++){
                        if(idUser.get(i).equals(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                            j = 1;
                            break;
                        }
                    }
                    if(j == 0){
                        idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                    }
                }else{
                    idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                }
                replyMsg.setText("Отправьте имя и фамилию.");
                replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                return replyMsg;
            }
        }

        //РЕГИСТРАЦИЯ//ФИО
        if(update.getCallbackQuery().getData().equals("registration")){
            if(!nameUser.isEmpty()){
                for(int i = 0; i < nameUser.size(); i++){
                    if(nameUser.get(i).startsWith(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                        if(nameUser.get(i).split("_").length == 1) {
                            if(!idUser.isEmpty()){
                                int j = 0;
                                for(int s = 0; s < idUser.size(); s++){
                                    if(idUser.get(s).equals(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                                        j = 1;
                                        break;
                                    }
                                }
                                if(j == 0){
                                    idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                                }
                            }else{
                                idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                            }
                            replyMsg.setText("Отправьте имя и фамилию.");
                            replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }else if(nameUser.get(i).split("_").length == 2) {
                            replyMsg.setText(sendPhoneNumber);
                            replyMsg.setReplyMarkup(kb.ikmBackOrSend);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }else if(nameUser.get(i).split("_").length == 3){
                            replyMsg.setText("Ваши имя и фамилия: " + nameUser.get(i).split("_")[1] + "\nВаш номер телефона: " + nameUser.get(i).split("_")[2] + "\n" +result);
                            replyMsg.setReplyMarkup(kb.ikmOpen);
                            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
                            return replyMsg;
                        }
                    }
                }
            }
            if(!idUser.isEmpty()){
                int j = 0;
                for(int i = 0; i < idUser.size(); i++){
                    if(idUser.get(i).equals(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                        j = 1;
                        break;
                    }
                }
                if(j == 0){
                    idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
                }
            }else{
                idUser.add(update.getCallbackQuery().getFrom().getId().toString() + "_");
            }
            deleteMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), update.getCallbackQuery().getMessage().getMessageId());
            replyMsg.setText(present);
            replyMsg.setReplyMarkup(kb.ikmBackOrSend1);
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        //О НАС
        if(update.getCallbackQuery().getData().equals("about")){
            deleteMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), update.getCallbackQuery().getMessage().getMessageId());
            replyMsg.setText(aboutUs);
            replyMsg.setReplyMarkup(kb.ikmBack);
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("back")){
            deleteMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), update.getCallbackQuery().getMessage().getMessageId());
            replyMsg.setText(welcome);
            replyMsg.setReplyMarkup(kb.ikmMain);
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        if(update.getCallbackQuery().getData().equals("back2")){
            for(int i = 0; i < idUser.size(); i++){
                if(idUser.get(i).startsWith(update.getCallbackQuery().getFrom().getId().toString() + "_")){
                    idUser.remove(i);
                }
            }
            deleteMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), update.getCallbackQuery().getMessage().getMessageId());
            replyMsg.setText(welcome);
            replyMsg.setReplyMarkup(kb.ikmMain);
            replyMsg.setChatId(update.getCallbackQuery().getFrom().getId().toString());
            return replyMsg;
        }

        return replyMsg;
    }

    public void deleteMessage(String chatId, Integer messageId) {
        try{
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);
            execute(deleteMessage);
        }catch(TelegramApiException e){

        }
    }

    @Override
    public String getBotPath() { return null; }
}
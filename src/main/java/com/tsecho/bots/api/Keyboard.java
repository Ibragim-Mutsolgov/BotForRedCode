package com.tsecho.bots.api;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class Keyboard {
    InlineKeyboardMarkup ikmMain;

    InlineKeyboardMarkup ikmBack;

    InlineKeyboardMarkup ikmBackOrSend1;

    InlineKeyboardMarkup ikmBackOrSend2;

    InlineKeyboardMarkup ikmOpen;

    ReplyKeyboardMarkup ikmBackOrSend;

    List<List<InlineKeyboardButton>> itemsMain = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsBack = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsBackOrSend1 = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsBackOrSend2 = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsBackOrSend3 = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsOpen = new ArrayList<>();

    public Keyboard() {
        ikmMain = createIkmMain();

        ikmBack = createIkmBack();

        ikmBackOrSend1 = createIkmBackOrSend1();

        ikmBackOrSend = createReplyKeyboard();

        ikmOpen = createIkmOpen();
    }


    private InlineKeyboardMarkup createIkmMain(){
        InlineKeyboardMarkup inlineKeyboardMarkupStart = new InlineKeyboardMarkup();
        itemsMain.add(getIKMButton("registration","РЕГИСТРАЦИЯ"));
        itemsMain.add(getIKMButton("about","О НАС"));
        List<List<InlineKeyboardButton>> inlineKeysStart = new ArrayList<>(itemsMain);
        inlineKeyboardMarkupStart.setKeyboard(inlineKeysStart);
        return inlineKeyboardMarkupStart;
    }

    private InlineKeyboardMarkup createIkmOpen(){
        InlineKeyboardMarkup inlineKeyboardMarkupOpen = new InlineKeyboardMarkup();
        itemsOpen.add(getIKMButton("open","ПЕРЕЙТИ"));
        itemsOpen.add(getIKMButton("back","НАЗАД"));
        List<List<InlineKeyboardButton>> inlineKeysOpen = new ArrayList<>(itemsOpen);
        inlineKeyboardMarkupOpen.setKeyboard(inlineKeysOpen);
        return inlineKeyboardMarkupOpen;
    }

    private InlineKeyboardMarkup createIkmBack(){
        InlineKeyboardMarkup inlineKeyboardMarkupBack = new InlineKeyboardMarkup();
        itemsBack.add(getIKMButton("back","НАЗАД"));
        List<List<InlineKeyboardButton>> inlineKeysBack = new ArrayList<>(itemsBack);
        inlineKeyboardMarkupBack.setKeyboard(inlineKeysBack);
        return inlineKeyboardMarkupBack;
    }

    private InlineKeyboardMarkup createIkmBackOrSend1(){
        InlineKeyboardMarkup inlineKeyboardMarkupBackOrSend1 = new InlineKeyboardMarkup();
        itemsBackOrSend1.add(getIKMButton("back2","НАЗАД"));
        List<List<InlineKeyboardButton>> inlineKeysBackOrSend1 = new ArrayList<>(itemsBackOrSend1);
        inlineKeyboardMarkupBackOrSend1.setKeyboard(inlineKeysBackOrSend1);
        return inlineKeyboardMarkupBackOrSend1;
    }

    //Для кнопки Отправить мой номер
    private ReplyKeyboardMarkup createReplyKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton getPhoneButton = new KeyboardButton();
        getPhoneButton.setText("ОТПРАВИТЬ НОМЕР ТЕЛЕФОНА");
        getPhoneButton.setRequestContact(true);
        getPhoneButton.setRequestLocation(true);
        keyboardFirstRow.add(getPhoneButton);

        // first keyboard line
        KeyboardRow keyboardFirstRow2 = new KeyboardRow();
        KeyboardButton getPhoneButton2 = new KeyboardButton();
        getPhoneButton2.setText("НАЗАД");
        keyboardFirstRow.add(getPhoneButton2);

        // add array to list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardFirstRow2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;

    }

    private List<InlineKeyboardButton> getIKMButton(String key, String txt){
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(txt);
        btn.setCallbackData(key);
        row.add(btn);
        return row;
    }
}

package com.tsecho.bots.api;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class Keyboard {

    InlineKeyboardMarkup ikmGetCustomer;

    InlineKeyboardMarkup ikmGetServiceConvert;

    InlineKeyboardMarkup ikmGetServiceStart;

    InlineKeyboardMarkup ikmGetServiceLanguage;

    InlineKeyboardMarkup ikmGetServiceAddAndDelete;

    InlineKeyboardMarkup ikmGetServiceDelete;

    InlineKeyboardMarkup ikmGetServiceAdd;

    InlineKeyboardMarkup ikmGetServiceResponse;

    InlineKeyboardMarkup ikmGetMessage;

    InlineKeyboardMarkup ikmGetDeleteBack;

    InlineKeyboardMarkup ikmGetresponse;

    //InlineKeyboardMarkup ikmGetTop;

    //InlineKeyboardMarkup ikmGetGrade;

    //Для трех основных кнопок
    List<List<InlineKeyboardButton>> items = new ArrayList<>();

    //Для кнопок добавить и удалить
    List<List<InlineKeyboardButton>> itemsAddAndDelete = new ArrayList<>();

    //Для одной основной кнопки
    List<List<InlineKeyboardButton>> itemsConvert = new ArrayList<>();

    //Для первой кнопки
    List<List<InlineKeyboardButton>> itemsStart = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsLanguage = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsDelete = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsAdd = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsResponse = new ArrayList<>();

    //List<List<InlineKeyboardButton>> itemsTop = new ArrayList<>();

    //List<List<InlineKeyboardButton>> itemsGrade = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsMessage = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsDeleteBack = new ArrayList<>();

    List<List<InlineKeyboardButton>> itemsresponseback = new ArrayList<>();


    public Keyboard() {
        ikmGetCustomer = createIkmCustomer() ;
        ikmGetServiceConvert = createIkmConvert();
        ikmGetServiceStart = createIkmStart();
        ikmGetServiceLanguage = createIkmLanguage();
        ikmGetServiceAddAndDelete = createIkmAddAndDelete();
        ikmGetServiceAdd = createIkmAdd();
        ikmGetServiceDelete = createIkmDelete();
        ikmGetServiceResponse = createIkmResponse();
        //ikmGetTop = createIkmTop();
        //ikmGetGrade = createIkmGrade();
        ikmGetMessage = createIkmMessage();
        ikmGetDeleteBack = createIkmdeleteBack();
        ikmGetresponse = createIkmresponseback();
    }

    //Кнопки для Исполнителя
    private InlineKeyboardMarkup createIkmStart(){
        InlineKeyboardMarkup inlineKeyboardMarkupStart = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysStart = new ArrayList<>();
        itemsStart.add(getIKMButton("startExecutor","ВОЙТИ КАК ИСПОЛНИТЕЛЬ"));
        itemsStart.add(getIKMButton("startCustomer","ВОЙТИ КАК РАБОТАДАТЕЛЬ"));
        inlineKeysStart.addAll(itemsStart);
        inlineKeyboardMarkupStart.setKeyboard(inlineKeysStart);
        return inlineKeyboardMarkupStart;
    }

    //Кнопки для Раздела программирования
    private InlineKeyboardMarkup createIkmAddAndDelete(){
        InlineKeyboardMarkup inlineKeyboardMarkupAddAndDelete = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysAddAndDelete = new ArrayList<>();
        itemsAddAndDelete.add(getIKMButton("add","ДОБАВИТЬ"));
        itemsAddAndDelete.add(getIKMButton("delete","УДАЛИТЬ"));
        itemsAddAndDelete.add(getIKMButton("mylist","МОИ РАЗДЕЛЫ"));
        itemsAddAndDelete.add(getIKMButton("backChapter","НАЗАД"));
        inlineKeysAddAndDelete.addAll(itemsAddAndDelete);
        inlineKeyboardMarkupAddAndDelete.setKeyboard(inlineKeysAddAndDelete);
        return inlineKeyboardMarkupAddAndDelete;
    }
    //Кнопки откликнуться
    private InlineKeyboardMarkup createIkmResponse(){
        InlineKeyboardMarkup inlineKeyboardMarkupResponse = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysResponse = new ArrayList<>();
        itemsResponse.add(getIKMButton("response","ОТКЛИКНУТЬСЯ"));
        inlineKeysResponse.addAll(itemsResponse);
        inlineKeyboardMarkupResponse.setKeyboard(inlineKeysResponse);
        return inlineKeyboardMarkupResponse;
    }

    //Кнопки Удалить - назад
    private InlineKeyboardMarkup createIkmdeleteBack(){
        InlineKeyboardMarkup inlineKeyboardMarkupDeleteBack = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysDeleteBack = new ArrayList<>();
        itemsDeleteBack.add(getIKMButton("deleteBack","НАЗАД"));
        inlineKeysDeleteBack.addAll(itemsDeleteBack);
        inlineKeyboardMarkupDeleteBack.setKeyboard(inlineKeysDeleteBack);
        return inlineKeyboardMarkupDeleteBack;
    }
    //Отклик назад
    private InlineKeyboardMarkup createIkmresponseback(){
        InlineKeyboardMarkup inlineBack = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inline = new ArrayList<>();
        itemsresponseback.add(getIKMButton("BackResponse","НАЗАД"));
        inline.addAll(itemsresponseback);
        inlineBack.setKeyboard(inline);
        return inlineBack;
    }

    //Кнопки для Исполнителя
    private InlineKeyboardMarkup createIkmLanguage(){
        InlineKeyboardMarkup inlineKeyboardMarkupLanguage = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysLanguage = new ArrayList<>();
        itemsLanguage.add(getIKMButton("system","СИСТЕМНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsLanguage.add(getIKMButton("priklad","ПРИКЛАДНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsLanguage.add(getIKMButton("web","ВЕБ-ПРОГРАММИРОВАНИЕ"));
        itemsLanguage.add(getIKMButton("game","РАЗРАБОТКА ИГР"));
        itemsLanguage.add(getIKMButton("phone","РАЗРАБОТКА МОБИЛЬНЫХ ПРИЛОЖЕНИЙ"));
        inlineKeysLanguage.addAll(itemsLanguage);
        inlineKeyboardMarkupLanguage.setKeyboard(inlineKeysLanguage);
        return inlineKeyboardMarkupLanguage;
    }

    //Кнопки для Исполнителя add
    private InlineKeyboardMarkup createIkmAdd(){
        InlineKeyboardMarkup inlineKeyboardMarkupLanguageadd = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysLanguageadd = new ArrayList<>();
        itemsAdd.add(getIKMButton("systemadd","СИСТЕМНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsAdd.add(getIKMButton("prikladadd","ПРИКЛАДНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsAdd.add(getIKMButton("webadd","ВЕБ-ПРОГРАММИРОВАНИЕ"));
        itemsAdd.add(getIKMButton("gameadd","РАЗРАБОТКА ИГР"));
        itemsAdd.add(getIKMButton("phoneadd","РАЗРАБОТКА МОБИЛЬНЫХ ПРИЛОЖЕНИЙ"));
        itemsAdd.add(getIKMButton("backIKM","НАЗАД"));
        inlineKeysLanguageadd.addAll(itemsAdd);
        inlineKeyboardMarkupLanguageadd.setKeyboard(inlineKeysLanguageadd);
        return inlineKeyboardMarkupLanguageadd;
    }

    //Кнопки для Исполнителя delete
    private InlineKeyboardMarkup createIkmMessage(){
        InlineKeyboardMarkup inlineKeyboardMarkupMessage = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysMessage = new ArrayList<>();
        itemsMessage.add(getIKMButton("MessageBack","НАЗАД"));
        inlineKeysMessage.addAll(itemsMessage);
        inlineKeyboardMarkupMessage.setKeyboard(inlineKeysMessage);
        return inlineKeyboardMarkupMessage;
    }

    //Кнопки для Исполнителя delete
    private InlineKeyboardMarkup createIkmDelete(){
        InlineKeyboardMarkup inlineKeyboardMarkupLanguageDelete = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysLanguageDelete = new ArrayList<>();
        itemsDelete.add(getIKMButton("systemdelete","СИСТЕМНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsDelete.add(getIKMButton("prikladdelete","ПРИКЛАДНОЕ ПРОГРАММИРОВАНИЕ"));
        itemsDelete.add(getIKMButton("webdelete","ВЕБ-ПРОГРАММИРОВАНИЕ"));
        itemsDelete.add(getIKMButton("gamedelete","РАЗРАБОТКА ИГР"));
        itemsDelete.add(getIKMButton("phonedelete","РАЗРАБОТКА МОБИЛЬНЫХ ПРИЛОЖЕНИЙ"));
        itemsDelete.add(getIKMButton("backIKM","НАЗАД"));
        inlineKeysLanguageDelete.addAll(itemsDelete);
        inlineKeyboardMarkupLanguageDelete.setKeyboard(inlineKeysLanguageDelete);
        return inlineKeyboardMarkupLanguageDelete;
    }

    //Кнопки для Исполнителя
    private InlineKeyboardMarkup createIkmConvert(){
        InlineKeyboardMarkup inlineKeyboardMarkupConvert = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeysConvert = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        itemsConvert.add(getIKMButton("chapter","РАЗДЕЛЫ"));
        itemsConvert.add(getIKMButton("payExecutor","ПОДПИСКА"));
        itemsConvert.add(getIKMButton("customerExecutor","ВОЙТИ КАК РАБОТАДАТЕЛЬ"));
        inlineKeysConvert.addAll(itemsConvert);
        inlineKeyboardMarkupConvert.setKeyboard(inlineKeysConvert);
        return inlineKeyboardMarkupConvert;
    }

    //Кнопки для Заказчика
    private InlineKeyboardMarkup createIkmCustomer(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeys = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        items.add(getIKMButton("workCustomer","ОПУБЛИКОВАТЬ РАБОТУ"));
        items.add(getIKMButton("my_worksCustomer","СПИСОК РАБОТ"));
        items.add(getIKMButton("deletework","УДАЛИТЬ РАБОТУ"));
        items.add(getIKMButton("payCustomer","ПОДПИСКА"));
        items.add(getIKMButton("executorCustomer","ВОЙТИ КАК ИСПОЛНИТЕЛЬ"));
        inlineKeys.addAll(items);
        inlineKeyboardMarkup.setKeyboard(inlineKeys);
        return inlineKeyboardMarkup;
    }

    /*
    //Кнопка рейтинг разработчиков
    private InlineKeyboardMarkup createIkmTop(){
        InlineKeyboardMarkup inlineKeyboardTop = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineTop = new ArrayList<>();
        itemsTop.add(getIKMButton("rating","РЕЙТИНГ"));
        itemsTop.add(getIKMButton("leave","ОСТАВИТЬ ОТЗЫВ"));
        itemsTop.add(getIKMButton("back","НАЗАД"));
        inlineTop.addAll(itemsTop);
        inlineKeyboardTop.setKeyboard(inlineTop);
        return inlineKeyboardTop;
    }

    //Кнопка оценки
    private InlineKeyboardMarkup createIkmGrade(){
        InlineKeyboardMarkup inlineKeyboardGrade = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineGrade = new ArrayList<>();
        itemsGrade.add(getIKMButton("5","5"));
        itemsGrade.add(getIKMButton("4","4"));
        itemsGrade.add(getIKMButton("3","3"));
        itemsGrade.add(getIKMButton("2","2"));
        itemsGrade.add(getIKMButton("1","1"));
        inlineGrade.addAll(itemsGrade);
        inlineKeyboardGrade.setKeyboard(inlineGrade);
        return inlineKeyboardGrade;
    }
    */

    private List<InlineKeyboardButton> getIKMButton(String key, String txt){
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(txt);
        btn.setCallbackData(key);
        row.add(btn);
        return row;
    }
}

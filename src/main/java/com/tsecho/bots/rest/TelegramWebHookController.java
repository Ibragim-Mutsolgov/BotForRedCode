package com.tsecho.bots.rest;


import com.tsecho.bots.api.RedCodeBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class TelegramWebHookController {


    @Autowired
    RedCodeBot redCodeBotBot;

    public TelegramWebHookController(RedCodeBot redcodeBot) {
        this.redCodeBotBot = redcodeBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        // в зависимости от типа запроса(ответа)
        // update.getCallbackQuery().
        return redCodeBotBot.onWebhookUpdateReceived(update);
    }
}

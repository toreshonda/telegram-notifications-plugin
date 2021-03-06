package jenkinsci.plugins.telegrambot.telegram.commands;

import jenkinsci.plugins.telegrambot.users.Subscribers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SubCommand extends AbstractBotCommand {

    private static final String LOG_TAG = "/sub";
    private static final Logger LOGGER = LoggerFactory.getLogger(SubCommand.class);

    public SubCommand() {
        super("sub", "command.sub");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Subscribers subscribers = Subscribers.getInstance();
        String ans;

        Long id = chat.getId();
        String name = chat.isUserChat() ? user.toString() : chat.toString();

        boolean isSubscribed = subscribers.isSubscribed(id);

        if (!isSubscribed) {
            subscribers.subscribe(name, id);
            ans = botStrings.get("message.sub.success");
        } else {
            ans = botStrings.get("message.sub.alreadysub");
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(ans);

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            LOGGER.error(LOG_TAG, e);
        }
    }
}

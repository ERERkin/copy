package it.academy.demoBot.handlers;

import it.academy.demoBot.Infrastructure.State;
import it.academy.demoBot.Services.Emoji;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    public static Map<Long, State> chatIdStatus = new HashMap<>();
    public static Map<Long, Double> chatIdTotalIncome = new HashMap<>();
    public static Map<Long, Double> chatIdTotalExpense = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            handleMessage(message);
        } else if (update.hasCallbackQuery()) {
            try {
                execute(new SendMessage().setText(
                        update.getCallbackQuery().getData())
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {

        State currentState = chatIdStatus.get(message.getChatId());
//        try {
//            for(int i = 0; i < 100; i++)
//                execute(new SendMessage(message.getChatId(), Emoji.HEAVY_BLACK_HEART.toString()));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
        if (message.hasText()) {
            if (message.getText().equals("Hello")) {
                try {
                    execute(sendInlineKeyBoardMessage(message.getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().equals("Menu")) {
                try {
                    execute(messageOnMainMenu(message, "Please select"));
                    chatIdStatus.put(message.getChatId(), State.MAIN_MENU);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().equals(getIncomeCommand())) {
                try {
                    execute(new SendMessage(message.getChatId(), "Please enter amount"));
                    chatIdStatus.put(message.getChatId(), State.INCOME);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().equals(getExpenseCommand())) {
                try {
                    execute(new SendMessage(message.getChatId(), "Please enter amount"));
                    chatIdStatus.put(message.getChatId(), State.EXPENSE);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().equals(getResetCommand())) {
                try {
                    execute(new SendMessage(message.getChatId(), "Everything was reset").setReplyToMessageId(message.getMessageId()));
                    chatIdStatus.put(message.getChatId(), State.NOT_SELECTED);
                    chatIdTotalIncome.clear();
                    chatIdTotalExpense.clear();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.getText().equals(getTotalCommand())) {
                try {
                    Double expenses = chatIdTotalExpense.get(message.getChatId());
                    Double incomes = chatIdTotalIncome.get(message.getChatId());
                    expenses = expenses == null ? 0 : expenses;
                    incomes = incomes == null ? 0 : incomes;
                    double total = incomes - expenses;
                    execute(new SendMessage(message.getChatId(),
                            String.format("Total: %s \nIncome: %s \nExpense: %s",
                                    Double.toString(total),
                                    incomes.toString(),
                                    expenses.toString())));
                    chatIdStatus.put(message.getChatId(), State.INCOME);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                processNonCommandMessage(message);
            }
        }
    }

    private void processNonCommandMessage(Message message) {
        Double enteredAmount = tryParseDouble(message.getText());
        if (enteredAmount != null)
            switch (chatIdStatus.get(message.getChatId())) {
                case INCOME:
                    Double prev = chatIdTotalIncome.get(message.getChatId());
                    chatIdTotalIncome.put(message.getChatId(), (prev != null ? prev : 0) + enteredAmount);
                    break;
                case EXPENSE:
                    Double prevExp = chatIdTotalExpense.get(message.getChatId());
                    chatIdTotalExpense.put(message.getChatId(), (prevExp != null ? prevExp : 0) + enteredAmount);
                    break;
                default:
                    break;
            }
    }

    private Double tryParseDouble(String value) {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static SendMessage messageOnMainMenu(Message message, String messageText) {
        SendMessage sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                getMainMenuKeyboard(false), messageText);

        return sendMessageRequest;
    }

    private static SendMessage sendChooseOptionMessage(Long chatId, Integer messageId,
                                                       ReplyKeyboard replyKeyboard, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(messageText);

        return sendMessage;
    }

    private static ReplyKeyboardMarkup getMainMenuKeyboard(boolean isOneTime) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTime);

        List<KeyboardRow> keyboard = new ArrayList<>();

        if (!isOneTime) {
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            keyboardFirstRow.add(getIncomeCommand());
            keyboardFirstRow.add(getExpenseCommand());

            KeyboardRow keyboardSecondRow = new KeyboardRow();
            keyboardSecondRow.add(getTotalCommand());

            KeyboardRow keyboardThirdRow = new KeyboardRow();
            keyboardThirdRow.add(getResetCommand());

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private static String getTotalCommand() {
        return String.format("%sTotal",
                Emoji.SHUSHING_FACE.toString());
    }

    private static String getIncomeCommand() {
        return String.format("%sIncome",
                Emoji.HEAVY_PLUS_SIGN.toString());
    }

    private static String getExpenseCommand() {
        return String.format("%sExpense",
                Emoji.HEAVY_MINUS_SIGN.toString());
    }

    private static String getResetCommand() {
        return String.format("%sReset",
                Emoji.CROSS_MARK.toString());
    }


    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        String s = " ";
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton9 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(s);
        inlineKeyboardButton1.setCallbackData("Hello");
        inlineKeyboardButton2.setText("-");
        inlineKeyboardButton2.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton3.setText("-");
        inlineKeyboardButton3.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton4.setText("-");
        inlineKeyboardButton4.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton5.setText("-");
        inlineKeyboardButton5.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton6.setText("-");
        inlineKeyboardButton6.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton7.setText("-");
        inlineKeyboardButton7.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton8.setText("-");
        inlineKeyboardButton8.setCallbackData("Button \"Тык\" has been pressed");
        inlineKeyboardButton9.setText("-");
        inlineKeyboardButton9.setCallbackData("Button \"Тык\" has been pressed");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);
        keyboardButtonsRow2.add(inlineKeyboardButton5);
        keyboardButtonsRow2.add(inlineKeyboardButton6);
        keyboardButtonsRow3.add(inlineKeyboardButton7);
        keyboardButtonsRow3.add(inlineKeyboardButton8);
        keyboardButtonsRow3.add(inlineKeyboardButton9);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Поехали!").setReplyMarkup(inlineKeyboardMarkup);
    }


    @Override
    public String getBotUsername() {
        return "tic_tactoe_bot";
    }

    @Override
    public String getBotToken() {
        return "1110465489:AAE4CE06N9WTvR1Yt5rXsSumsvxcanCeT8Q";
    }

}

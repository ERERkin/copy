package it.academy.demoBot.handlers;

import it.academy.demoBot.Controller;
import it.academy.demoBot.Dao.GameDao;
import it.academy.demoBot.Infrastructure.State;
import it.academy.demoBot.Services.Emoji;
import it.academy.demoBot.model.Game;
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
    static int playerId = 0;
    Message msg;
    static long cd = -1;
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            msg = message;
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
        if (message.hasText()) {
            if (message.getText().equals("play")) {
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
            }else if (message.getText().equals("1")) {
                playerId = 1;
            } else if (message.getText().equals("2")) {
                playerId = 2;
            }else if (message.getText().equals("0")) {
                playerId = 0;
                Controller.gameOver();
            }
            else if (message.getText().equals(getIncomeCommand())) {
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
            }else if (message.getText().equals(getCommandCommand1())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(1)));
                    if(cd != -1){
                        execute(new SendMessage(msg.getChatId(), getLast(1)));
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand2())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(2)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand3())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(3)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand4())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(4)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand5())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(5)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand6())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(6)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if (message.getText().equals(getCommandCommand7())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(7)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand8())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(8)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message.getText().equals(getCommandCommand9())) {
                try {
                    execute(new SendMessage(message.getChatId(), changeBD(9)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
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
//            keyboardFirstRow.add(getIncomeCommand());
//            keyboardFirstRow.add(getExpenseCommand());
            keyboardFirstRow.add(getCommandCommand1());
            keyboardFirstRow.add(getCommandCommand2());
            keyboardFirstRow.add(getCommandCommand3());
            KeyboardRow keyboardSecondRow = new KeyboardRow();
//            keyboardSecondRow.add(getTotalCommand());
            keyboardSecondRow.add(getCommandCommand4());
            keyboardSecondRow.add(getCommandCommand5());
            keyboardSecondRow.add(getCommandCommand6());
            KeyboardRow keyboardThirdRow = new KeyboardRow();
            keyboardThirdRow.add(getCommandCommand7());
            keyboardThirdRow.add(getCommandCommand8());
            keyboardThirdRow.add(getCommandCommand9());
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private static String getTotalCommand() {
        return String.format("%sГЫ",
                Emoji.SHUSHING_FACE.toString());
    }

    private static String getCommandCommand1() {
        return String.format("%s1",
                Emoji.SHUSHING_FACE.toString());
    }

    private static String getCommandCommand2() {
        return String.format("%s2",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand3() {
        return String.format("%s3",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand4() {
        return String.format("%s4",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand5() {
        return String.format("%s5",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand6() {
        return String.format("%s6",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand7() {
        return String.format("%s7",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand8() {
        return String.format("%s8",
                Emoji.SHUSHING_FACE.toString());
    }private static String getCommandCommand9() {
        return String.format("%s9",
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
        inlineKeyboardButton1.setCallbackData(changeBD(1));

        inlineKeyboardButton2.setText("-");
        inlineKeyboardButton2.setCallbackData(changeBD(2));
        inlineKeyboardButton3.setText("-");
        inlineKeyboardButton3.setCallbackData(changeBD(3));
        inlineKeyboardButton4.setText("-");
        inlineKeyboardButton4.setCallbackData(changeBD(4));
        inlineKeyboardButton5.setText("-");
        inlineKeyboardButton5.setCallbackData(changeBD(5));
        inlineKeyboardButton6.setText("-");
        inlineKeyboardButton6.setCallbackData(changeBD(6));
        inlineKeyboardButton7.setText("-");
        inlineKeyboardButton7.setCallbackData(changeBD(7));
        inlineKeyboardButton8.setText("-");
        inlineKeyboardButton8.setCallbackData(changeBD(8));
        inlineKeyboardButton9.setText("-");
        inlineKeyboardButton9.setCallbackData(changeBD(9));
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
        System.out.println(playerId);
        return new SendMessage().setChatId(chatId).setText("Поехали!").setReplyMarkup(inlineKeyboardMarkup);
    }

    static String changeBD(int button){
        if(playerId == 0)playerId = 1;
        else if(playerId == 1) playerId = 2;
        else if(playerId == 2) playerId = 1;
        System.out.println(button);
        Game game = Controller.update(button, playerId);
        String ans = "";
        /*String ans = "" + game.getButton1()  + " " +
                game.getButton2() + " " + game.getButton3() + "\n" +
                game.getButton4() + " " +
                game.getButton5() + " " + game.getButton6() + "\n" +
                game.getButton7() + " " +
                game.getButton8() + " " + game.getButton9();*/
        if(game.getButton1() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton1() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        if(game.getButton2() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton2() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        if(game.getButton3() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton3() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        ans += '\n';
        if(game.getButton4() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton4() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton5() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton5() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton6() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton6() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        ans += '\n';
        if(game.getButton7() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton7() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton8() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton8() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton9() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton9() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        return ans;
    }


    @Override
    public String getBotUsername() {
        return "tic_tactoe_bot";
    }

    @Override
    public String getBotToken() {
        return "1110465489:AAE4CE06N9WTvR1Yt5rXsSumsvxcanCeT8Q";
    }

    static String getLast(int button){
        Game game = GameDao.getLastGame();
        String ans = "";
        /*String ans = "" + game.getButton1()  + " " +
                game.getButton2() + " " + game.getButton3() + "\n" +
                game.getButton4() + " " +
                game.getButton5() + " " + game.getButton6() + "\n" +
                game.getButton7() + " " +
                game.getButton8() + " " + game.getButton9();*/
        if(game.getButton1() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton1() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        if(game.getButton2() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton2() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        if(game.getButton3() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton3() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        ans += '\n';
        if(game.getButton4() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton4() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton5() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton5() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton6() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton6() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        ans += '\n';
        if(game.getButton7() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton7() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton8() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton8() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;

        if(game.getButton9() == 1) ans += Emoji.CROSS_MARK;
        else if(game.getButton9() == 2) ans += Emoji.NEGATIVE_SQUARED_CROSS_MARK;
        else ans += Emoji.CURLY_LOOP;
        return ans;
    }

}

// Варіант 6: Builder, Adapter, Interpreter
// Тема: Система створення і друку різних типів повідомлень у месенджері

import java.util.*;

// === Interpreter Pattern ===
interface Expression {
    boolean interpret(String context);
}

class KeywordExpression implements Expression {
    private String keyword;

    public KeywordExpression(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean interpret(String context) {
        return context.contains(keyword);
    }
}

// === Message Entity ===
class Message {
    String content;
    String type;
    public Message(String content, String type) {
        this.content = content;
        this.type = type;
    }
    public String getContent() { return content; }
    public String getType() { return type; }
}

// === Builder Pattern ===
abstract class MessageBuilder {
    protected Message message;
    public void createNewMessage() { message = null; }
    public Message getMessage() { return message; }
    public abstract void buildContent(String content);
    public abstract void buildType();
}

class AlertMessageBuilder extends MessageBuilder {
    @Override
    public void buildContent(String content) {
        message = new Message("[ALERT]: " + content, "alert");
    }
    @Override
    public void buildType() {}
}

class InfoMessageBuilder extends MessageBuilder {
    @Override
    public void buildContent(String content) {
        message = new Message("[INFO]: " + content, "info");
    }
    @Override
    public void buildType() {}
}

class Director {
    private MessageBuilder builder;
    public void setBuilder(MessageBuilder b) {
        builder = b;
    }
    public Message construct(String content) {
        builder.buildContent(content);
        return builder.getMessage();
    }
}

// === Adapter Pattern ===
interface MessagePrinter {
    void print(List<Message> messages);
}

class ConsolePrinter {
    public void printMessage(Message msg) {
        System.out.println(msg.getContent());
    }
}

class PrinterAdapter implements MessagePrinter {
    private ConsolePrinter printer = new ConsolePrinter();

    @Override
    public void print(List<Message> messages) {
        for (Message m : messages) {
            printer.printMessage(m);
        }
    }
}

// === Main Application ===
public class Main {
    public static void main(String[] args) {
        Director director = new Director();
        Expression isAlert = new KeywordExpression("!");

        List<Message> messages = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            String content = i % 2 == 0 ? "System running smoothly." : "!Disk space low!";
            boolean alert = isAlert.interpret(content);

            if (alert) {
                director.setBuilder(new AlertMessageBuilder());
            } else {
                director.setBuilder(new InfoMessageBuilder());
            }

            Message msg = director.construct(content);
            messages.add(msg);
        }

        PrinterAdapter printer = new PrinterAdapter();
        printer.print(messages);
    }
}

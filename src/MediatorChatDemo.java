import java.util.*;
import java.util.concurrent.*;

interface Mediator {
    void join(User u);
    void leave(User u);
    void sendToAll(String from, String msg);
    void sendPrivate(String from, String to, String msg);
    List<String> memberList();
}

class ChatRoom implements Mediator {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final ExecutorService exec = Executors.newCachedThreadPool();

    public void join(User u) {
        if (u == null || u.name == null) return;
        users.put(u.name, u);
        System.out.println("========================");
        System.out.println(u.name + " присоединился к чату");
        broadcastSystem(u.name + " присоединился");
    }

    public void leave(User u) {
        if (u == null) return;
        users.remove(u.name);
        System.out.println("========================");
        System.out.println(u.name + " покинул чат");
        broadcastSystem(u.name + " покинул чат");
    }

    public void sendToAll(String from, String msg) {
        System.out.println("========================");
        System.out.println(from + " отправил всем: " + msg);
        for (User u : users.values()) {
            if (!u.name.equals(from)) {
                exec.submit(() -> u.receive(from, msg));
            }
        }
    }

    public void sendPrivate(String from, String to, String msg) {
        User target = users.get(to);
        if (target == null) {
            System.out.println("========================");
            System.out.println("попытка отправить личное сообщение, но пользователь не найден: " + to);
            return;
        }
        System.out.println("========================");
        System.out.println(from + " -> " + to + ": " + msg);
        exec.submit(() -> target.receive(from + " (лично)", msg));
    }

    public List<String> memberList() {
        return new ArrayList<>(users.keySet());
    }

    private void broadcastSystem(String notice) {
        for (User u : users.values()) exec.submit(() -> u.receive("система", notice));
    }

    public void shutdown() {
        exec.shutdown();
    }
}

class User {
    final String name;
    private final Mediator room;
    public User(String name, Mediator room) { this.name = name; this.room = room; }
    public void sendAll(String msg) { room.sendToAll(name, msg); }
    public void sendPrivate(String to, String msg) { room.sendPrivate(name, to, msg); }
    public void receive(String from, String msg) {
        System.out.println("========================");
        System.out.println("[" + name + "] сообщение от " + from + ": " + msg);
    }
    public void join() { room.join(this); }
    public void leave() { room.leave(this); }
}

public class MediatorChatDemo {
    public static void main(String[] args) throws InterruptedException {
        ChatRoom room = new ChatRoom();

        User dilnaz = new User("Дильназ", room);
        User sumaya = new User("Сумая", room);
        User madiyar = new User("Мадияр", room);

        dilnaz.join();
        sumaya.join();
        madiyar.join();

        Thread.sleep(200);

        dilnaz.sendAll("сәлем, как дела?");
        sumaya.sendPrivate("Мадияр", "ты идёшь на встречу?");
        madiyar.sendAll("я буду через 10 минут");

        Thread.sleep(200);

        System.out.println("========================");
        System.out.println("участники сейчас: " + room.memberList());

        madiyar.leave();

        Thread.sleep(200);

        sumaya.sendPrivate("Мадияр", "попробую отправить после ухода");

        Thread.sleep(200);

        room.shutdown();
    }
}
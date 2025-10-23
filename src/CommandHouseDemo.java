import java.util.*;

interface ICommand {
    void execute();
    void undo();
}

class LightDevice {
    private boolean on = false;
    private final String name;
    public LightDevice(String name) { this.name = name; }
    public void on() {
        on = true;
        System.out.println("========================");
        System.out.println("свет в " + name + " включён");
    }
    public void off() {
        on = false;
        System.out.println("========================");
        System.out.println("свет в " + name + " выключен");
    }
}

class DoorDevice {
    private boolean open = false;
    private final String name;
    public DoorDevice(String name) { this.name = name; }
    public void open() {
        open = true;
        System.out.println("========================");
        System.out.println("дверь " + name + " открыта");
    }
    public void close() {
        open = false;
        System.out.println("========================");
        System.out.println("дверь " + name + " закрыта");
    }
}

class ThermostatDevice {
    private double temp = 22.0;
    private final String name;
    public ThermostatDevice(String name) { this.name = name; }
    public void increase(double delta) {
        temp += delta;
        System.out.println("========================");
        System.out.println("температура " + name + " поднята до " + temp + "°C");
    }
    public void decrease(double delta) {
        temp -= delta;
        System.out.println("========================");
        System.out.println("температура " + name + " опущена до " + temp + "°C");
    }
}

class LightOnCommand implements ICommand {
    private final LightDevice light;
    public LightOnCommand(LightDevice light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements ICommand {
    private final LightDevice light;
    public LightOffCommand(LightDevice light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class DoorOpenCommand implements ICommand {
    private final DoorDevice door;
    public DoorOpenCommand(DoorDevice door) { this.door = door; }
    public void execute() { door.open(); }
    public void undo() { door.close(); }
}

class DoorCloseCommand implements ICommand {
    private final DoorDevice door;
    public DoorCloseCommand(DoorDevice door) { this.door = door; }
    public void execute() { door.close(); }
    public void undo() { door.open(); }
}

class TempUpCommand implements ICommand {
    private final ThermostatDevice thermo;
    private final double delta;
    public TempUpCommand(ThermostatDevice thermo, double delta) { this.thermo = thermo; this.delta = delta; }
    public void execute() { thermo.increase(delta); }
    public void undo() { thermo.decrease(delta); }
}

class TempDownCommand implements ICommand {
    private final ThermostatDevice thermo;
    private final double delta;
    public TempDownCommand(ThermostatDevice thermo, double delta) { this.thermo = thermo; this.delta = delta; }
    public void execute() { thermo.decrease(delta); }
    public void undo() { thermo.increase(delta); }
}

class HomeInvoker {
    private final Deque<ICommand> history = new ArrayDeque<>();
    public void run(ICommand cmd) {
        try {
            cmd.execute();
            history.push(cmd);
        } catch (Exception e) {
            System.out.println("========================");
            System.out.println("ошибка выполнения команды");
        }
    }
    public void undoLast() {
        if (history.isEmpty()) {
            System.out.println("========================");
            System.out.println("нет команд для отмены");
            return;
        }
        ICommand cmd = history.pop();
        try {
            cmd.undo();
        } catch (Exception e) {
            System.out.println("========================");
            System.out.println("ошибка при отмене команды");
        }
    }
    public void undoMultiple(int n) {
        for (int i = 0; i < n; i++) undoLast();
    }
}

public class CommandHouseDemo {
    public static void main(String[] args) {
        HomeInvoker remote = new HomeInvoker();
        LightDevice living = new LightDevice("гостиная");
        DoorDevice front = new DoorDevice("входная");
        ThermostatDevice hall = new ThermostatDevice("коридор");

        ICommand lightOn = new LightOnCommand(living);
        ICommand lightOff = new LightOffCommand(living);
        ICommand doorOpen = new DoorOpenCommand(front);
        ICommand doorClose = new DoorCloseCommand(front);
        ICommand tempUp = new TempUpCommand(hall, 2.5);
        ICommand tempDown = new TempDownCommand(hall, 1.5);

        System.out.println("========================");
        System.out.println("пульт активирован для семьи Дильназ и Мадияр");

        remote.run(lightOn);
        remote.run(doorOpen);
        remote.run(tempUp);

        remote.run(lightOff);
        remote.run(doorClose);

        System.out.println("========================");
        System.out.println("отменяем последние две команды");
        remote.undoMultiple(2);

        System.out.println("========================");
        System.out.println("добавим ещё команд и отменим по одной");
        remote.run(tempDown);
        remote.run(lightOff);
        remote.undoLast();
        remote.undoLast();

        System.out.println("========================");
        System.out.println("конец демонстрации для Сумая");
    }
}
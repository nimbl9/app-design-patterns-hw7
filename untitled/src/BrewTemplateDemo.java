import java.util.Scanner;

abstract class DrinkMaker {
    public final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) addCondiments();
        finalTouch();
    }
    void boilWater() {
        System.out.println("========================");
        System.out.println("кипятим воду");
    }
    abstract void brew();
    void pourInCup() {
        System.out.println("========================");
        System.out.println("наливаем в чашку");
    }
    abstract void addCondiments();
    boolean customerWantsCondiments() {
        return true;
    }
    void finalTouch() {
        System.out.println("========================");
        System.out.println("напиток готов");
    }
}

class TeaMaker extends DrinkMaker {
    public void brew() {
        System.out.println("========================");
        System.out.println("настаиваем чайный пакетик");
    }
    public void addCondiments() {
        System.out.println("========================");
        System.out.println("добавляем лимон");
    }
    private boolean wantCondiments = true;
    public void setWantCondiments(boolean v) { this.wantCondiments = v; }
    boolean customerWantsCondiments() { return wantCondiments; }
}

class CoffeeMaker extends DrinkMaker {
    public void brew() {
        System.out.println("========================");
        System.out.println("завариваем кофе");
    }
    public void addCondiments() {
        System.out.println("========================");
        System.out.println("добавляем молоко и сахар");
    }
    private boolean wantCondiments = true;
    public void setWantCondiments(boolean v) { this.wantCondiments = v; }
    boolean customerWantsCondiments() { return wantCondiments; }
}

class CocoaMaker extends DrinkMaker {
    public void brew() {
        System.out.println("========================");
        System.out.println("разводим горячий шоколад");
    }
    public void addCondiments() {
        System.out.println("========================");
        System.out.println("добавляем маршмеллоу");
    }
}

public class BrewTemplateDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TeaMaker tea = new TeaMaker();
        CoffeeMaker coffee = new CoffeeMaker();
        CocoaMaker cocoa = new CocoaMaker();

        System.out.println("========================");
        System.out.println("выберите напиток: 1 - чай, 2 - кофе, 3 - горячий шоколад");
        System.out.print("> ");
        String ch = sc.nextLine().trim();

        switch (ch) {
            case "1" -> {
                System.out.println("========================");
                System.out.print("хотите добавить лимон? (y/n): ");
                String a = sc.nextLine().trim();
                tea.setWantCondiments(a.equalsIgnoreCase("y"));
                tea.prepareRecipe();
            }
            case "2" -> {
                System.out.println("========================");
                System.out.print("хотите молоко и сахар? (y/n): ");
                String a = sc.nextLine().trim();
                coffee.setWantCondiments(a.equalsIgnoreCase("y"));
                coffee.prepareRecipe();
            }
            case "3" -> cocoa.prepareRecipe();
            default -> {
                System.out.println("========================");
                System.out.println("неверный выбор");
            }
        }

        System.out.println("========================");
        System.out.println("готовка завершена для Дильназ, Сумая и Мадияр");
    }
}
package hometheater;

public class Main {
    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade();

        System.out.println(theater.watchMovie("Interstellar"));
        System.out.println(theater.endMovie());
    }
}

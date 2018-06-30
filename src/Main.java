public class Main {
    public static void main(String[] args){
        Game game = new Game(1, null);
        while (game != null) {
            game = game.run();
        }
    }
}
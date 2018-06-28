import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

Objects[][] objects = new Objects[17][17];
ArrayList<Entity> entities = new ArrayList<>();

public Game() {
    for (int i = 0; i < objects.length; i++) {
        Arrays.fill(objects[i], Objects.FLOOR);
    }
    entities.add(new Entity(Entities.PLAYER, 8, 8, 100, 5));
    entities.add(new Entity(Entities.GOBLIN, (int) (Math.random() * 17), (int) (Math.random() * 17), 30, 5));
    entities.add(new Entity(Entities.GOBLIN, (int) (Math.random() * 17), (int) (Math.random() * 17), 30, 5));
    entities.add(new Entity(Entities.BAT, (int) (Math.random() * 17), (int) (Math.random() * 17), 15, 4));
    entities.add(new Entity(Entities.BAT, (int) (Math.random() * 17), (int) (Math.random() * 17), 15, 4));
}

public void run() {
    draw();
    command();
    while (true) {

    }
}

private Entity getEntity(int x, int y) {
    for (Entity e : entities) {
        if (e.x == x && e.y == y) {
            return e;
        }
    }
    return null;
}

public void draw() {
    for (int y = 0; y < objects.length; y++) {
        for (int x = 0; x < objects[y].length; x++) {
            Entity entity = getEntity(x, y);
            if (entity != null) {
                System.out.format("%s ", entity.getType().getEntity());
            } else {
                Objects currentObject = objects[x][y];
                System.out.format("%s ", currentObject.getObject());
            }
        }
        System.out.println();
    }
    command();
}

public void tryAttack(Entity attacker, int modx, int mody) {
    Entity enemy = getEntity(attacker.x + modx, attacker.y + mody);
    if (enemy != null) {
        enemy.health -= attacker.attack;
        System.out.print("\nYour health: \n");
        System.out.print("{");
        for (int i = 1; i < attacker.health; i++) {
            System.out.print("=");
        }
        System.out.print("}\n");
        System.out.format("%s's health\n", enemy.getType().toString());
        for (int i = 1; i < enemy.health; i++) {
            System.out.print("=");
        }
        System.out.print("}\n");
    } else {
        System.out.print("\n You swing your sword at thin air...");
    }
}

public void attack(char direction) {
    for (Entity e : entities) {
        if (e.getType() == Entities.PLAYER) {
            if(direction == 'w'){
                tryAttack(e, 0, -1);
            }
            else if(direction == 's'){
                tryAttack(e, 0, 1);
            }
            else if(direction == 'a'){
                if(getEntity(e.x - 1, e.y) != null) {
                    tryAttack(e, -1, 0);
                } else System.out.print("\nYou swing your sword at thin air...");
            }
            else if(direction == 'd'){
                if(getEntity(e.x + 1, e.y) != null) {
                    tryAttack(e, 1, 0);
                } else System.out.print("\nYou swing your sword at thin air...");
            }
        }
    }
}

public void move(char direction) {
    for (Entity e : entities) {
        if (e.getType() == Entities.PLAYER) {
            if(direction == 'w'){
                if(getEntity(e.x, e.y - 1) == null) {
                    e.y--;
                } else System.out.print("\na creature blocks your path.");
            }
            else if(direction == 's'){
                if(getEntity(e.x, e.y + 1) == null) {
                    e.y++;
                } else System.out.print("\na creature blocks your path.");
            }
            else if(direction == 'a'){
                if(getEntity(e.x - 1, e.y) == null) {
                    e.x--;
                } else System.out.print("\na creature blocks your path.");
            }
            else if(direction == 'd'){
                if(getEntity(e.x + 1, e.y) == null) {
                    e.x++;
                } else System.out.print("\na creature blocks your path.");
            }
        }
    }
}

public void command() {
    String command = null;
    Scanner sc = new Scanner(System.in);
    System.out.print("\nWhat will you do?: ");
    command = sc.nextLine();
    if (command.equals("help")/* Compares if 'command' and '"help"' are the same 'pointers' command == "help"*/) {
        System.out.println("Available commands: \nmove [up, down, left, right]\nattack [up, down, left, right]\nuse [item]");
    }
    else if (command.equals("move up")) {
            System.out.print("\nYou move up!\n");
            move('w');
            draw();
    }
    else if (command.equals("move down")) {
        System.out.print("\nYou move down!\n");
        move('s');
        draw();
    }
    else if (command.equals("move left")) {
        System.out.print("\nYou move right!\n");
        move('a');
        draw();
    }
    else if (command.equals("move right")) {
        System.out.print("\nYou move right!\n");
        move('d');
        draw();
    }
    else if (command.equals("attack up")) {
        System.out.print("\nYou attack forward\n");
        attack('w');
        draw();
    }
    else if(command.equals("attack right")){
        attack('d');
        draw();
    }
    else if (command.equals("use")) {
            //todo: create use function.
            System.out.print("\nYou use an item.\n");
        }
        else {
            System.out.print("\nPlease use a valid command. type 'help' for help with commands.\n");
            command();
    }
    }
}


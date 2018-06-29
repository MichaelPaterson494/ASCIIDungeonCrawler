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

        for (int x = 0; x < objects.length; x++) {
            for (int y = 0; y < objects[x].length; y++) {
                if (x == 0 || x == 16) {
                    objects[x][y] = Objects.WALL;
                }
                if (y == 0 || y == 16) {
                    objects[x][y] = Objects.WALL;
                }
//
//                //Visualize detection radius.
//                float distance = 2.4f;
//                int centerX = 8;
//                int centerY = 8;
//
//                int diffX = centerX - x;
//                int diffY = centerY - y;
//
//                float dist = (float)Math.sqrt(diffX * diffX + diffY * diffY);
//                if (dist < distance) {
//                    objects[x][y] = Objects.WALL;
//                }
            }
        }

        entities.add(new Entity(Entities.PLAYER, 8, 8, 20, 3, 4));
        entities.add(new Entity(Entities.GOBLIN, (int) (1 + Math.random() * 15), (int) (1 + Math.random() * 15), 10, 1, 4));
        entities.add(new Entity(Entities.GOBLIN, (int) (1 + Math.random() * 15), (int) (1 + Math.random() * 15), 10, 1, 4));
        entities.add(new Entity(Entities.BAT, (int) (1 + Math.random() * 15), (int) (1 + Math.random() * 15), 4, 2, 4));
        entities.add(new Entity(Entities.BAT, (int) (1 + Math.random() * 15), (int) (1 + Math.random() * 15), 4, 2, 4));

    }

    public void run() {
        draw();
        command();
        while (true) {

        }
    }

    private void moveEntities(){
        for (Entity entity : entities){
            if(entity.getType() != Entities.PLAYER) {
                Entity seenTarget = tryDetect(entity, Entities.PLAYER);
                int moveChance = (int) (Math.random() * 99);
                if (moveChance > 31) {
                    if (seenTarget == null) {
                        int move = (int) (Math.random() * 3);
                        if (move == 0) {
                            move(entity, 'w');
                        } else if (move == 1) {
                            move(entity, 'a');
                        } else if (move == 2) {
                            move(entity, 's');
                        } else if (move == 3) {
                            move(entity, 'd');
                        }
                    }
                    else {
                        int dirX = seenTarget.x - entity.x;
                        int dirY = seenTarget.y - entity.y;

                        char direction;
                        if (Math.abs(dirX) > Math.abs(dirY)) {
                            direction = dirX < 0 ? 'a' : 'd';
                        }
                        else {
                            direction = dirY < 0 ? 'w' : 's';
                        }
                        move(entity, direction);

                    }
                }
            }
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

    public boolean isDead() {
        for (Entity entity : entities) {
            if (entity.health <= 0) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        moveEntities();
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

    private void getHealth(Entity entity) {
        for (int i = 0; i < entity.health; i++) {
            if (entity.getType() == Entities.PLAYER) {
                System.out.print("\u001B[32m=\u001B[0m");
            } else {
                System.out.print("\u001B[31m=\u001B[0m");
            }
        }
    }

    public void tryAttack(Entity attacker, int modx, int mody) {
        Entity enemy = getEntity(attacker.x + modx, attacker.y + mody);
        if (enemy != null) {
            enemy.health -= attacker.attack;
            if (isDead()) {
                if (enemy.getType() == Entities.PLAYER) {
                    entities.remove(enemy);
                    System.out.print("\nYou have Died. Game Over\n");
                    //todo: add close game function.
                } else {
                    entities.remove(enemy);
                    System.out.format("\nThe %s has died by your hand.\n", enemy.getType().toString());
                    return;
                }
            }
            System.out.print("\nYour health: \n");
            System.out.print("{");

            if (enemy.getType() == Entities.PLAYER) {
                getHealth(enemy);
            } else {
                getHealth(attacker);
            }
            System.out.print("}\n");
            System.out.format("\n%s's health\n", enemy.getType().toString());
            System.out.print("{");
            if (attacker.getType() == Entities.PLAYER) {
                getHealth(enemy);
            } else {
                getHealth(attacker);
            }
            System.out.print("}\n");
        } else {
            System.out.print("\n You swing your sword at thin air...\n");
        }
    }

    public void attack(Entities attacker, char direction) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e.getType() == attacker) {
                if (direction == 'w') {
                    tryAttack(e, 0, -1);
                } else if (direction == 's') {
                    tryAttack(e, 0, 1);
                } else if (direction == 'a') {
                    tryAttack(e, -1, 0);
                } else if (direction == 'd') {
                    tryAttack(e, 1, 0);
                }
            }
        }
    }

    public void moveAll(Entities entity, char direction) {
        for (Entity e : entities) {
            if (e.getType() == entity) {
                move(e, direction);
            }
        }
    }


    public void move(Entity e, char direction) {
        int newX = e.x;
        int newY = e.y;

        switch (direction) {
            case 'w':
                newY--;
                break;
            case 's':
                newY++;
                break;
            case 'a':
                newX--;
                break;
            case 'd':
                newX++;
                break;
            default:
                break;
        }

        if (getEntity(newX, newY) == null && objects[newX][newY] != Objects.WALL) {
            e.x = newX;
            e.y = newY;
        }
        else if(e.getType() == Entities.PLAYER) {
            System.out.print("\nSomething blocks your path.\n");
        }
    }

    public Entity tryDetect(Entity entity, Entities targetType) {
        for (Entity e : entities) {
            if (e.getType() == targetType) {
                int diffX = e.x - entity.x;
                int diffY = e.y - entity.y;
                float distance = (float)Math.sqrt(diffX * diffX + diffY * diffY);

                if (distance <= entity.detection + 0.5f) {
                    return e;
                }
            }
        }

        return null;
    }

    public void command() {
        String command = null;
        Scanner sc = new Scanner(System.in);
        System.out.print("\nWhat will you do?: ");
        command = sc.nextLine();
        if (command.equals("help")) {
            System.out.println("Available commands: \nmove [up, down, left," +
                    " right]\nattack [up, down, left, right]\nuse [item]");
        } else if (command.equals("move up")) {
            moveAll(Entities.PLAYER, 'w');
            draw();
        } else if (command.equals("move down")) {
            System.out.print("\nYou move down!\n");
            moveAll(Entities.PLAYER, 's');
            draw();
        } else if (command.equals("move left")) {
            moveAll(Entities.PLAYER, 'a');
            draw();
        } else if (command.equals("move right")) {
            moveAll(Entities.PLAYER, 'd');
            draw();
        } else if (command.equals("attack up")) {
            attack(Entities.PLAYER, 'w');
            draw();
        } else if (command.equals("attack right")) {
            attack(Entities.PLAYER, 'd');
            draw();
        } else if (command.equals("attack down")) {
            attack(Entities.PLAYER, 's');
            draw();
        } else if (command.equals("attack left")) {
            attack(Entities.PLAYER, 'a');
            draw();
        } else if (command.equals("use")) {
            //todo: create use function.
            System.out.print("\nYou use an item.\n");
        } else {
            System.out.print("\nPlease use a valid command. type 'help' for help with commands.\n");
            command();
        }
    }
}


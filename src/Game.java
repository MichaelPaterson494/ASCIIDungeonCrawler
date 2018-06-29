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
        entities.add(new Entity(Entities.DRAGON, (int) (1 + Math.random() * 15), (int) (1 + Math.random() * 15), 30, 5, 3));

    }

    public void run() {
        while (true) {
            report();
            draw();
            command();
            processEntities();
        }
    }

    private void processEntities(){
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            if(entity.getType() != Entities.PLAYER) {
                Entity attackTarget = tryDetectAttackable(entity, Entities.PLAYER);
                if (attackTarget != null) {
                    int diffX = attackTarget.x - entity.x;
                    if (diffX == -1) {
                        attack(entity, 'a');
                    }
                    if (diffX == 1) {
                        attack(entity, 'd');
                    }

                    int diffY = attackTarget.y - entity.y;
                    if (diffY == 1) {
                        attack(entity, 's');
                    }
                    if (diffY == -1) {
                        attack(entity, 'w');
                    }
                }
                else {
                    Entity chaseTarget = tryDetect(entity, Entities.PLAYER);
                    int moveChance = (int) (Math.random() * 99);
                    if (moveChance > 31) {
                        if (chaseTarget == null) {
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
                            int dirX = chaseTarget.x - entity.x;
                            int dirY = chaseTarget.y - entity.y;

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
    }

    private Entity getEntity(int x, int y) {
        for (Entity e : entities) {
            if (e.x == x && e.y == y) {
                return e;
            }
        }
        return null;
    }

    private Entity getEntity(Entities type) {
        for (Entity e : entities) {
            if (e.getType() == type) {
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
    }

    public void tryAttack(Entity attacker, int modx, int mody) {
        Entity enemy = getEntity(attacker.x + modx, attacker.y + mody);
        if (enemy != null) {
            enemy.health -= attacker.attack;

            System.out.format("%s attacked %s for %d damage.\n",
                              attacker.getType().toString(),
                              enemy.getType().toString(),
                              attacker.attack);

            if (enemy.isDead()) {
                System.out.format("The %s died.\n", enemy.getType().toString());
                if (enemy.getType() == Entities.PLAYER) {
                    System.out.print("Game Over\n");
                    //todo: add close game function.
                }
                entities.remove(enemy);
            }
        }
        else {
            System.out.format("%s swung their sword at thin air...\n", attacker.getType().toString());
        }
    }

    public void report() {
        System.out.println("\nHealth report:");
        Entity player = getEntity(Entities.PLAYER);
        System.out.format("%s: {%s}\n",player.getType().toString(), player.getHealthString());
        Entity eUp = getEntity(player.x, player.y - 1);
        if(eUp != null){
            System.out.format("%s (up): {%s}\n",eUp.getType().toString(), eUp.getHealthString());
        }
        Entity eDown = getEntity(player.x, player.y + 1);
        if(eDown != null){
            System.out.format("%s (down): {%s}\n",eDown.getType().toString(), eDown.getHealthString());
        }
        Entity eLeft = getEntity(player.x - 1, player.y);
        if(eLeft != null){
            System.out.format("%s (left): {%s}\n",eLeft.getType().toString(), eLeft.getHealthString());
        }
        Entity eRight = getEntity(player.x + 1, player.y);
        if(eRight != null){
            System.out.format("%s (right): {%s}\n",eRight.getType().toString(), eRight.getHealthString());
        }
        System.out.print('\n');
    }

    public void attack(Entity e, char direction) {
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
            System.out.print("Something blocks your path.\n");
        }
    }

    public Entity tryDetectAttackable(Entity entity, Entities targetType) {
        Entity up = getEntity(entity.x, entity.y - 1);
        if (up != null && up.type == targetType) {
            return up;
        }
        Entity down = getEntity(entity.x, entity.y + 1);
        if (down != null && down.type == targetType) {
            return down;
        }
        Entity left = getEntity(entity.x - 1, entity.y);
        if (left != null && left.type == targetType) {
            return left;
        }
        Entity right = getEntity(entity.x + 1, entity.y);
        if (right != null && right.type == targetType) {
            return right;
        }
        return null;
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
        Entity player = getEntity(Entities.PLAYER);

        Scanner sc = new Scanner(System.in);
        System.out.print("\nWhat will you do?: ");
        String command = sc.nextLine();
        System.out.print('\n'); // New line to push the reports down by one line.

        if (command.equals("help")) {
            System.out.println("Available commands: \nmove [up, down, left," +
                    " right]\nattack [up, down, left, right]\nuse [item]");
        } else if (command.equals("move up")) {
            move(player, 'w');
        } else if (command.equals("move down")) {
            System.out.print("\nYou move down!\n");
            move(player, 's');
        } else if (command.equals("move left")) {
            move(player, 'a');
        } else if (command.equals("move right")) {
            move(player, 'd');
        } else if (command.equals("attack up")) {
            attack(player, 'w');
        } else if (command.equals("attack right")) {
            attack(player, 'd');
        } else if (command.equals("attack down")) {
            attack(player, 's');
        } else if (command.equals("attack left")) {
            attack(player, 'a');
        } else if (command.equals("use")) {
            //todo: create use function.
            System.out.print("\nYou use an item.\n");
        } else {
            System.out.print("\nPlease use a valid command. type 'help' for help with commands.\n");
            command();
        }
    }
}


import java.util.Arrays;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

class Game {
    private Objects[][] objects = new Objects[17][17];
    ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    private boolean isRunning = true;
    private int currentLevel;

    Game(int level, Entity player) {
        this.currentLevel = level;

        for (Objects[] object : objects) {
            Arrays.fill(object, Objects.FLOOR);
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

        //entities.add(new Entity(Entities.PLAYER, 8, 8, 20, 3, 4));
        if (player == null) {
            player = new Entity(Entities.PLAYER, 8, 8, 20, 5, 4);
        }
        entities.add(player);
        player.addItem(new ItemSharpStone());

        spawnEnemies();

        for (Entity entity : entities) {
            if (entity != player) {
                distributeItems(entity, 0.01f, 1.0f, 5.0f, 10.0f);
            }
        }
    }

    // Returns the game instance to use as the next level.
    // If returns null, the game ends.
    Game run() {
        while (isRunning) {
            spawnTrapDoor();
            report();
            draw();
            command();
            processEntities();

            if (checkTrapDoor()) {
                Entity player = getEntity(Entities.PLAYER);

                distributeItems(player, 1, 10, 30, 100);

                return new Game(currentLevel + 1, player);
            }
        }
        return null;
    }


    private void spawnEnemies() {

        for (int x = 1; x < objects.length - 1; x++) {
            for (int y = 1; y < objects[x].length - 1; y++) {

                Entities type = null;
                float easy = Math.min(1.0f * currentLevel, 15);
                float medium = Math.min(0.25f * currentLevel, 10);
                float hard = Math.min(0.05f * currentLevel, 5);
                float boss = Math.min(0.00025f * currentLevel, 1);
                boolean spawn = true;
                float rng = (float) (Math.random() * 100);
                int health = 0, attack = 0, detection = 0;
                if (rng < boss) {
                    type = Entities.CTHULHU;
                    health = 50;
                    attack = 15;
                    detection = 6;
                } else if (rng < hard) {
                    type = Entities.DRAGON;
                    health = 20;
                    attack = 5;
                    detection = 4;
                } else if (rng < medium) {
                    type = Entities.ZOMBIE;
                    health = 15;
                    attack = 2;
                    detection = 3;
                } else if (rng < easy) {
                    int mobSwitch = (int) (Math.round(Math.random()));
                    switch (mobSwitch) {
                        case 0:
                            type = Entities.GOBLIN;
                            health = 10;
                            attack = 2;
                            detection = 3;
                            break;
                        case 1:
                            type = Entities.BAT;
                            health = 5;
                            attack = 3;
                            detection = 5;
                            break;
                    }
                } else {
                    spawn = false;
                }
                if (spawn) {
                    entities.add(new Entity(type, x, y, health, attack, detection));
                }
            }
        }
    }

    private void processEntities() {
        for (Entity entity : entities) {
            if (entity.getType() != Entities.PLAYER) {
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
                } else {
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
                        } else {
                            int dirX = chaseTarget.x - entity.x;
                            int dirY = chaseTarget.y - entity.y;

                            char direction;
                            if (Math.abs(dirX) > Math.abs(dirY)) {
                                direction = dirX < 0 ? 'a' : 'd';
                            } else {
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

    private void draw() {
        for (int y = 0; y < objects.length; y++) {
            for (int x = 0; x < objects[y].length; x++) {
                Entity entity = getEntity(x, y);
                Item item = getItem(x, y);
                if (entity != null) {
                    System.out.format("%s ", entity.getType().getEntity());
                } else if (item != null) {
                    System.out.format("%s ", item.getIcon());
                } else {
                    Objects currentObject = objects[x][y];
                    System.out.format("%s ", currentObject.getObject());
                }
            }
            System.out.println();
        }
    }

    private void tryAttack(Entity attacker, int modx, int mody) {
        Entity enemy = getEntity(attacker.x + modx, attacker.y + mody);
        if (enemy != null) {
            enemy.health -= attacker.attack;

            System.out.format("%s attacked %s for %d damage.\n",
                    attacker.getType().toString(),
                    enemy.getType().toString(),
                    attacker.attack);

            if (enemy.isDead()) {
                System.out.format("%s died.\n", enemy.getType().toString());

                ArrayList<Item> enemy_items = enemy.getItems();

                boolean death_cancelled = false;
                ListIterator<Item> item_iterator = enemy_items.listIterator();
                while (item_iterator.hasNext()) {
                    Item item = item_iterator.next();
                    if (item.cancelsDeath()) {
                        death_cancelled = true;
                        item.onOwnerDeathCancelled(enemy);
                        item_iterator.remove();
                        break;
                    }
                }

                if (!death_cancelled) {
                    for (Item item : enemy_items) {
                        System.out.format("%s dropped %s.\n", enemy.getType().toString(), item.getName());
                        spawnItem(item, enemy.x, enemy.y);
                    }

                    if (enemy.getType() == Entities.PLAYER) {
                        System.out.print("Game Over\n");
                        isRunning = false;
                    }

                    entities.remove(enemy);
                }
            }
        } else {
            System.out.format("%s swung their sword at thin air...\n", attacker.getType().toString());
        }
    }

    private void report() {
        System.out.println("\nHealth report:");
        Entity player = getEntity(Entities.PLAYER);
        assert player != null;
        System.out.format("%s: {%s}\n", player.getType().toString(), player.getHealthString());
        Entity eUp = getEntity(player.x, player.y - 1);
        if (eUp != null) {
            System.out.format("%s (up): {%s}\n", eUp.getType().toString(), eUp.getHealthString());
        }
        Entity eDown = getEntity(player.x, player.y + 1);
        if (eDown != null) {
            System.out.format("%s (down): {%s}\n", eDown.getType().toString(), eDown.getHealthString());
        }
        Entity eLeft = getEntity(player.x - 1, player.y);
        if (eLeft != null) {
            System.out.format("%s (left): {%s}\n", eLeft.getType().toString(), eLeft.getHealthString());
        }
        Entity eRight = getEntity(player.x + 1, player.y);
        if (eRight != null) {
            System.out.format("%s (right): {%s}\n", eRight.getType().toString(), eRight.getHealthString());
        }
        System.out.print('\n');
    }

    private void attack(Entity e, char direction) {
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

    private void move(Entity e, char direction) {
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

            ArrayList<Item> items = takeItems(newX, newY);
            for (Item item : items) {
                System.out.format("%s picked up %s.\n", e.getType().toString(), item.getName());
                e.addItem(item);
            }
        } else if (e.getType() == Entities.PLAYER) {
            System.out.print("Something blocks your path.\n");
        }
    }

    private Entity tryDetectAttackable(Entity entity, Entities targetType) {
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

    private Entity tryDetect(Entity entity, Entities targetType) {
        for (Entity e : entities) {
            if (e.getType() == targetType) {
                int diffX = e.x - entity.x;
                int diffY = e.y - entity.y;
                float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);

                if (distance <= entity.detection + 0.5f) {
                    return e;
                }
            }
        }

        return null;
    }

    private void command() {
        Entity player = getEntity(Entities.PLAYER);

        Scanner input = new Scanner(System.in);
        System.out.format("\nYou are on level %d what will you do?: ", currentLevel);
        Scanner sc = new Scanner(input.nextLine());
        String command = sc.next();
        System.out.print('\n'); // New line to push the reports down by one line.

        switch (command) {
            case "help":
                System.out.println("Available commands: \nmove [up, down, left," +
                        " right]\nattack [up, down, left, right]\nuse [item]\nkms\nwait\ninv\n. (repeats last)\nstats");
                command();
                break;
            case "move":
                if (sc.hasNext()) {
                    String direction = sc.next();

                    switch (direction) {
                        case "up":
                            assert player != null;
                            move(player, 'w');
                            break;
                        case "down":
                            assert player != null;
                            move(player, 's');
                            break;
                        case "left":
                            assert player != null;
                            move(player, 'a');
                            break;
                        case "right":
                            assert player != null;
                            move(player, 'd');
                            break;
                    }
                } else {
                    System.out.println("You need to enter a direction!");
                    command();
                }
                break;
            case "attack":
                if (sc.hasNext()) {
                    String direction = sc.next();

                    switch (direction) {
                        case "up":
                            attack(player, 'w');
                            break;
                        case "down":
                            attack(player, 's');
                            break;
                        case "left":
                            attack(player, 'a');
                            break;
                        case "right":
                            attack(player, 'd');
                            break;
                    }
                } else {
                    System.out.println("You need to enter a direction!");
                    command();
                }
                break;
            case "use":
                if (sc.hasNextLine()) {
                    String item = sc.nextLine().substring(1);

                    assert player != null;
                    if (player.useItem(item, this)) {
                        System.out.format("you use the %s.\n", item);
                    } else {
                        System.out.format("You don't have %s.\n", item);
                    }
                } else {
                    System.out.println("You need to enter an item name!");
                    command();
                }
                break;
            case "kms":
                System.out.print("You give in to evil and the darkness takes over.\nGame Over.\n");
                assert player != null;
                player.health = -1337;
                isRunning = false;
                break;
            case "wait":
                System.out.print("You stay where you are.\n");
                break;
            case "inv":
                assert player != null;
                if (player.inventory.size() == 0) {
                    System.out.print("Your inventory contains no items...\n");
                } else {
                    System.out.print("Items: \n");
                    for (Item item : player.inventory) {
                        System.out.format("%s\n", item.getName());
                    }
                }
                command();
                break;
            case "stats":
                assert player != null;
                System.out.format("Statistics:\nMax Health: %d      Current Health: %d\nAttack Damage: %d",
                        player.maxHealth, player.health, player.attack);
                break;
            default:
                System.out.print("Please use a valid command. type 'help' for help with commands.\n");
                command();
                break;
        }
    }

    private Item getItem(int x, int y) {
        for (Item i : items) {
            if (i.x == x && i.y == y) {
                return i;
            }
        }
        return null;
    }

    private ArrayList<Item> takeItems(int x, int y) {
        ArrayList<Item> itemsAtPos = new ArrayList<>();

        ListIterator<Item> itemIter = items.listIterator();
        while (itemIter.hasNext()) {
            Item item = itemIter.next();
            if (item.x == x && item.y == y) {
                itemsAtPos.add(item);
                itemIter.remove();
            }
        }

        return itemsAtPos;
    }

    private void spawnItem(Item item, int x, int y) {
        items.add(item);
        item.x = x;
        item.y = y;
    }

    private void spawnTrapDoor() {
        if (entities.size() == 1 && entities.get(0).getType() == Entities.PLAYER) {
            objects[8][8] = Objects.TRAPDOOR;
        }
    }

    private boolean checkTrapDoor() {
        for (Entity e : entities) {
            if (e.getType() == Entities.PLAYER) {
                if (objects[e.x][e.y] == Objects.TRAPDOOR) {
                    return true;
                }
            }
        }
        return false;
    }

    private void distributeItems(Entity entity, float superRare, float rare, float uncommon, float common) {

        float rng = (float) (Math.random() * 100);

        if (rng < superRare) {
            entity.addItem(new ItemEyeOfCthulhu());
        } else if (rng < rare) {
            entity.addItem(new ItemPhilosophersStone());
        } else if (rng < uncommon) {
            entity.addItem(new ItemGlimmeringSharpStone());
        } else if (rng < common) {
            int chooseItem = (int) (Math.round(Math.random()));
            switch (chooseItem) {
                case 0:
                    entity.addItem(new ItemHealthPot());
                case 1:
                    entity.addItem(new ItemSharpStone());
            }
        }
    }
}


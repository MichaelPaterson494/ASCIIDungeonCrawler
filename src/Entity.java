public class Entity {

    Entities type;
    int x, y;
    int health;
    int maxHealth;
    int attack;
    int detection; // Radius in which the entity can see others.

    public Entity(Entities type, int x, int y, int health, int attack, int detection){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.attack = attack;
        this.detection = detection;
        this.maxHealth = health;

    }
    public Entities getType(){
        return this.type;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public String getHealthString() {
        StringBuilder str = new StringBuilder();

        // Print color code.
        if (getType() == Entities.PLAYER) {
            str.append("\u001B[32m"); // Green
        }
        else {
            str.append("\u001B[33m"); // Yellow
        }

        // Print health bar.
        for (int i = 0; i < maxHealth; i++) {
            if (i == health) {
                // We reached the 'health' so the remaining equals signs should be red.
                str.append("\u001B[0m|\u001B[31m");
            }
            str.append("=");
        }

        str.append("\u001B[0m");

        if (health == maxHealth) {
            str.append('|');
        }

        return str.toString();
    }
}

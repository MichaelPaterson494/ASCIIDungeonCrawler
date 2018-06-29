public class Entity {

    Entities type;
    int x, y;
    int health;
    int attack;
    int detection; // Radius in which the entity can see others.

    public Entity(Entities type, int x, int y, int health, int attack, int detection){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.attack = attack;
        this.detection = detection;

    }
    public Entities getType(){
        return this.type;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public String getHealthString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < health; i++) {
            if (getType() == Entities.PLAYER) {
                str.append("\u001B[32m=\u001B[0m");
            } else {
                str.append("\u001B[31m=\u001B[0m");
            }
        }
        return str.toString();
    }
}

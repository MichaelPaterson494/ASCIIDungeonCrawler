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
}

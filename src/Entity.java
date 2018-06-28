public class Entity {

    Entities type;
    int x, y;
    int health;
    int attack;

    public Entity(Entities type, int x, int y, int health, int attack){
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.attack = attack;

    }
    public Entities getType(){
        return this.type;
    }
}

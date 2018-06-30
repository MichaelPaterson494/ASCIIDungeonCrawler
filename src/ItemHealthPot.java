public class ItemHealthPot extends Item {

    @Override
    public String getIcon() {
        return "\u001B[32mh\u001B[0m";
    }

    @Override
    public String getName() {
        return "Health Potion";
    }

    @Override
    public void use(Entity entity, Game game) {
        int heal = 5;
        entity.health += heal;
        if(entity.health > entity.maxHealth){
            entity.health = entity.maxHealth;
        }
        System.out.print("\nYou begin to feel much better after ");
    }
}

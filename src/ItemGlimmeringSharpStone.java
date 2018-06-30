public class ItemGlimmeringSharpStone extends Item {


    @Override
    public String getIcon() {
        return "\033[0;37m$\u001B[0m";
    }

    @Override
    public String getName() {
        return "Glimmering Sharp Stone";
    }


    @Override
    public void use(Entity entity, Game game) {
        int damage = 2;
        entity.attack += damage;
        System.out.print("\nYour sword shines brightly with power as ");
    }
}

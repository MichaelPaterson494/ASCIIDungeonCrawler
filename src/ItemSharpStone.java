public class ItemSharpStone extends Item {
    private int count = 0;

    @Override
    public String getIcon() {
        return "\u001B[36ms\u001B[0m";
    }

    @Override
    public String getName() {
        return "Sharp Stone";
    }


    @Override
    public void use(Entity entity, Game game) {
        count++;
        float breakChance = (float) (Math.random()*100 / count);
        int damage;
        if(breakChance < 5) {
            System.out.print("\nYou damaged your weapon as ");
            damage = -2;

        }
        else {
            damage = 1;
            System.out.print("\nYour sword shines dimly with power as ");
        }
        entity.attack += damage;

    }

}


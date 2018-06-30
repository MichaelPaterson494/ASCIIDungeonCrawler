public class ItemPhilosophersStone extends Item {

    @Override
    public String getIcon() {
        return "\u001B[33mp\u001B[0m";
    }

    @Override
    public String getName() {
        return "Philosophers Stone";
    }

    @Override
    public void onOwnerDeathCancelled(Entity entity){
        System.out.format("%s came back to life by using a Philosophers Stone!\n", entity.getType().toString());
        entity.health = entity.maxHealth/2;
    }

    @Override
    public boolean cancelsDeath() {
        return true;
    }
}

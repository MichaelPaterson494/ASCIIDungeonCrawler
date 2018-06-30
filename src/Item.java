public abstract class Item {
    int x;
    int y;

    public abstract String getIcon();
    public abstract String getName();

    public void use(Entity entity, Game game) {}
    public void onOwnerDeathCancelled(Entity entity) {}

    public boolean cancelsDeath() {
        return false;
    }
}

public class ItemEyeOfCthulhu extends Item {

    @Override
    public String getIcon(){
        return "\u001B[34me\u001B[0m";
    }

    @Override
    public String getName(){
        return "Eye of the Great One";
    }

    @Override
    public void use(Entity entity, Game game) {
        if (entity.y - 1 == 0) {
            System.out.print("\nA wall blocks you from using the eye.");
        }
        else {
            game.entities.add(new Entity(Entities.CTHULHU, entity.x, entity.y - 1, 50, 15, 6));
            System.out.print("\nThe formidable Cthulhu rises as soon as");
        }
    }
}

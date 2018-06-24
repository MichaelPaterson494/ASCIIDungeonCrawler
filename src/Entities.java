public enum Entities {
    //Monsters
    GOBLIN('G'),

    //Characters
    PLAYER('@');

    char entity;

    private Entities(char entity){
        this.entity = entity;
    }

    public char getEntity(){
        return this.entity;
    }
}

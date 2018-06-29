public enum Entities {
    //Monsters
    GOBLIN("\033[0;32mG\033[0m"), BAT("\033[0;31mB\033[0m"), DRAGON("\033[0;31mD\033[0m"),

    //Characters
    PLAYER("@");

    String entity;

    private Entities(String entity){
        this.entity = entity;
    }

    public String getEntity(){
        return this.entity;
    }

    public String toString(){
        switch(this){
            case BAT: return "Bat";
            case PLAYER: return "Hero";
            case GOBLIN: return "Goblin";
            case DRAGON: return "Dragon";
            default: return "Unknown";
        }
    }
}

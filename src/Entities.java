public enum Entities {
    //Monsters
    GOBLIN("\033[0;32mG\033[0m"),
    BAT("\033[0;31mB\033[0m"),
    DRAGON("\033[0;31mD\033[0m"),
    ZOMBIE("\u001B[35mZ\u001B[0m"),
    CTHULHU("\u001B[34mC\u001B[0m"),

    //Characters
    PLAYER("@");

    String entity;

    Entities(String entity){
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
            case ZOMBIE: return "Zombie";
            case CTHULHU: return "God of Destruction";
            default: return "Unknown";
        }
    }
}

public enum Objects {
    WALL("#"), FLOOR(" "), TRAPDOOR("▢");

    String object;

    Objects(String object){
        this.object = object;
    }

    public String getObject(){
        return this.object;
    }
}

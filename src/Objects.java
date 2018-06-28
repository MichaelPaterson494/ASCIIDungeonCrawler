public enum Objects {
    WALL("#"), FLOOR(" ");

    String object;

    private Objects(String object){
        this.object = object;
    }

    public String getObject(){
        return this.object;
    }
}

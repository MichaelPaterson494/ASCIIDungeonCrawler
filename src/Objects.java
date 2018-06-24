public enum Objects {
    WALL('#');

    char object;

    private Objects(char object){
        this.object = object;
    }

    public char getObject(){
        return this.object;
    }
}

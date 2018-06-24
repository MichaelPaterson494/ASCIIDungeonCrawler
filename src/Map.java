import java.util.Arrays;

public class Map {

    Objects[][] objects = new Objects[16][16];

    public Map(){
        for(int i = 0; i < 16; i++) {
            Arrays.fill(objects[i], Objects.WALL);
        }
    }

    public void draw(){
        for(int y = 0; y < 16; y++){
            for(int x = 0; x < 16; x++){
                Objects currentObject = objects[x][y];
                System.out.format("%c ", currentObject.getObject());
            }
            System.out.println();
        }
    }
}

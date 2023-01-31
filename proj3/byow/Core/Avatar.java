package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Avatar {

    int x;
    int y;
    TETile image;
    Rooms b;


    public Avatar(String s, Rooms r) {
        this.b = r;
        if (s.equals("enemy")){
            this.image = Tileset.TREE;
        }
        else{
            this.image = Tileset.AVATAR;
        }
    }

    public Point getPoint(){
        return new Point(this.x, this.y);
    }

    public void putPoint(long seed) {

        Random r = new Random(seed);
        int x = 0;
        int y = 0;
        for (int i = 0; i < b.getWidth() * b.getHeight(); i++) {
            x = r.nextInt(1, b.getWidth());
            y = r.nextInt(0, b.getHeight());
            if (b.world[x][y] == Tileset.FLOOR){
                break;
            }
        }
        this.x = x;
        this.y = y;
        b.world[x][y] = image;
    }

    public void putPoint(int x, int y) {
        this.x = x;
        this.y = y;
        b.world[x][y] = image;
    }

    public boolean equals(Point e){
        if (this.x == e.x && this.y == e.y){
            return true;
        }
        return false;
    }



    public void moveRight(){
        if (canGo(x + 1, y)){
            b.world[x][y] = Tileset.FLOOR;
            x += 1;
            b.world[x][y] = image;
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/retro_arcade_jump_hop_subtle_dull.wav");
        }
        else if (hitWall(x + 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSELOUD("byow/Core/Music/incorrect_001.wav");
        }
        else if (hitEnemy(x + 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/lose_life_fail.wav");
        }
        else if (hitLockedDoor(x + 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/game over (won).wav");
        }
    }

    public void moveLeft(){
        if (canGo(x - 1, y)){
            b.world[x][y] = Tileset.FLOOR;
            x -= 1;
            b.world[x][y] = image;
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/retro_arcade_jump_hop_subtle_dull.wav");
        }
        else if (hitWall(x - 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSELOUD("byow/Core/Music/incorrect_001.wav");
        }
        else if (hitEnemy(x - 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/lose_life_fail.wav");
        }
        else if (hitLockedDoor(x - 1, y)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/game over (won).wav");
        }
    }

    public void moveUp(){
        if (canGo(x, y + 1)){
            b.world[x][y] = Tileset.FLOOR;
            y += 1;
            b.world[x][y] = image;
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/retro_arcade_jump_hop_subtle_dull.wav");
        }
        else if (hitWall(x, y + 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSELOUD("byow/Core/Music/incorrect_001.wav");
        }
        else if (hitEnemy(x, y + 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/lose_life_fail.wav");
        }
        else if (hitLockedDoor(x, y + 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/game over (won).wav");
        }
    }

    public void moveDown(){
        if (canGo(x, y - 1)){
            b.world[x][y] = Tileset.FLOOR;
            y -= 1;
            b.world[x][y] = image;
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/retro_arcade_jump_hop_subtle_dull.wav");
        }
        else if (hitWall(x, y - 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSELOUD("byow/Core/Music/incorrect_001.wav");
        }
        else if (hitEnemy(x, y - 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/lose_life_fail.wav");
        }
        else if (hitLockedDoor(x, y - 1)){
            PlayMusic p = new PlayMusic();
            p.PlayMusicSE("byow/Core/Music/game over (won).wav");
        }
    }

    public boolean canGo(int x, int y){
        if (y < 0 || y >= b.getHeight()){
            return false;
        }
        if (x < 0 || x >= b.getWidth()){
            return false;
        }
        if (b.world[x][y] == Tileset.WALL || b.world[x][y] == Tileset.TREE){
            return false;
        }
        return true;
    }

    public boolean hitWall(int x, int y) {
        if (b.world[x][y] == Tileset.WALL){
            return true;
        }
        return false;
    }


    public boolean hitEnemy(int x, int y) {
        if (b.world[x][y] == Tileset.TREE){
            return true;
        }
        return false;
    }

    public boolean hitLockedDoor(int x, int y) {
        if (b.world[x][y] == Tileset.LOCKED_DOOR){
            return true;
        }
        return false;
    }

    public void moveAlong(){

        if (canGo(x + 1, y) && b.world[x + 1][y] == Tileset.MOUNTAIN){
            moveRight();
        }
        else if (canGo(x - 1, y) && b.world[x - 1][y] == Tileset.MOUNTAIN){
            moveLeft();
        }
        else if (canGo(x, y + 1) && b.world[x][y + 1] == Tileset.MOUNTAIN){
            moveUp();
        }
        else if (canGo(x, y - 1) && b.world[x][y - 1] == Tileset.MOUNTAIN){
            moveDown();
        }
    }

    public void goToPoint(Point a){
        if (a == null){
            return;
        }
        b.world[x][y] = Tileset.FLOOR;
        b.world[a.x][a.y] = image;
        x = a.x;
        y = a.y;
    }

    public Point findPoint(){
        if (canGo(x + 1, y) && b.world[x + 1][y] == Tileset.MOUNTAIN){
            return new Point(x + 1, y);
        }
        else if (canGo(x - 1, y) && b.world[x - 1][y] == Tileset.MOUNTAIN){
            return new Point(x - 1, y);
        }
        else if (canGo(x, y + 1) && b.world[x][y + 1] == Tileset.MOUNTAIN){
            return new Point(x, y + 1);
        }
        else if (canGo(x, y - 1) && b.world[x][y - 1] == Tileset.MOUNTAIN){
            return new Point(x, y - 1);
        }
        return null;
    }
}

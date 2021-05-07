package population.tiles;

import javafx.scene.image.Image;
import points2d.Vec2df;
import points2d.Vec2di;
import population.AbstractPopulation;
import population.AbstractPopulationObject;
import population.SimpleUpdateable;
import population.animals.Animal;

import java.util.Random;

public class TileField extends AbstractPopulation<Tile> implements SimpleUpdateable {

    private Vec2di tileFieldSize;

    private Vec2df tileSize;

    public TileField(Vec2di tileFieldSize, double width, double height, Image image) {
        super(image);
        this.tileFieldSize = tileFieldSize;
        tileSize = new Vec2df((float)(width / tileFieldSize.getX()), (float)(height / tileFieldSize.getY()));
    }

    private Tile getTile(int x, int y) {
        try {
            return population.get(y * tileFieldSize.getX() + x);
        } catch ( IndexOutOfBoundsException e ) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Tile getTile(Vec2di pos) {
        return getTile(pos.getX(), pos.getY());
    }

    private Vec2di getTilePos(int index, Vec2di fieldTileSize) {
        int i = index;
        int x;
        int y = 0;
        while ( i >= fieldTileSize.getX() ) {
            i -= fieldTileSize.getX();
            y++;
        }
        x = i;
        return new Vec2di(x, y);
    }

    private Tile getTileUp(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(-1);
        if ( pos.getY() >= 0 ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileLeft(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToX(-1);
        if ( pos.getX() >= 0 ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileDown(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(1);
        if ( pos.getY() < tileFieldSize.getY() ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileRight(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToX(1);
        if ( pos.getX() < tileFieldSize.getX() ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileUpLeft(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(-1);
        pos.addToX(-1);
        if ( pos.getX() >= 0 && pos.getY() >= 0 ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileDownLeft(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(1);
        pos.addToX(-1);
        if ( pos.getX() >= 0 && pos.getY() < tileFieldSize.getY() ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileDownRight(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(1);
        pos.addToX(1);
        if ( pos.getX() < tileFieldSize.getX() && pos.getY() < tileFieldSize.getY() ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getTileUpRight(int index) {
        Vec2di pos = getTilePos(index, tileFieldSize);
        pos.addToY(-1);
        pos.addToX(1);
        if ( pos.getX() < tileFieldSize.getX() && pos.getY() >= 0 ) {
            return getTile(pos);
        } else {
            return null;
        }
    }

    private Tile getAdjacentTile(int opt, int index) {
        switch ( opt ) {
            case 0: default:
                return null;
            case 1:
                return getTileUp(index);
            case 2:
                return getTileLeft(index);
            case 3:
                return getTileDown(index);
            case 4:
                return getTileRight(index);
            case 5:
                return getTileUpLeft(index);
            case 6:
                return getTileDownLeft(index);
            case 7:
                return getTileDownRight(index);
            case 8:
                return getTileUpRight(index);
        }
    }

    @Override
    public float getEnergyFromEatenObjects(AbstractPopulationObject o) {
        if ( o instanceof Animal ) {
            int numOfEatenTiles = getNumEatenTilesByAnimal((Animal)o);
            return numOfEatenTiles * 0.1f;
        } else {
            return 0;
        }
    }

    private int getNumEatenTilesByAnimal(Animal a) {
        int numEatenTiles = 0;
        Vec2df fieldPos = new Vec2df(a.getPos());
        fieldPos.add(a.getSize() / 2);
        int firstTileIndex = 0;
        for ( Tile t : population) {
            if ( t.pointInside(fieldPos) ) {
                firstTileIndex = t.getId();
                numEatenTiles++;
                break;
            }
        }
        for ( float y = a.getPos().getY(); y <= a.getPos().getY() + a.getSize(); y += tileSize.getY() ) {
            for ( float x = a.getPos().getX(); x <= a.getPos().getX() + a.getSize(); x += tileSize.getX() ) {
                fieldPos.addToX(tileSize.getX());
                if ( firstTileIndex >= population.size() -1 ) {
                    break;
                }
                firstTileIndex++;
                if ( population.get(firstTileIndex).pointInside(fieldPos) ) {
                    if ( population.get(firstTileIndex).isLive() ) {
                        numEatenTiles++;
                        population.get(firstTileIndex).setLive(false);
                        population.get(firstTileIndex).setTimeLiving(0);
                        population.get(firstTileIndex).setAccumulatedTime(0);
                    }
                } else {
                    break;
                }
            }
            fieldPos.addToY(tileSize.getY());
            fieldPos.setX(a.getPos().getX());
            while ( firstTileIndex <= population.size() -1 && population.get(firstTileIndex).pointInside(fieldPos) ) {
                firstTileIndex++;
            }
            if ( firstTileIndex >= population.size() -1 ) {
                break;
            }
        }
        return numEatenTiles;
        //return drawFillCircle((int)a.getPos().getX(), (int)a.getPos().getY(), (int)a.getSize());
    }

    private int drawLineForFillCircle(int sx, int ex, int ny) {
        int count = 0;
        for (int i = sx; i <= ex; i++) {
            //setPixel(i, ny, color);
            Tile t = getTile(i, ny);
            if ( t != null && t.isLive() ) {
                count++;
                t.setLive(false);
                t.setTimeLiving(0);
                t.setAccumulatedTime(0);
            }
        }
        return count;
    }

    public int drawFillCircle(int x, int y, int radius) {
        int count = 0;

        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2 * radius;
        if ( radius == 0 ) {
            return 0;
        }

        while ( y0 >= x0 ) {
            // Modified to draw scan-lines instead of edges
            count += drawLineForFillCircle(x - x0, x + x0, y - y0);
            count += drawLineForFillCircle(x - y0, x + y0, y - x0);
            count += drawLineForFillCircle(x - x0, x + x0, y + y0);
            count += drawLineForFillCircle(x - y0, x + y0, y + x0);
            if (d < 0) d += 4 * x0++ + 6;
            else d += 4 * (x0++ - y0--) + 10;
        }

        return count;
    }

    @Override
    public void populate(int numObjects) {
        population.clear();
        Vec2di tilePos;
        for ( int i = 0; i < numObjects; i++ ) {
            tilePos = getTilePos(i, tileFieldSize);
            population.add(new Tile(i,
                    new Vec2df(
                            tilePos.getX() * tileSize.getX(),
                            tilePos.getY() * tileSize.getY()
                    ),
                    tileSize,
                    rand.nextInt(4) == 1));
        }
    }

    public void populate() { // Vec2df tileSize
        populate(tileFieldSize.getX() * tileFieldSize.getY());
    }

    @Override
    public void update(float elapsedTime) {
        for ( Tile t : population ) {
            if ( t.isLive() ) {
                t.addTimeLiving(1 * elapsedTime);
                t.addAccumulatedTime(1 * elapsedTime);

                if ( t.getTimeLiving() >= Tile.getMaxTimeLiving() ) {
                    t.setTimeLiving(0);
                    t.setLive(false);
                    t.setAccumulatedTime(0);
                }

                reproduce(t);
            }
        }
        text.set("" + getLiveTiles());
    }

    private void reproduce(Tile t) {
        int direction;
        boolean infect;
        Tile t2;
        if ( t.getAccumulatedTime() >= Tile.getReproduceTime() ) {
            t.setAccumulatedTime(0);

            infect = rand.nextBoolean();
            if ( infect ) {
                direction = rand.nextInt(9) + 1;
                t2 = getAdjacentTile(direction, t.getId());
                if ( t2 != null && !t2.isLive() ) {
                    t2.setLive(true);
                }
            }
        }
    }

    public int getLiveTiles() {
        int live = 0;
        for ( Tile t : population) {
            if ( t.isLive() ) {
                live++;
            }
        }
        return live;
    }

    public int getDeadTiles() {
        return tileFieldSize.getX() * tileFieldSize.getY() - getLiveTiles();
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Vec2df getTileSize() {
        return tileSize;
    }

    public Vec2di getTileFieldSize() {
        return tileFieldSize;
    }

}

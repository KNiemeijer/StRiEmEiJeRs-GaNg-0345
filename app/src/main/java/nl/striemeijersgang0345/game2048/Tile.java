package nl.striemeijersgang0345.game2048;

class Tile extends Cell {
    private final int value;
    private Tile[] mergedFrom = null;

    Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
    }

    void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    int getValue() {
        return this.value;
    }

    Tile[] getMergedFrom() {
        return mergedFrom;
    }

    void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }
}

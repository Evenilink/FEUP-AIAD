package algorithms.pledge;

import repast.simphony.space.grid.GridPoint;

public class NeighbourPoints {
    public enum Direction {
        UPWARDS,
        DOWNWARDS,
        LEFT,
        RIGHT
    }

    private GridPoint frontPoint;
    private GridPoint backPoint;
    private GridPoint leftPoint;
    private GridPoint rightPoint;

    private void NeighbourPoints(GridPoint centralPoint, Direction direction, int offset) {
        if (direction == Direction.RIGHT) {
            // Facing Right
            frontPoint = new GridPoint(centralPoint.getX() + offset, centralPoint.getY());
            backPoint = new GridPoint(centralPoint.getX() - offset, centralPoint.getY());
            leftPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() + offset);
            rightPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() - offset);
        } else if (direction == Direction.LEFT) {
            // Facing Left
            frontPoint = new GridPoint(centralPoint.getX() - offset, centralPoint.getY());
            backPoint = new GridPoint(centralPoint.getX() + offset, centralPoint.getY());
            leftPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() - offset);
            rightPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() + offset);
        } else if (direction == Direction.UPWARDS) {
            // Facing Upwards
            frontPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() + offset);
            backPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() - offset);
            leftPoint = new GridPoint(centralPoint.getX() - offset, centralPoint.getY());
            rightPoint = new GridPoint(centralPoint.getX() + offset, centralPoint.getY());
        } else if (direction == Direction.DOWNWARDS) {
            // Facing Downwards
            frontPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() - offset);
            backPoint = new GridPoint(centralPoint.getX(), centralPoint.getY() + offset);
            leftPoint = new GridPoint(centralPoint.getX() + offset, centralPoint.getY());
            rightPoint = new GridPoint(centralPoint.getX() - offset, centralPoint.getY());
        }
    }

    public NeighbourPoints(GridPoint frontPoint, GridPoint backPoint, GridPoint leftPoint, GridPoint rightPoint) {
        this.frontPoint = frontPoint;
        this.backPoint = backPoint;
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
    }

    public NeighbourPoints(GridPoint centralPoint, Direction direction, int offset) {
        this.NeighbourPoints(centralPoint, direction, offset);
    }

    public NeighbourPoints(GridPoint centralPoint, Direction direction) {
        this.NeighbourPoints(centralPoint, direction, 1);
    }


    public GridPoint front() {
        return frontPoint;
    }

    public void front(GridPoint frontPoint) {
        this.frontPoint = frontPoint;
    }

    public GridPoint back() {
        return backPoint;
    }

    public void back(GridPoint backPoint) {
        this.backPoint = backPoint;
    }

    public GridPoint left() {
        return leftPoint;
    }

    public void left(GridPoint leftPoint) {
        this.leftPoint = leftPoint;
    }

    public GridPoint right() {
        return rightPoint;
    }

    public void right(GridPoint rightPoint) {
        this.rightPoint = rightPoint;
    }

    public int frontX() {
        return frontPoint.getX();
    }

    public int frontY() {
        return frontPoint.getY();
    }

    public int backX() {
        return backPoint.getX();
    }

    public int backY() {
        return backPoint.getY();
    }

    public int leftX() {
        return leftPoint.getX();
    }

    public int leftY() {
        return leftPoint.getY();
    }

    public int rightX() {
        return rightPoint.getX();
    }

    public int rightY() {
        return rightPoint.getY();
    }
}
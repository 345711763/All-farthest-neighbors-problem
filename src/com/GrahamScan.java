package com;

import java.awt.Point;
import java.util.*;

public class GrahamScan {

    private enum Turn { RIGHT_TURN, LEFT_TURN, COLLINEAR }
    private Turn turnTest(Point b, Point q, Point r) {

        double delta = (q.x - b.x) * (r.y - b.y) -(q.y - b.y) * (r.x - b.x);

        if(delta > 0) {
            return Turn.LEFT_TURN;
        }
        if(delta < 0) {
            return Turn.RIGHT_TURN;
        }
        if(delta ==0){
            return Turn.COLLINEAR;
        }
        return null;
    }
    private boolean checkCollinear(List<Point> pointSet) {

        if(pointSet.size() < 2) {
            return true;
        }

        Point a = pointSet.get(0);
        Point b = pointSet.get(1);

        for(int i = 2; i < pointSet.size(); i++) {

            Point c = pointSet.get(i);

            if(turnTest(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    public List<Point> computeConvexHull(List<Point> pointSet) throws IllegalArgumentException {

        List<Point> sortedPointSet = new ArrayList<Point>(sortPointSet(pointSet));

        if(sortedPointSet.size()<3) {
            throw new IllegalArgumentException("need more points");
        }

        if(checkCollinear(sortedPointSet)) {
            throw new IllegalArgumentException("all points are collinear");
        }

        Stack<Point> stack = new Stack<Point>();
        stack.push(sortedPointSet.get(0));
        stack.push(sortedPointSet.get(1));

        for (int i = 2; i < sortedPointSet.size(); i++) {
            Point q = stack.pop();
            Point b = stack.peek();
            Point r = sortedPointSet.get(i);

            switch(turnTest(b, q, r)) {
                case LEFT_TURN:
                    stack.push(q);
                    stack.push(r);
                    break;
                case RIGHT_TURN:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(r);
                    break;
            }
        }
        stack.push(sortedPointSet.get(0));
        return new ArrayList<Point>(stack);
    }

    private Set<Point> sortPointSet(List<Point> pointSet) {
        Point temp=pointSet.get(0);
        for(int i = 1; i < pointSet.size(); i++) {
            if(pointSet.get(i).y < temp.y || (pointSet.get(i).y == temp.y && pointSet.get(i).x < temp.x)) {
                temp = pointSet.get(i);
            }
        }
        final Point lowestPoint = temp;
       TreeSet<Point> treeSet = new TreeSet<Point>(new Comparator<Point>() {
            public int compare(Point p1, Point p2) {

                if(p1 == p2 || p1.equals(p2)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double angle1 = Math.atan2(p1.y - lowestPoint.y, p1.x - lowestPoint.x);
                double angle2 = Math.atan2(p2.y - lowestPoint.y, p2.x - lowestPoint.x);

                if(angle1 < angle2) {
                    return -1;
                }
                else if(angle1 > angle2) {
                    return 1;
                }
                else {
                    double distance1 = Math.sqrt((lowestPoint.x - p1.x) * (lowestPoint.x - p1.x)) +
                                                ((lowestPoint.y - p1.y) * (lowestPoint.y - p1.y));
                    double distance2 = Math.sqrt(((lowestPoint.x - p2.x) * (lowestPoint.x - p2.x)) +
                                                ((lowestPoint.y - p2.y) * (lowestPoint.y - p2.y)));

                    if(distance1 < distance2) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });
;
        treeSet.addAll(pointSet);

        return treeSet;
    }

 
}
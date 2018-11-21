
/*
    Name: Max Staples
    Assignment: QuickHull
    Course: CS371
    Semester: Fall 2018
    Date: 11/11/2018
    Sources Consulted: None
    Known Bugs: None
    Program Instructions: The main method in this program illustrates
    the steps that should be taken in order to perform the necessary
    requirements of the program.
        1. Instantiate QuickHull instance
        2. Read the file
        3. Call convexHull to get the Hull and print it to result.txt
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class QuickHull {

    private ArrayList<Point> allPoints;
    private ArrayList<Point> convexHull;

    /**
     * Constructor
     * Initializes allPoints which hold all points read in from
     * the file and convexHull which is holds all points of the
     * Convex Hull
     */
    public QuickHull() {
        allPoints =new ArrayList<>();
        convexHull = new ArrayList<>();
    }

    /**
     * Writes the convex hull to a file result.txt
     * @throws FileNotFoundException
     *
     */
    public void writeConvexHull() throws FileNotFoundException {
        Collections.sort(convexHull,new PointCompare());
        PrintWriter writer = new PrintWriter("results.txt");
        for(int i = 0; i < convexHull.size(); i++){
            writer.println(convexHull.get(i).toString());
            System.out.println(convexHull.get(i).toString());
        }
        writer.close();
    }

    /**
     * getter method to return all points read from file
     * @return allPoints ArrayList containing all points
     */
    public ArrayList<Point> getAllPoints() {
        return allPoints;
    }

    /**
     * Wrapper method to perform quickhull search; calls findUpperHull on upper and lower hull
     * prints to terminal and saves to file result.txt
     * @throws FileNotFoundException
     */
    public void convexHull() throws FileNotFoundException {
        ArrayList<QuickHull.Point> allPoints = getAllPoints();
        findUpperHull(allPoints.get(0), allPoints.get(allPoints.size()-1), allPoints);
        findUpperHull(allPoints.get(allPoints.size()-1), allPoints.get(0), allPoints);
        writeConvexHull();
    }

    /**
     * Method to read in points from file data.txt. Upon finishing
     * reading in points the points are sorted
     * @param filename the name of the file containing points
     * @return boolean true if successful false otherwise
     * @throws FileNotFoundException
     */
    public boolean readFile(String filename) throws FileNotFoundException {
        File input = new File(filename);
        if (!input.exists())
            return false;
        Scanner inFile = new Scanner(input);
        int size = inFile.nextInt();
        inFile.nextLine();
        while (inFile.hasNextLine()) {
            String curLine = inFile.nextLine();
            Scanner lineScanner = new Scanner(curLine);
            double x = lineScanner.nextDouble();
            double y = lineScanner.nextDouble();
            allPoints.add(new Point(x, y));
        }
        Collections.sort(allPoints, new PointCompare());
        return true;
    }


    /**
     * Recursive method to find convex hull
     * @param p1 first point in ordered ArrayList allPoints
     * @param pn last point in ordered ArrayList allPoints
     * @param s ArrayList of allPoints
     */
    public void findUpperHull(Point p1, Point pn, ArrayList<Point> s){
        if(s.isEmpty()){
            if(convexHull.contains(p1) && convexHull.contains(pn))
                return;
            else if(convexHull.contains(p1) && !convexHull.contains(pn))
                convexHull.add(pn);
            else if(convexHull.contains(pn) && !convexHull.contains(p1))
                convexHull.add(p1);
            else{
                convexHull.add(p1);
                convexHull.add(pn);
            }
        }
        else{
            int maxindex = findMaxPt(s, p1, pn);
            ArrayList<Point> s1 = findS(s, p1, s.get(maxindex));
            ArrayList<Point> s2 = findS(s, s.get(maxindex), pn);
            findUpperHull(p1, s.get(maxindex), s1);
            findUpperHull(s.get(maxindex), pn, s2);
        }
    }

    /**
     * finds s according to the current line being looked at
     * @param s ArrayList of set of interest
     * @param p1 initial point
     * @param p2 final point
     * @return ArrayList of points above the line p1p2
     */
    private ArrayList<Point> findS(ArrayList<Point> s, Point p1, Point p2) {
        ArrayList<Point> s1 = new ArrayList<>();
        for(int i = 1; i < s.size(); i++){
            if(p1.x*p2.y + s.get(i).x*p1.y + p2.x*s.get(i).y - s.get(i).x*p2.y - p2.x*p1.y - p1.x*s.get(i).y > 0) {
                s1.add(s.get(i));
            }
        }
        return s1;
    }

    /**
     * Finds max point between p1 and p2
     * @param s set of points to look through
     * @param p1 first point in the line
     * @param p2 second point in the line
     * @return index of maxPt above line p1p2
     */
    private int findMaxPt(ArrayList<Point> s, Point p1, Point p2) {
        double maxPt = 0;
        int maxIndex = 0;
        for(int i = 1; i < s.size()-2; i++){
            if(p1.x*p2.y + s.get(i).x*p1.y + p2.x*s.get(i).y - s.get(i).x*p2.y - p2.x*p1.y - p1.x*s.get(i).y > maxPt) {
                maxPt = p1.x*p2.y + s.get(i).x*p1.y + p2.x*s.get(i).y - s.get(i).x*p2.y - p2.x*p1.y - p1.x*s.get(i).y;
                maxIndex = i;
            }
        }
    return maxIndex;
    }


    /**
     * nested point class
     */
    static class Point{
        public double x;
        public double y;

        /**
         * Constructor for point class
         * @param x x coordinate
         * @param y y coordinate
         */
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }

        /**
         * @return formatted point ex: (1.000,1.000)
         */
        public String toString(){

            return String.format("(%.3f,%.3f)",this.x,this.y);
        }
    }
}

/**
 * nested Point compare class for collections.sort method
 */
    class PointCompare implements Comparator<QuickHull.Point>{

        @Override
        public int compare(QuickHull.Point p1, QuickHull.Point p2){
            //if both x and y coordinates are equal
            if(p1.x== p2.x && p1.y==p2.y)
                return 0;
                //if x coordinate less or x coordinate equal, but
                //y coordinate is less (tie break)
            else if(p1.x<p2.x || p1.x==p2.x && p1.y<p2.y)
                return -1;
            else
                return 1;
        }
    }

class QuickHullDemo{
    public static void main(String[] args) throws FileNotFoundException {
        QuickHull quickHull = new QuickHull();
        quickHull.readFile("data.txt");
        quickHull.convexHull();


    }
}

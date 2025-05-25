import edu. princeton. cs. algs4.Picture;
import edu. princeton. cs. algs4. Stack;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;

import static java.lang.Math.pow;

public class SeamCarver {
    private Picture thePicture;
    public SeamCarver(Picture picture) {
        this.thePicture  = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(this.thePicture);
    }
    // width of current picture
    public int width() {
        return thePicture.width();
    }
    // height of current picture
    public int height() {
        return thePicture.height();
    }

    private int colToLeft(int currentCol) {
        if(currentCol == 0){
            return this.width() - 1;
        }
        else if (currentCol <= this.width() - 1 && currentCol > 0){
            return currentCol - 1;
        }
        throw new IndexOutOfBoundsException();
    }

    private int colToRight(int currentCol) {
        if (currentCol == this.width() - 1) {
            return 0;
        }
        else if (currentCol < this.width() - 1 && currentCol >= 0){
            return currentCol + 1;
        }
        throw new IndexOutOfBoundsException();
    }

    private int rowToUp(int currentRow){
        if(currentRow == 0){
            return this.height() - 1;
        }
        else if (currentRow <=this.height() - 1 && currentRow > 0){
            return currentRow - 1;
        }
        throw new IndexOutOfBoundsException();
    }

    private int rowToDown(int currentRow){
        if(currentRow == this.height() - 1){
            return 0;
        }
        else if (currentRow <this.height() - 1 && currentRow >= 0){
            return currentRow + 1;
        }
        throw new IndexOutOfBoundsException();
    }

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {
        Color upRGB = thePicture.get(x, rowToUp(y));
        Color downRGB = thePicture.get(x, rowToDown(y));
        Color leftRGB = thePicture.get(colToLeft(x), y);
        Color rightRGB = thePicture.get(colToRight(x), y);

        double ret = 0;

        ret += pow(upRGB.getRed() - downRGB.getRed(), 2);
        ret += pow(upRGB.getBlue() - downRGB.getBlue(), 2);
        ret += pow(upRGB.getGreen() - downRGB.getGreen(), 2);

        ret += pow(leftRGB.getRed() - rightRGB.getRed(), 2);
        ret += pow(leftRGB.getBlue() - rightRGB.getBlue(), 2);
        ret += pow(leftRGB.getGreen() - rightRGB.getGreen(), 2);

        return ret;
    }
    //sequence of indices for horizontal seam
    public  int[] findHorizontalSeam() {
        Picture pT = new Picture(thePicture.height(), thePicture.width());
        //Transpose the picture, then perform vertical operation
        for (int i = 0; i < thePicture.height(); i += 1){
            for (int j = 0; j < thePicture.width(); j += 1){
                pT.set(i, j, thePicture.get(j, i));
            }
        }

        SeamCarver scT = new SeamCarver(pT);
        return scT.findVerticalSeam();
    }
    // sequence of indices for vertical seam

    private class NodeForfVS {
        public int x;
        public int y;
        public double totalEnergy;
        public int[] waysToHere = new int[height()];

        public NodeForfVS(int x, int y, double totalEnergy){
            this.x = x;
            this.y = y;
            this.totalEnergy = totalEnergy;
        }
    }

    public int[] findVerticalSeam() {
         return fVS_DP();
    }
    /**----------------------------The trial to implement the fVS in Dynamic Programming--------------------------------**/
    /**The relationship(only \ or | or /) and the set of nodes are easy to interpreted in the 2-D array, so we can use
     * the dynamic programming**/
    /*Dynamic Programming is so great that there is no need to rebuild a map, so something that represent the map,
    * using stack,priority queue or queue as the fringe. The disadvantages of these graph algorithms is the overuse of the
    * space when build the fringe, which dynamic programming can avoid.
     */

    private int[] fVS_DP(){
        //The minimum cost to go to the node from the top of the array
        double [][] M = new double[width()][height()];
        //The father of each pixel, the higher is father, the lower is son
        int [][] father = new int[width()][height()];

        /*We start to treat the highest layer of the node(equivalent to "have searched the destination" , a.k.a the
        ** end condition in the graph algorithm*/
        for (int i = 0; i < width(); i += 1){
            M[i][0] = energy(i, 0);
            father[i][0] = -1;
        }

        /*from the second layer, we set the M[i][j] = energy(i, j) + min(M[i-1][j-1], M[i][j-1], M[i+1][j-1]), if they
        * exists*/
        for (int j = 1; j < height(); j += 1){
            for (int i = 0; i < width(); i += 1){
                M[i][j] = energy(i, j) + M[i][j - 1];
                father[i][j] = i;

                //If the left father exist
                if(i > 0 && M[i - 1][j - 1] < M[i][j - 1]){
                    M[i][j] = energy(i, j) + M[i - 1][j - 1];
                    father[i][j] = i - 1;
                }
                //If the right father exist
                if(i < width() - 1 && (M[i + 1][j - 1] < M[i][j - 1]) &&
                        (i == 0 ||(M[i + 1][j - 1] < M[i - 1][j - 1]))){
                    M[i][j] = energy(i, j) + M[i + 1][j - 1];
                    father[i][j] = i + 1;
                }
            }
        }

        double minEnergy = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < width(); i += 1){
            if(M[i][height() - 1] < minEnergy){
                minEnergy = M[i][height() - 1];
                minIndex = i;
            }
        }

        int [] ret = new int[height()];
        ret[height() - 1] = minIndex;
        for (int i = height() - 1; i > 0; i -= 1){
            ret[i - 1] = father[minIndex][i];
            minIndex = father[minIndex][i];
        }
        return ret;
    }
    /**----------------------------The trial to implement the fVS in Dynamic Programming--------------------------------**/
    /*----------------------------The trial to implement the fVS in an iterative way----------------------------------*/
    private NodeForfVS fVS_iterativeHelper(int x){
        //the root of the tree is at (x, height() - 1)
        NodeForfVS root = new NodeForfVS(x, height() - 1, energy(x, height() - 1));
        root.waysToHere[height() - 1] = x;

        Stack<NodeForfVS> fringe = new Stack<>();
        fringe.push(root);

        double minEnergy = Double.MAX_VALUE;
        //Initialize the node to return
        NodeForfVS retNode = new NodeForfVS(0,0,0);

        while (!fringe.isEmpty()){
            NodeForfVS currentNode = fringe.pop();

            // 剪枝：当前路径能量已超过已知最小值
            if (currentNode.totalEnergy >= minEnergy) {
                continue;
            }

            if(currentNode.y == 0){
                if(currentNode.totalEnergy < minEnergy){
                    minEnergy = currentNode.totalEnergy;
                    retNode = currentNode;
                }
            }
            else{
                NodeForfVS newNode1 = new NodeForfVS(currentNode.x, currentNode.y - 1,
                        currentNode.totalEnergy + energy(currentNode.x, currentNode.y - 1));
                newNode1.waysToHere = currentNode.waysToHere.clone();
                newNode1.waysToHere[currentNode.y - 1] = currentNode.x;
                fringe.push(newNode1);
                if (currentNode.x > 0){
                    NodeForfVS newNode2 = new NodeForfVS(currentNode.x - 1, currentNode.y - 1,
                            currentNode.totalEnergy + energy(currentNode.x - 1, currentNode.y - 1));
                    newNode2.waysToHere = currentNode.waysToHere.clone();
                    newNode2.waysToHere[currentNode.y - 1] = currentNode.x - 1;
                    fringe.push(newNode2);
                }
                if (currentNode.x < width() - 1){
                    NodeForfVS newNode3 = new NodeForfVS(currentNode.x + 1, currentNode.y - 1,
                            currentNode.totalEnergy + energy(currentNode.x + 1, currentNode.y - 1));
                    newNode3.waysToHere = currentNode.waysToHere.clone();
                    newNode3.waysToHere[currentNode.y - 1] = currentNode.x + 1;
                    fringe.push(newNode3);
                }
            }
        }
        return retNode;
    }

    private int[] fVS_iterative(){
        int [] ret = new int[height()];
        double minEnergy = Double.MAX_VALUE;

        for(int i = 0; i < width(); i += 1){
            NodeForfVS end = fVS_iterativeHelper(i);
            if(end.totalEnergy < minEnergy){
                ret = end.waysToHere;
                minEnergy = end.totalEnergy;
            }
        }
        return ret;
    }
    /*----------------------------The end of trial to implement the fVS in an iterative way----------------------------*/
    /*----------------------------The trial to implement the fVS in a Uniform-cost search way-------------------------*/
    private NodeForfVS fVS_UCSHelper(int x){
        //the root of the tree is at (x, height() - 1)
        NodeForfVS root = new NodeForfVS(x, height() - 1, energy(x, height() - 1));
        root.waysToHere[height() - 1] = x;

        Comparator<NodeForfVS> newCompare = new Comparator<NodeForfVS>() {
            @Override
            public int compare(NodeForfVS o1, NodeForfVS o2) {
                double difference = o1.totalEnergy - o2.totalEnergy;
                if(difference > 0){
                    return 1;
                }
                else if(difference < 1e-6 || difference > 1e-6)
                    return 0;
                else return -1;
            }
        };

        PriorityQueue<NodeForfVS> fringe = new PriorityQueue<>(newCompare);
        fringe.add(root);

        double minEnergy = Double.MAX_VALUE;
        //Initialize the node to return
        NodeForfVS retNode = new NodeForfVS(0,0,0);

        while (!fringe.isEmpty()){
            NodeForfVS currentNode = fringe.poll();

            // 剪枝：当前路径能量已超过已知最小值
            if (currentNode.totalEnergy >= minEnergy) {
                continue;
            }

            if(currentNode.y == 0){
                if(currentNode.totalEnergy < minEnergy){
                    minEnergy = currentNode.totalEnergy;
                    retNode = currentNode;
                }
            }
            else{
                NodeForfVS newNode1 = new NodeForfVS(currentNode.x, currentNode.y - 1,
                        currentNode.totalEnergy + energy(currentNode.x, currentNode.y - 1));
                newNode1.waysToHere = currentNode.waysToHere.clone();
                newNode1.waysToHere[currentNode.y - 1] = currentNode.x;
                fringe.add(newNode1);
                if (currentNode.x > 0){
                    NodeForfVS newNode2 = new NodeForfVS(currentNode.x - 1, currentNode.y - 1,
                            currentNode.totalEnergy + energy(currentNode.x - 1, currentNode.y - 1));
                    newNode2.waysToHere = currentNode.waysToHere.clone();
                    newNode2.waysToHere[currentNode.y - 1] = currentNode.x - 1;
                    fringe.add(newNode2);
                }
                if (currentNode.x < width() - 1){
                    NodeForfVS newNode3 = new NodeForfVS(currentNode.x + 1, currentNode.y - 1,
                            currentNode.totalEnergy + energy(currentNode.x + 1, currentNode.y - 1));
                    newNode3.waysToHere = currentNode.waysToHere.clone();
                    newNode3.waysToHere[currentNode.y - 1] = currentNode.x + 1;
                    fringe.add(newNode3);
                }
            }
        }
        return retNode;
    }

    private int[] fVS_UCS(){
        int [] ret = new int[height()];
        double minEnergy = Double.MAX_VALUE;

        for(int i = 0; i < width(); i += 1){
            NodeForfVS end = fVS_UCSHelper(i);
            if(end.totalEnergy < minEnergy){
                ret = end.waysToHere;
                minEnergy = end.totalEnergy;
            }
        }
        return ret;
    }
    /*----------------------------The end of trial to implement the fVS in a Uniform-cost search way------------------*/
    /*----------------------------The trial to implement the fVS in a recursive way----------------------------------*/
    /**Notice that we store the route and energy in the forward direction**/
    private NodeForfVS fVS_recursiveHelper(int x, int y){
        if(y == 0){
            NodeForfVS ret = new NodeForfVS(x, y, energy(x, y));
            ret.waysToHere[0] = x;
            return ret;
        }
        /*Reflections:
        * This kind of recursive search is actually equivalent to the DFS, because before it select the shortest
        * path toward the current node from three child nodes, it needs to check(traverse) all possible ways */
        if(x < 0 || y < 0 || x > width() - 1 || y > height() - 1){
            throw new IndexOutOfBoundsException();
        }

        //Initialize the nodes
        NodeForfVS node1 = fVS_recursiveHelper(0, 0),node2, node3 = fVS_recursiveHelper(0, 0);

        if(x > 0) {
            node1 = fVS_recursiveHelper(x - 1, y - 1);
        }
        node2 = fVS_recursiveHelper(x, y - 1);
        if(x < width() - 1) {
            node3 = fVS_recursiveHelper(x + 1, y - 1);
        }

        /*To select the minimum cost from three children*/
        NodeForfVS ret = new NodeForfVS(0, 0,0);
        if (x > 0 && node1.totalEnergy < node2.totalEnergy && (x >= width() -1 || node1.totalEnergy < node3.totalEnergy)){
            ret = new NodeForfVS(x, y, node1.totalEnergy + energy(x, y));
            ret.waysToHere = node1.waysToHere.clone();
        }
        else if (x < width() - 1 && node3.totalEnergy < node2.totalEnergy && (x <= 0 || node3.totalEnergy < node1.totalEnergy)){
            ret = new NodeForfVS(x, y, node3.totalEnergy + energy(x, y));
            ret.waysToHere = node3.waysToHere.clone();
        }
        else{
            ret = new NodeForfVS(x, y, node2.totalEnergy + energy(x, y));
            ret.waysToHere = node2.waysToHere.clone();
        }
        /*to add current node to the path*/
        ret.waysToHere[y] = x;

        return ret;
    }

    private int[]  fVS_recursive() {
        double minEnergy = Double.MAX_VALUE;
        int [] ret = new int[0];
        for(int i = 0; i < width(); i += 1){
            NodeForfVS n = fVS_recursiveHelper(i, height() - 1);
            if (n.totalEnergy < minEnergy){
                ret = n.waysToHere;
                minEnergy = n.totalEnergy;
            }
        }
        return ret;
    }
    /*----------------------------The end of trial to implement the fVS in a recursive way-----------------------------*/
    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width()){
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width(), height() - 1);
        for(int i = 0; i < width(); i += 1){
            for(int j = 0, k = 0; k < height() ; j += 1, k += 1){
                if(k == seam[i]){
                    j -= 1;
                }
                else{
                    newPicture.set(i, j, thePicture.get(i, k));
                }

            }
        }
        thePicture = newPicture;
    }

    // remove vertical seam from picture(throw error)
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height()){
            throw new IllegalArgumentException();
        }
        Picture newPicture = new Picture(width() - 1, height());
        for(int i = 0; i < height(); i += 1){
            for(int j = 0, k = 0; k < width(); j += 1, k += 1){
                if(k == seam[i]){
                    j -= 1;
                }
                else{
                    newPicture.set(j, i, thePicture.get(k, i));
                }

            }
        }
        thePicture = newPicture;
    }
}

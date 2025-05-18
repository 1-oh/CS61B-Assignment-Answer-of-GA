import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.pow;
import static org.junit.Assert.assertEquals;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
/*LON:经度*/
/*LAT:纬度*/
public class Rasterer {
    double LeftMost,RightMost,UpMost,LowMost,TileSize;
    public Rasterer() {
        // YOUR CODE HERE
        LeftMost = MapServer.ROOT_ULLON;
        RightMost = MapServer.ROOT_LRLON;
        UpMost = MapServer.ROOT_ULLAT;
        LowMost = MapServer.ROOT_LRLAT;
        TileSize = MapServer.TILE_SIZE;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * [*]"render_grid"   : String[][], the files to display. <br>
     * [*]"raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * [*]"raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * [*]"raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * [*]"raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * [*]"depth"         : Number, the depth of the nodes of the rastered image <br>
     * []"query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        /*Get the request information from the params*/
        Map<String, Object> results = new HashMap<>();
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double w = params.get("w");
        double h = params.get("h");
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");

        /*Compute the depth that we should provide*/
        int depth = ComputeDepth(lrlon, ullon, w);
        results.put("depth",depth);

        /*Get the range of the area*/
        int LeftIndex, RightIndex, UpIndex, LowIndex;
        for(LeftIndex = 0; LeftIndex < (int)pow(2, depth); LeftIndex += 1){
            if(ComputeTileLON(depth, LeftIndex, false) >= ullon){
                results.put("raster_ul_lon", ComputeTileLON(depth, LeftIndex, true));
                break;
            }
        }
        for(RightIndex = (int)pow(2, depth) - 1; RightIndex > 0; RightIndex -= 1){
            if(ComputeTileLON(depth, RightIndex, true) <= lrlon){
                results.put("raster_lr_lon", ComputeTileLON(depth, RightIndex, false));
                break;
            }
        }
        for(UpIndex = 0; UpIndex < (int)pow(2, depth); UpIndex += 1){
            if(ComputeTileLAT(depth, UpIndex, false) <= ullat){
                results.put("raster_ul_lat",ComputeTileLAT(depth, UpIndex, true));
                break;
            }
        }
        for(LowIndex = (int)pow(2, depth) - 1; LowIndex > 0; LowIndex -= 1){
            if(ComputeTileLAT(depth, LowIndex, true) >= lrlat){
                results.put("raster_lr_lat", ComputeTileLAT(depth, LowIndex, false));
                break;
            }
        }
        /*Make the string[][] array*/
        String[][] area = new String[LowIndex - UpIndex + 1][RightIndex - LeftIndex + 1];
        for (int i = 0; i < RightIndex - LeftIndex + 1; i += 1){
            for(int j = 0; j < LowIndex - UpIndex + 1; j += 1){
                area[j][i] = "d" + depth + "_x" + (i + LeftIndex) + "_y" + (j + UpIndex) + ".png";
            }
        }
        results.put("render_grid", area);

        results.put("query_success", true);
        return results;
    }

    /*Compute the LonDPP that users wants*/
    private double RequestLonDPP(double LRLON, double ULLON, double width){
        return (LRLON - ULLON)/width;
    }
    /*Compute the LonDPP that we can provide with depth n*/
    private double TileLonDPP(int depth){
        return (RightMost - LeftMost)/(TileSize*pow(2, depth));
    }
    /*Decide on which depth should we provide*/
    private int ComputeDepth(double LRLON, double ULLON, double width){
        double request = RequestLonDPP(LRLON, ULLON, width);
        for(int i = 0; i <= 7; i += 1){
            if(TileLonDPP(i) <= request){
                return i;
            }
        }
        return 7;
    }

    /*Compute the LON of a tile's sides*/
    private double ComputeTileLON(int depth, int index, boolean LeftOrRight){
        /*We assume that true represent left while false represent the right*/
        if(index < 0 || index >= (int)pow(2, depth))
            throw new RuntimeException("The index is out of boundary");
        double leftLON = LeftMost + TileLonDPP(depth)*TileSize*index;
        if(LeftOrRight){
            return leftLON;
        }
        return leftLON + TileLonDPP(depth)*TileSize;
    }

    /*Compute the LAT of a tile's sides*/
    private double ComputeTileLAT(int depth, int index, boolean UpOrLow){
        /*We assume that true represent upper while false represent the lower*/
        if(index < 0 || index >= (int)pow(2, depth))
            throw new RuntimeException("The index is out of boundary");
        double UpLAT = UpMost - (UpMost - LowMost)/(int)pow(2, depth)*index;
        if(UpOrLow){
            return UpLAT;
        }
        return UpLAT - (UpMost - LowMost)/(int)pow(2, depth);
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Point {

    public ArrayList<Point> neighbors;
    public static Integer []types ={0,1,2,3};
    public int type;
//    public int staticField;
    public double staticField;
    public boolean isPedestrian;

    double impatience;

    boolean blocked = false;

    int maxx = 100000;
    
    public Point() {
        type=0;
        staticField = maxx;
        neighbors= new ArrayList<Point>();
        impatience = 0;
    }

    public void clear() {
        staticField = maxx;

    }

    public boolean calcStaticField() {
        if(type==1)
            return false;
//        int min_pot = maxx;
        double min_pot = maxx;
        int idx = -1;
//        boolean diag = true;
        for(int i=0;i<neighbors.size();i++){
            if(neighbors.get(i).staticField<min_pot) {
                min_pot = neighbors.get(i).staticField;
                idx = i;
            }
        }
//        int dist = ((idx-4<0)?5:7);
        double dist = ((idx<4)?1:(double)Math.sqrt(2));
        if(staticField>min_pot+dist){
            staticField=min_pot+dist;
            return true;
        }
        return false;
    }

    public void move(){
        if(isPedestrian && !blocked){
//            int min_pot = maxx;
//            double min_pot = maxx;
            double min_pot = staticField + impatience;
            int idx = -1;
            List<Integer> side = new ArrayList<Integer>();
            List<Integer> slant = new ArrayList<Integer>();
            for(int i=0;i< neighbors.size();i++){
                if(neighbors.get(i).staticField<=min_pot && !neighbors.get(i).isPedestrian){
                    min_pot=neighbors.get(i).staticField;
                    if(i<4)
                        side.add(i);
                    else
                        slant.add(i);
                    idx = i;
                }
            }
            if(idx==-1) {
                impatience+=0.75;
                return;
            }

            Random rand = new Random();
            if(side.size()>0){
                int upperbound = side.size();
                idx=side.get(rand.nextInt(upperbound));
            }
            else{
                int upperbound = slant.size();
                idx=slant.get(rand.nextInt(upperbound));
            }

            if(neighbors.get(idx).type!=2) {
                neighbors.get(idx).isPedestrian = true;
                neighbors.get(idx).blocked=true;
                neighbors.get(idx).impatience = 0;
            }
            blocked=true;
            isPedestrian=false;
            impatience = 0;
        }
    }

    public void addNeighbor(Point nei) {
        neighbors.add(nei);
    }
}
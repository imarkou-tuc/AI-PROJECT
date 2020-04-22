package GridWorld;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.*;


public class Navigator {
    Grid map;
    int [] start=new int[2];
    int [] finish=new int[2];
    int M;
    int N;
    private int sr,sc,fr,fc;
    private  Queue<Integer> rq= new LinkedList<Integer>();//Lista me tous kombous pou anaptussw(BFS)
    private  Queue<Integer> cq = new LinkedList<Integer>();//Lista me tous kombous pou anaptussw(BFS)
    private Stack<Integer> rstack=new Stack<Integer>();//(DFS)
    private Stack<Integer> cstack=new Stack<Integer>();//(DFS)
    private  LinkedList<Integer> openList= new LinkedList<Integer>();//A* open list
    private  LinkedList<Integer> closeList = new LinkedList<Integer>();//A* close list
    private  LinkedList<Integer> temp = new LinkedList<Integer>();//A* close list

    boolean reached_end=false;
    boolean [][] visited;//Array NxM;
    int [] prev;
    int nodes_left_in_layer=1,nodes_in_next_layer=0,move_count=0;
    int [] dr;
    int [] dc;
    int cost=0;

    public Navigator(Grid map){
        this.map=map;
        M=this.map.getNumOfColumns();
        N=this.map.getNumOfRows();
        start=this.map.getStart();//Starting coordinates in 2d array
                sr=start[0];//starting row
        sc=start[1];//starting column
        finish=this.map.getTerminal();//finishing coordinates in 2d array
                fr=finish[0];//finishing row
        fc=finish[1];//finising column
        dr=new int []{-1,+1,0,0};
        dc=new int []{0,0,+1,-1};
        visited=new boolean[N][M];
        prev=new int[M*N];

    }

    public int dfs() {
        int r, c;
        rstack.add(sr);
        cstack.add(sc);

        visited[sr][sc] = true;

        while(!rstack.isEmpty() || !cstack.isEmpty()) {
            r=rstack.pop();
            c=cstack.pop();
            move_count++;

            if (map.getCell(r, c).isTerminal()) {
                System.out.println("Solution found at"  + map.getCell(r,c) + " ");
                reached_end=true;
                System.out.println("Total nodes expanded" +move_count);
                return 0;
            }
            explore_neighboursDFS(r,c);
        }
        return -1;
    }


    public int bfs() {
        int r, c;
        rq.add(sr);
        cq.add(sc);
        visited[sr][sc] = true;
        while (rq.size() > 0 ||  cq.size()>0) {
            r = rq.poll();
            c = cq.poll();
            move_count++;
            if (map.getCell(r, c).isTerminal()) {
                System.out.println("Solution found at"  + map.getCell(r,c) + " ");
                reached_end=true;
                System.out.println("Total nodes expanded" +move_count);
                return 0;
            }
            explore_neighboursBFS(r, c);
            nodes_left_in_layer--;
            if(nodes_left_in_layer==0) {
                nodes_left_in_layer = nodes_in_next_layer;
                nodes_in_next_layer = 0;
            }
        }

        return -1;
    }


    public void explore_neighboursBFS(int r1,int c1){
        int rr,cc,x,i;
        x=r1*M +c1;
        for(i=0; i<4; i++){
            rr=r1+dr[i];
            cc=c1+dc[i];

            if((rr>=0 && cc>=0) && (rr<N && cc<M) &&(visited[rr][cc]==false) && (map.getCell(rr, cc).isWall()==false) ){
                rq.add(rr);
                cq.add(cc);
                visited[rr][cc]=true;
                prev[rr*M+ cc]=x;
                nodes_in_next_layer++;
            }
        }
    }

    public void explore_neighboursDFS(int r1,int c1){
        int i;
        int rr;
        int x;
        x=r1*M +c1;
        int cc;
        for(i=0; i<4; i++){
            rr=r1+dr[i];
            cc=c1+dc[i];

            if((rr>=0 && cc>=0) && (rr<N && cc<M) &&(visited[rr][cc]==false) && (map.getCell(rr, cc).isWall()==false) ){
                rstack.add(rr);
                cstack.add(cc);
                visited[rr][cc]=true;
                prev[rr*M+ cc]=x;
                nodes_in_next_layer++;
            }
        }
    }

    public void explore_neighboursAStar(int r1,int c1) {
        int rr,cc,x,i,neighbour;
        x = r1 * M + c1;
        for (i = 0; i < 4; i++) {
            rr = r1 + dr[i];
            cc = c1 + dc[i];
            neighbour=(rr*M)+cc;

            if ((rr >= 0 && cc >= 0)  && (rr < N && cc < M) && (map.getCell(rr, cc).isWall() == false)) {

                if(!openList.contains(neighbour)) {
                    map.getCell(rr,cc).setParent(map.getCell(r1,c1));
                    map.getCell(rr,cc).setPosition(neighbour);
                    map.getCell(r1,c1).setgCost();
                    openList.add(neighbour);
                } else {//node is in open list
                    if(map.getCell(rr,cc).getgCost()>map.getCell(r1,c1).getgCost()) {// costs from current node are cheaper than previous costs
                        map.getCell(rr,cc).setParent(map.getCell(r1,c1));
                        map.getCell(r1,c1).setgCost();
                    }
                }
                prev[rr*M+cc]=x;
                visited[rr][cc] = true;
            }
        }
    }

    public void Astar() {
        int r,c,cheapest,cheapestx,cheapesty;
        reached_end=false;
        openList.add((sr*M)+sc);

        visited[sr][sc]=true;
        prev[sr*M+sc]=2;
        findHeuristics();
        map.getCell(sr,sc).setParent(map.getCell(sr,sc));


        while (!reached_end) {
            cheapest=cheapestNeighbour();
            cheapestx=cheapest/M;
            cheapesty=cheapest%M;
            closeList.add(cheapest);
            move_count++;
            for(int i = 0; i < openList.size();i++) {
                if(openList.get(i)==cheapest)
                openList.remove(i);
        }
            if (map.getCell(cheapestx, cheapesty).isTerminal()) {
                System.out.println("Solution found at"  + (cheapestx) + " " + (cheapesty));
                reached_end=true;
                System.out.println("Total nodes expanded" +move_count);
            }
            explore_neighboursAStar(cheapestx,cheapesty);
        }

    }

    public int cheapestNeighbour() {
        int cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            int cheapx=cheapest/M;
            int cheapy=cheapest%M;
            if (map.getCell(openList.get(i)/M,openList.get(i)%M).getfCost() < map.getCell(cheapx,cheapy).getfCost()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    public void LRTA() {
        int r,c,cheapest,cheapestx,cheapesty;
        reached_end=false;
        openList.add((sr*M)+sc);

        visited[sr][sc]=true;
        prev[sr*M+sc]=-1;
        findHeuristics();
        map.getCell(sr,sc).setParent(map.getCell(sr,sc));

        while (!reached_end) {
            cheapest=cheapestNeighbourLRTA();
            cheapestx=cheapest/M;
            cheapesty=cheapest%M;

            closeList.add(cheapest);
            map.getCell(cheapestx, cheapesty).setgCost();
            move_count++;



            for(int i = 0; i < openList.size();i++) {
                if(openList.get(i)==cheapest)
                    openList.remove(i);
            }
            if (map.getCell(cheapestx, cheapesty).isTerminal()) {
                System.out.println("Solution found at"  + (cheapestx) + " " + (cheapesty));
                reached_end=true;
            }
            explore_neighboursLRTA(cheapestx,cheapesty);
        }

    }
    public void explore_neighboursLRTA(int r1,int c1) {
        int rr,cc,x,i,neighbour;
        x = r1 * M + c1;
        for (i = 0; i < 4; i++) {
            rr = r1 + dr[i];
            cc = c1 + dc[i];
            neighbour=(rr*M)+cc;

            if ((rr >= 0 && cc >= 0)  && (rr < N && cc < M) && (map.getCell(rr, cc).isWall() == false)) {

                if(!openList.contains(neighbour)) {
                    map.getCell(rr,cc).setParent(map.getCell(r1,c1));
                    map.getCell(rr,cc).setPosition(neighbour);
                    map.getCell(rr,cc).setLRTAgCost();
                    openList.add(neighbour);
                } else {//node is in open list
                    if(map.getCell(rr,cc).getLRTAgCost()<map.getCell(r1,c1).getLRTAgCost()) {// costs from current node are cheaper than previous costs
                        map.getCell(rr,cc).setParent(map.getCell(r1,c1));
                        map.getCell(r1,c1).setLRTAgCost();
                }
                }
                prev[rr*M+cc]=x;
            }
        }
    }

    public int cheapestNeighbourLRTA() {
        int cheapest = openList.getLast();
        for (int i = 0; i < openList.size(); i++) {
            int cheapx=cheapest/M;
            int cheapy=cheapest%M;
            if (map.getCell(openList.get(i)/M,openList.get(i)%M).getLRTAfCost() < map.getCell(cheapx,cheapy).getLRTAfCost()) {
                cheapest = openList.get(i);
            }
        }
        visited[cheapest/M][cheapest%M] = true;
        return cheapest;
    }
    public List<Integer> reconstruct_Path() {
        bfs();
        List<Integer> path = new ArrayList();
        for (Integer at = map.getTerminalidx(); at != map.getStartidx(); at = prev[at]) {
            int x=prev[at]/map.getNumOfColumns();
            int y=prev[at]%map.getNumOfColumns();
            path.add(at);
            cost+=map.getCell(x,y).getCost();
        }
        Collections.reverse(path);
        if (path.get(0) == map.getStartidx()) return path;
        path.clear();
        return path;
    }

    public LinkedList<Integer> reconstruct_Path_Astar() {
        Astar();



        for(int p = 0 ; p < closeList.size();p++) {
            cost+=map.getCell(closeList.get(p)/M,closeList.get(p)%M).getCost();
        }

        return closeList;

    }

    public LinkedList<Integer> reconstruct_Path_LRTA() {
        LRTA();
        List<Integer> path = new ArrayList();

        for(int h = 0; h < closeList.size(); h++) {
            if(!temp.contains(closeList.get(h)))
            temp.add(closeList.get(h));
        }

       // for(int p = 0 ; p < temp.size();p++) {
        //    cost+=map.getCell(temp.get(p)/M,temp.get(p)%M).getCost();
        //}

        return temp;

    }



    public int [] FinalpathLRTA(){
        List<Integer> path1=reconstruct_Path_LRTA();
        System.out.printf("The shortest path from %d to %d is [%s]\n", map.getStartidx(), map.getTerminalidx(), formatPath(path1));
        System.out.println();
        System.out.print("Total cost of LRTA*: " + temp.size());
        int[] arr = path1.parallelStream().mapToInt(Integer::intValue).toArray();
        return arr;
    }

    public int [] FinalpathAstar(){
        List<Integer> path1=reconstruct_Path_Astar();
        System.out.printf("The shortest path from %d to %d is [%s]\n", map.getStartidx(), map.getTerminalidx(), formatPath(path1));
        System.out.println();
        System.out.print("Total cost of A*: " + cost);
        int[] arr = path1.parallelStream().mapToInt(Integer::intValue).toArray();
        return arr;
    }

    private static String formatPath(List<Integer> path) {
        return String.join(
                " -> ", path.stream().map(Object::toString).collect(java.util.stream.Collectors.toList()));
    }

    public void findHeuristics() {
        for(int i = 0; i < map.getNumOfRows(); i++) {
            for (int j = 0; j < map.getNumOfColumns(); j++) {
                Cell cell = new Cell();
                cell = map.getCell(i,j);
                cell.setHeuristic(Math.abs(i-map.getTerminalidx()/map.getNumOfColumns()) + Math.abs(j-map.getTerminalidx()%map.getNumOfColumns()));
            }
        }
    }

    public static void main(String[] args) {
        Grid mygrid;
        Navigator n;
        mygrid = new Grid(args[1]);
        n=new Navigator(mygrid);
        System.out.println();
        n.FinalpathLRTA();
    }
}




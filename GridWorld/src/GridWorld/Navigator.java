package GridWorld;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Navigator {
    Grid map;
    int [] start=new int[2];
    int [] finish=new int[2];
    int M;
    int N;
    private int sr,sc,fr,fc;
    private  Queue<Integer> rq= new LinkedList<Integer>();//Lista me tous kombous pou anaptussw
    private  Queue<Integer> cq = new LinkedList<Integer>();//Lista me tous kombous pou anaptussw
    boolean reached_end=false;
    boolean [][] visited;//Array NxM;
    int [] prev;
    int nodes_left_in_layer=1,nodes_in_next_layer=0,move_count=0;
    int [] dr;
    int [] dc;
    private Stack<Integer> rstack=new Stack<Integer>();
    private Stack<Integer> cstack=new Stack<Integer>();
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
        int i;
        int rr;
        int x;
        x=r1*M +c1;
        int cc;
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

    public List<Integer> reconstruct_Path() {
        dfs();
        List<Integer> path = new ArrayList();
        for (Integer at = map.getTerminalidx(); at != 0; at = prev[at]) {
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
    public void Finalpath(){
        List<Integer> path1 = reconstruct_Path();
        System.out.printf("The shortest path from %d to %d is [%s]\n", map.getStartidx(), map.getTerminalidx(), formatPath(path1));
        System.out.println();
        System.out.print("Optimal cost path: " + cost);

    }
    private static String formatPath(List<Integer> path) {
        return String.join(
                " -> ", path.stream().map(Object::toString).collect(java.util.stream.Collectors.toList()));
    }


    public static void main(String[] args) {
        Grid mygrid;
        Navigator n;
        mygrid = new Grid(args[1]);
        n=new Navigator(mygrid);
        n.Finalpath();
    }
}




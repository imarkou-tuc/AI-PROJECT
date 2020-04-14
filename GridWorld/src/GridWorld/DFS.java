package GridWorld;

import java.util.ArrayList;

public class DFS {

    public Grid myGrid;

     boolean[] visited = new boolean[117];
    private ArrayList<Integer>[] neighbours = new ArrayList[117];
    int cost = 0;


    public DFS(Grid myGrid) {
        this.myGrid = myGrid;


        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        for (int l = 0; l < 116; l++) {
            neighbours[l] = new ArrayList<Integer>();
        }

    }

    public void dfs(int r, int c) {

        visited[r*myGrid.getNumOfColumns()+c]=true;


        if(myGrid.getCell(r,c).isTerminal()) {
            System.out.println("Solution found at :" + r + "<-Row Column->" + c);
            System.out.println("Total cost:" + cost);

            return;
        }
        int pick=1;

        for(int i = 1; i < 5; i++) {
            pick=i;

            switch(pick) {
                case 1://going up
                    if(c-1>=0) {
                        if((!myGrid.getCell(r,c-1).isWall()) && !visited[r*myGrid.getNumOfColumns()+(c-1)]) {
                            dfs(r,c-1);
                            cost+=myGrid.getCell(r,c-1).getCost();
                        }
                    }
                    break;
                case 2://going down
                    if(c+1<myGrid.getNumOfColumns()) {
                        if((!myGrid.getCell(r,c+1).isWall()) && !visited[r*myGrid.getNumOfColumns()+(c+1)]) {
                            dfs(r,c+1);
                            cost+=myGrid.getCell(r,c+1).getCost();
                        }
                    }
                    break;
                case 3://going left
                    if(r-1>=0) {
                        if((!myGrid.getCell(r-1,c).isWall()) && !visited[(r-1)*myGrid.getNumOfColumns()+c]) {
                            dfs(r-1,c);
                            cost+=myGrid.getCell(r-1,c).getCost();
                        }
                    }
                    break;
                case 4://going right
                    if(r+1<myGrid.getNumOfRows()) {
                        if((!myGrid.getCell(r+1,c).isWall()) && !visited[(r+1)*myGrid.getNumOfColumns()+c]) {
                            dfs(r+1,c);
                            cost+=myGrid.getCell(r+1,c).getCost();
                        }
                    }
                    break;
                default: System.out.println("Not supposed to reach this.");
                    break;
            }
        }


    }

}

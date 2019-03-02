import java.util.*;
import java.io.*;

public class Maze{

  private char[][] maze;

  private boolean animate;//false by default

  private int counter;

  public static void main(String[] args) {
    try {
      Maze maze = new Maze(args[0]);
      maze.setAnimate(true);
      System.out.println(maze.solve());
      System.out.println(maze);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  /*Constructor loads a maze text file, and sets animate to false by default.
  1. The file contains a rectangular ascii maze, made with the following 4 characters:
  '#' - Walls - locations that cannot be moved onto
  ' ' - Empty Space - locations that can be moved onto
  'E' - the location of the goal (exactly 1 per file)
  'S' - the location of the start(exactly 1 per file)
  2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!
  3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:
  throw a FileNotFoundException or IllegalStateException
  */
  public Maze(String filename) throws FileNotFoundException{
    File text = new File(filename);
    Scanner inf = new Scanner(text);
    String m = "";
    int row = 0;
    int col = 0;
    int totalChars = 0;
    int index = 0;
    counter = 0;
    animate = false;
    int eCount = 0;
    int sCount = 0;
    while(inf.hasNextLine()){
      String line = inf.nextLine();
      row++;
      m += line;
    }
    for (int i = 0; i < m.length(); i++) {
      totalChars++;
    }
    col = totalChars / row;
    maze = new char[row][col];
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        maze[r][c] = m.charAt(index);
        if (maze[r][c] == 'S') sCount++;
        if (maze[r][c] == 'E') eCount++;
        index++;
      }
    }
    if (eCount != 1 || sCount != 1) {
      throw new IllegalStateException("Board does not contain exactly one start and one exit");
    }
  }

  private void wait(int millis){
    try {
      Thread.sleep(millis);
    }
    catch (InterruptedException e) {
    }
  }
  public void setAnimate(boolean b){
    animate = b;
  }
  public void clearTerminal(){
    //erase terminal, go to top left of screen.
    System.out.println("\033[2J\033[1;1H");
  }
  /*Wrapper Solve Function returns the helper function
  Note the helper function has the same name, but different parameters.
  Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.
  */
  public int solve(){
    int row = 0;
    int col = 0;
    int moves = 0;
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        if (maze[r][c] == 'S') {
          row = r;
          col = c;
        }
      }
    }
    maze[row][col] = ' ';
    return solve(row, col);
  }
  /*
  Recursive Solve function:
  A solved maze has a path marked with '@' from S to E.
  Returns the number of @ symbols from S to E when the maze is solved,
  Returns -1 when the maze has no solution.
  Postcondition:
  The S is replaced with '@' but the 'E' is not.
  All visited spots that were not part of the solution are changed to '.'
  All visited spots that are part of the solution are changed to '@'
  */
  private int solve(int row, int col){ //you can add more parameters since this is private
    int[] moves = new int[] {-1, 0, 1, 0, 0, -1, 0, 1};
    if(animate){
      clearTerminal();
      System.out.println(this);
      //debug();
      wait(50);
    }
    if (maze[row][col] == 'E') {
      return counter;
    }
    maze[row][col] = '@';
    counter++;
    for (int i = 0; i < moves.length; i += 2) {
      if (maze[row + moves[i]][col + moves[i + 1]] != '#' && maze[row + moves[i]][col + moves[i + 1]] != '.' && maze[row + moves[i]][col + moves[i + 1]] != '@' &&
      solve(row + moves[i], col + moves[i + 1]) != -1) {
        return counter;
      }
    }
    maze[row][col] = '.';
    counter--;
    return -1;
  }
  public void debug() {
    String ans = "";
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        if (maze[r][c] == '#') {
          ans += '|';
        } else {
          ans += maze[r][c];
        }
      }
      ans += "\n";
    }
    System.out.println(ans);
  }
  public String toString() {
    String ans = "";
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        ans += maze[r][c];
      }
      ans += "\n";
    }
    return ans;
  }
}

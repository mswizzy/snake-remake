import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int unitSize = 25;
    static int boardWidth;
    static int boardHeight;

    static final int gameUnits = (screenWidth * screenHeight) / unitSize;
    int delay;
    SnakeWindow frame;
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];
    int bodyParts = 5;
    Item item;
    //starts running to the right
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    //indicates if game is over
    Scoreboard scoreboard;
    Level level;
    static boolean gameOn = false;

    public GamePanel(SnakeWindow frame, Scoreboard scoreboard, Level level) {
        this.scoreboard = scoreboard;
        this.frame = frame;
        this.level = level;
        delay = level.getDelay();
        random = new Random();
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(level.changeBackground());
        setFocusable(true);

        setVisible(true);
        requestFocusInWindow();
        //grabFocus();
        addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        newItem();
        running = true;
        timer = new Timer(delay, this);
        scoreboard.timer = timer;
        //timer.start();
    }

    public void pause() {
        GamePanel.gameOn = false;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = true;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boardWidth = getWidth();
        boardHeight = getHeight();
        getSize();
        drawSnake(g);
        drawItem(g);
//        if (!gameOn) {
//            draw(g);
//        }

    }

    public void drawSnake(Graphics g) {
        if (running) {
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(42, 162, 42));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
        }
    }

    public void drawItem(Graphics g){
        if (running) {
            //get new item;
            g.setColor(item.changeColor());
            g.fillOval(item.getX(), item.getY(), unitSize, unitSize);
        }
    }


    public void newItem() {
        //randomly select next item
        item = level.selectItem();
    }

    public void move(){
        for(int i = bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){
            case'U':
                y[0] = y[0]-unitSize;
                break;
            case'D':
                y[0] = y[0]+unitSize;
                break;
            case'L':
                x[0] = x[0]-unitSize;
                break;
            case'R':
                x[0] = x[0]+unitSize;
                break;
        }
    }

    public void checkItem() {
        if ((x[0] == item.getX()) && (y[0] == item.getY())) {

            //increase score on scoreboard
            scoreboard.score.addItem(item);
            bodyParts++;
            newItem();
            //make sure item y is in bounds of the board
            if (item.getY() > boardHeight) {
                newItem();
            }

        }
    }

    public void checkCollisions(){
        //if head hits body
        for(int i=bodyParts;i>0;i--){
             if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //for walls collides left border
        if(x[0] < 0){
            running = false;
        }
        //right border
        //if(x[0] > boardWidth) {
        if(x[0] > screenWidth){
            running = false;
        }
        //touches top border
        if(y[0] < 0){
            running = false;
        }
        //bottom border
        //if (y[0] > boardHeight) {
        if(y[0] > screenHeight){
            running = false;
        }
        if (!running){
            gameOn = false;
            frame.gameOver(scoreboard.score.score);
            timer.stop();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkItem();
            checkCollisions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!GamePanel.gameOn){
                        resume();
                    }else{
                        pause();
                    }
                    break;

            }
        }
    }
}

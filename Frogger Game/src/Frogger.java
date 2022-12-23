import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Frogger extends JFrame {

    public Frogger(){
        //take a look at the constructor of FroggerPanel. It creates the frog and the cars
        FroggerPanel frogPanel = new FroggerPanel(); //also frog and cars are created
        this.setLayout(new BorderLayout());
        this.add(frogPanel, BorderLayout.CENTER);
        setTitle("Frogger");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible (true);
        this.setResizable(false);

        this.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                //move the frog
                if (e.getKeyCode() == KeyEvent.VK_UP){
                    frogPanel.frog.moveUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN){
                    frogPanel.frog.moveDown();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                    frogPanel.frog.moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT){
                    frogPanel.frog.moveLeft();
                }
            }
        });
    }

    public static void main ( String[] args ){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                Frogger frogger = new Frogger();//creates the main window
            }
        });
    }
}

class FroggerPanel extends JPanel {
    Car[] cars;
    Frog frog;
    int lives = 3;
    int score = 0;

    public FroggerPanel() {
        cars = new Car[16];
        for (int i=0; i<4; i++){
            Color color = new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
            cars[i] = new Car(-100 + (i*150), 385,1,1, color);
        }
        for (int i=4; i<8; i++){
            Color color = new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
            cars[i] = new Car(500 - (i -4) * 150, 345,1,-1, color);
        }
        for (int i=8; i<12; i++){
            Color color = new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
            cars[i] = new Car(-100 + (i-8) * 150, 255,2,1, color);
        }
        for (int i=12; i<16; i++){
            Color color = new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random()));
            cars[i] = new Car(500 - (i-12) * 150, 215,2,-1, color);
        }

        frog = new Frog(190, 500);

        Timer myTimer = new Timer (10, new ActionListener(){
            public void actionPerformed (ActionEvent evt){
                if (lives <= 0 || frog.y < 70) {
                    gameOver();
                }
                else {
                    repaint();
                }
            }
        });
        myTimer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        drawBackground(g2);
        drawLives(lives, g2);
        drawScore(score, g2);
        frog.draw(g2);

        for (int i = 0; i < 16; i++) {
            cars[i].move();
            cars[i].draw(g2);
            if (frog.isTouching(cars[i])) {
                lives--;
                frog.x = 190;
                frog.y = 500;
            }
        }
    }

    void drawBackground(Graphics2D g2) {
        //grass
        g2.setPaint(Color.GREEN);
        g2.fillRect(0, 0, getWidth(), getHeight());

        //roads
        g2.setPaint(Color.GRAY);
        g2.fillRect(0, 330, 400, 90); //lower road
        g2.fillRect(0,200,400,90); //upper road

        //upper road lines
        g2.setPaint(new Color(239, 183, 0));
        g2.drawLine(5,245,40,245);
        g2.drawLine(60,245,90,245);
        g2.drawLine(120,245,150,245);
        g2.drawLine(180,245,210,245);
        g2.drawLine(240,245,270,245);
        g2.drawLine(300,245,330,245);
        g2.drawLine(360,245,390,245);

        //lower road lines
        g2.drawLine(5,375,40,375);
        g2.drawLine(60,375,90,375);
        g2.drawLine(120,375,150,375);
        g2.drawLine(180,375,210,375);
        g2.drawLine(240,375,270,375);
        g2.drawLine(300,375,330,375);
        g2.drawLine(360,375,390,375);

        //lake
        g2.setPaint(new Color(0, 159, 225));
        g2.fill(new Ellipse2D.Double(-50, -150, 500, 255));
    }

    void drawLives(int lives, Graphics2D g2) {
        //number of lives left
        g2.setPaint(Color.BLACK);
        g2.drawString("# of Lives:", 10, 515);
        g2.setPaint(new Color(2, 143, 30));

        //displays frogs depending on the # of lives user has left
        if (lives == 3) {
            g2.fill(new Ellipse2D.Double(10, 530, 30, 30));
            g2.fill(new Ellipse2D.Double(40, 530, 30, 30));
            g2.fill(new Ellipse2D.Double(70, 530, 30, 30));
        }
        else if (lives == 2) {
            g2.fill(new Ellipse2D.Double(10, 530, 30, 30));
            g2.fill(new Ellipse2D.Double(40, 530, 30, 30));
        }
        else if (lives == 1) {
            g2.fill(new Ellipse2D.Double(10, 530, 30, 30));
        }
    }

    void drawScore(int score, Graphics2D g2) {
        if (frog.y <= 375) {
            score = 1;
        }
        if (frog.y <= 330) {
            score = 2;
        }
        if (frog.y < 245) {
            score = 3;
        }
        if (frog.y < 200) {
            score = 4;
        }

        g2.setPaint(Color.BLACK);
        g2.drawString("Score:", 350, 515);
        g2.drawString("" + score, 350, 545);
    }

    void gameOver() {
        int option = 7;

        if (lives <= 0) {
            option = JOptionPane.showConfirmDialog(null,"You lost! Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        }
        else if (frog.y < 70) {
            option = JOptionPane.showConfirmDialog(null,"You won! Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        }

        if (option == JOptionPane.YES_OPTION) {
            lives = 3;
            frog.x = 190;
            frog.y = 500;

        } else {
            System.exit(0);
        }
    }
}

class Car extends Rectangle {
    int speed;
    int direction;
    Color color;

    public Car(int x, int y, int speed, int direction, Color color) {
        super(x, y, 50, 20);
        this.speed = speed;
        this.direction = direction;
        this.color = color;
    }

    void move() {
        x = x + speed * direction;

        if (direction == 1 && x > 400){ //car moving beyond the right margin
            x = -50;
        }
        if (direction == -1 && x < -50){ //car moving beyond the left margin
            x = 400;
        }
    }

    void draw (Graphics2D g2){
        g2.setPaint(color);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }
}

class Rectangle{
    int x, y, width, height, bottom, right;

    Rectangle (int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bottom = this.y + this.height;
        this.right = this.x + this.width;
    }

    boolean isTouching(Rectangle object){
        return ((x < object.x + 50) && (y < object.y + 20) &&
                (x + 30 > object.x) && (y + 30 > object.y));
    }
}

class Frog extends Rectangle {

    public Frog(int x, int y){
        super (x, y, 30, 30);
    }

    void moveLeft(){
        x-=width;
        right-=width;
        if (x < -10) {
            x = 390;
        }
    }
    void moveRight(){
        x+=width;
        right+=width;
        if (x > 390) {
            x = -10;
        }
    }
    void moveUp(){
        y-=20;
        bottom-=20;
    }
    void moveDown(){
        y+=20;
        bottom+=20;
        if (y > 545) {
            y = 545;
        }
    }

    void draw(Graphics2D g2){
        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new Ellipse2D.Double(x, y, width, height));
        g2.setPaint(new Color(2, 143, 30));
        g2.fill (new Ellipse2D.Double(x, y, width, height));
    }
}

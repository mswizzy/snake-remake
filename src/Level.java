import java.awt.*;
import java.util.Random;

public interface Level {
    Random random = new Random();
    public Color changeBackground();
    public Item selectItem();
    public int getDelay();
}

//only apples appear
class Easy implements Level {
    Color background = Color.CYAN;
    int delay = 75;

    public Easy() {
    }

    public Color changeBackground() {
        return background;
    }

    public Item selectItem() {
        return new Apple();
    }
    public int getDelay() {
        return delay;
    }
}

//introduces bombs
class Intermediate implements Level {
    Color background = Color.YELLOW;
    int delay = 50;
    Item item;
    public Intermediate(){}
    public Color changeBackground() {
        return background;
    }
    public Item selectItem() {
        int next = random.nextInt(100);

        if (next % 10 == 0) {
            item = new Bomb();
        }  else {
            //any other time
            item = new Apple();
        } return item;
    }
    public int getDelay() {
        return delay;
    }
}

//higher probability of bombs and diamonds
class Hard implements Level {
    Color background = Color.magenta;
    int delay = 35;
    Item item;
    public Hard(){}
    public Color changeBackground() {
        return background;
    }
    public Item selectItem() {
        int next = random.nextInt(100);
        if (next % 10 == 0 || next % 15 == 0) {
            item = new Bomb();
        } else if (next % 20 == 0 || next % 30 == 0) {
            item = new Diamond();
        } else {
            //any other time
            item = new Apple();
        } return item;
    }
    public int getDelay() {
        return delay;
    }
}


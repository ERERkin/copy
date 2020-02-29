package it.academy.demoBot.model;

public class Game {
    int id;
    int button1;
    int button2;
    int button3;
    int button4;
    int button5;
    int button6;
    int button7;
    int button8;
    int button9;
    boolean playerX;
    boolean playerY;

    public Game(int id, int button1, int button2, int button3, int button4, int button5,
                int button6, int button7, int button8,
                int button9, boolean playerX, boolean playerY) {
        this.id = id;
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;
        this.button4 = button4;
        this.button5 = button5;
        this.button6 = button6;
        this.button7 = button7;
        this.button8 = button8;
        this.button9 = button9;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getButton1() {
        return button1;
    }

    public void setButton1(int button1) {
        this.button1 = button1;
    }

    public int getButton2() {
        return button2;
    }

    public void setButton2(int button2) {
        this.button2 = button2;
    }

    public int getButton3() {
        return button3;
    }

    public void setButton3(int button3) {
        this.button3 = button3;
    }

    public int getButton4() {
        return button4;
    }

    public void setButton4(int button4) {
        this.button4 = button4;
    }

    public int getButton5() {
        return button5;
    }

    public void setButton5(int button5) {
        this.button5 = button5;
    }

    public int getButton6() {
        return button6;
    }

    public void setButton6(int button6) {
        this.button6 = button6;
    }

    public int getButton7() {
        return button7;
    }

    public void setButton7(int button7) {
        this.button7 = button7;
    }

    public int getButton8() {
        return button8;
    }

    public void setButton8(int button8) {
        this.button8 = button8;
    }

    public int getButton9() {
        return button9;
    }

    public void setButton9(int button9) {
        this.button9 = button9;
    }

    public boolean isPlayerX() {
        return playerX;
    }

    public void setPlayerX(boolean playerX) {
        this.playerX = playerX;
    }

    public boolean isPlayerY() {
        return playerY;
    }

    public void setPlayerY(boolean playerY) {
        this.playerY = playerY;
    }
}

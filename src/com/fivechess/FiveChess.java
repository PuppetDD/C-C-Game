package com.fivechess;

/**
 * C-C-Game
 * com.fivechess
 *
 * @author GOLD
 * @date 2019/6/6
 */
public class FiveChess {

    private double width;
    private double height;
    private double cellLen;
    private String color = "White";
    /**
     * 标志当前用户的落子颜色
     **/
    private char localSide = 'W';
    /**
     * 区别当前落子方
     **/
    private char flag = 'B';
    private char[][] chess;
    /**
     * 结束标志
     **/
    private boolean end;

    public FiveChess(double width, double height, double cellLen) {
        this.width = width;
        this.height = height;
        this.cellLen = cellLen;
        chess = new char[(int) height + 1][(int) width + 1];
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                chess[i][j] = ' ';
            }
        }
        this.end = false;
    }

    public void play(int x, int y, char color) {
        //将当前的棋子放置到（x,y）
        if (chess[x][y] == ' ') {
            chess[x][y] = color;
            //落子正确的话，与flag相等的一方不能落子
            flag = color;
        }
    }

    public boolean judgeGame(int row, int col, char chessColor) {
        //判断游戏是否结束
        if (rowJudge(row, col, chessColor) && colJudge(row, col, chessColor) && mainDiagonalJudge(row, col, chessColor) && DeputyDiagonalJudge(row, col, chessColor)) {
            return true;
        }
        return false;
    }

    public boolean rowJudge(int row, int col, char chessColor) {
        //判断一行是否五子连线
        int count = 0;
        for (int j = col; j < width; j++) {
            if (chess[row][j] != chessColor) {
                break;
            }
            count++;
        }
        for (int j = col - 1; j >= 0; j--) {
            if (chess[row][j] != chessColor) {
                break;
            }
            count++;
        }
        if (count >= 5) {
            return false;
        }
        return true;
    }

    public boolean colJudge(int row, int col, char chessColor) {
        //判断一列是否五子连线
        int count = 0;
        for (int i = row; i < height; i++) {
            if (chess[i][col] != chessColor) {
                break;
            }
            count++;
        }
        for (int i = row - 1; i >= 0; i--) {
            if (chess[i][col] != chessColor) {
                break;
            }
            count++;
        }
        if (count >= 5) {
            return false;
        }
        return true;
    }

    public boolean mainDiagonalJudge(int row, int col, char chessColor) {
        //判断主对角线是否五子连线
        int count = 0;
        for (int i = row, j = col; i < height && j < width; i++, j++) {
            if (chess[i][j] != chessColor) {
                break;
            }
            count++;
        }
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (chess[i][j] != chessColor) {
                break;
            }
            count++;
        }
        if (count >= 5) {
            return false;
        }
        return true;
    }

    public boolean DeputyDiagonalJudge(int row, int col, char chessColor) {
        //判断副对角线是否五子连线
        int count = 0;
        for (int i = row, j = col; i >= 0 && j < width; i--, j++) {
            if (chess[i][j] != chessColor) {
                break;
            }
            count++;
        }
        for (int i = row + 1, j = col - 1; i < height && j >= 0; i++, j--) {
            if (chess[i][j] != chessColor) {
                break;
            }
            count++;
        }
        if (count >= 5) {
            return false;
        }
        return true;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCellLen() {
        return cellLen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        localSide = color.compareTo("White") == 0 ? 'W' : 'B';
    }

    public char getLocalSide() {
        return localSide;
    }

    public char getFlag() {
        return flag;
    }

    public char[][] getChess() {
        return chess;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

}
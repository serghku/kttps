package com.tivi.homework.model;

import java.util.Arrays;
import java.util.Random;

public class Game {

    private char[] field;

    private boolean firstUser;

    public Game() {
        this.field = createField();
        this.firstUser = randomBoolean();
    }

    public void changePosition(int pos){
        if (firstUser){
            field[pos] = 'X';
        }
        else {
            field[pos] = 'O';
        }
        firstUser = !firstUser;
    }

    public char whoWon(){
        char row1 = checkField(0,1,2);
        char row2 = checkField(3,4,5);
        char row3 = checkField(6,7,8);
        char col1 = checkField(0,3,6);
        char col2 = checkField(1,4,7);
        char col3 = checkField(2,5,8);
        char diag1 = checkField(0,4,8);
        char diag2 = checkField(2,4,6);
        char[] array = {row1,row2,row3,col1,col2,col3,diag1,diag2};
        for (char symbol : array){
            if (symbol != 'N'){
                return symbol;
            }
        }
        return 'N';
    }

    private char checkField(int p1, int p2, int p3) {
        if (field[p1] == field[p2] &&
                field[p2] == field[p3] &&
                field[p1] != ' ') {
            return field[p1];
        }
        return 'N';
    }

    private boolean randomBoolean(){
        return new Random().nextBoolean();
    }

    public boolean isFinished(){
        return whoWon() != 'N';
    }

    public char[] getField() {
        return field;
    }

    public void setField(char[] field) {
        this.field = field;
    }

    public boolean isFirstUser() {
        return firstUser;
    }

    public void setFirstUser(boolean firstUser) {
        this.firstUser = firstUser;
    }

    private char[] createField(){
        return new char[]{ ' ',' ',' ',
                ' ',' ',' ',
                ' ',' ',' '};
    }


    public String toStringHTMLinRows(int row){
        StringBuilder result = new StringBuilder();
        int index = 0;
        if (row == 1){
            for (index = 0; index< 3; index++){
                result.append("|").append(field[index]);
            }
        }
        if (row == 2){
            for (index = 3; index< 6; index++){
                result.append("|").append(field[index]);
            }

        }
        if (row ==3){
            for (index = 6; index< 9; index++){
                result.append("|").append(field[index]);
            }
        }
        result.append("|");
        return String.valueOf(result);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (char ar : field){
            result.append("|").append(ar);
            i++;
            if (i % 3 == 0){
                result.append("|");
                result.append("\n");
            }
        }
        return String.valueOf(result);
    }
}
package com.example.xox;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView P1Score,P2Score, Status;
    private Button [] buttons = new Button[9];
    private Button Reset;

    private int P1ScoreCount, P2ScoreCount, roundCount;
    boolean activePlayer;                                  //p1 => 0,   p2 => 1,   empty = 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};
    int [][] winPos = {
            {0,1,2},{3,4,5},{6,7,8},    //redovi
            {0,3,6},{1,4,7},{2,5,8},    //kolone
            {0,4,8},{2,4,6}             //dijagonale
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P1Score = (TextView) findViewById(R.id.playerOneScore);
        P2Score = (TextView) findViewById(R.id.playerTwoScore);
        Status = (TextView) findViewById(R.id.playerStatus);

        Reset = (Button) findViewById(R.id.reset);
        for(int i = 0; i < buttons.length; i++){
            String buttonID = "btn_" + i;
            int resourceID=getResources().getIdentifier(buttonID,"id",getPackageName());
            buttons[i]= (Button)findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }
        roundCount=0;
        P1ScoreCount=0;
        P2ScoreCount=0;
        activePlayer=true;
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")) {
        return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length()));
        if(activePlayer){
            ((Button)v).setText("X");
            ((Button)v).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer]=0;
        }else{
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer]=1;
        }
        roundCount++;

        if(checkWin()){
            if(activePlayer){
                P1ScoreCount++;
                updateScore();
                Toast.makeText(this,"Player One Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }else{
                P2ScoreCount++;
                updateScore();
                Toast.makeText(this,"Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        }else if(roundCount== 9){
            Toast.makeText(this,"Draw!", Toast.LENGTH_SHORT).show();
            playAgain();
        }else{
            activePlayer = !activePlayer;
        }
        if(P1ScoreCount>P2ScoreCount){
            Status.setText("Player one is winning");
        }else if(P1ScoreCount<P2ScoreCount){
            Status.setText("Player two is winning");
        }else{
            Status.setText("");
        }

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                P1ScoreCount=0;
                P2ScoreCount=0;
                Status.setText("");
                updateScore();
            }
        });
    }

    public boolean checkWin(){
        boolean winneResult = false;

        for(int [] winningPosition : winPos){
            if(gameState[winningPosition[0]]==gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]]==gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]]!=2){
                winneResult=true;
            }
        }
        return winneResult;
    }

    public void updateScore(){
        P1Score.setText(Integer.toString(P1ScoreCount));
        P2Score.setText(Integer.toString(P2ScoreCount));
    }

    public void playAgain(){
        roundCount = 0;
        activePlayer = true;

        for(int i = 0; i< buttons.length; i++){
            gameState[i]=2;
            buttons[i].setText("");
        }
    }
}
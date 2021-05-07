package com.example.thechancesudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var gameBoard: SudokuBoard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameBoard = findViewById(R.id.SudokuBoard)

    }

    fun btnClick(view: View) {
        val btn = view as Button
        when (btn.id) {
            R.id.button1 -> Solver.setNumberPos(1)
            R.id.button2 -> Solver.setNumberPos(2)
            R.id.button3 -> Solver.setNumberPos(3)
            R.id.button4 -> Solver.setNumberPos(4)
            R.id.button5 -> Solver.setNumberPos(5)
            R.id.button6 -> Solver.setNumberPos(6)
            R.id.button7 -> Solver.setNumberPos(7)
            R.id.button8 -> Solver.setNumberPos(8)
            R.id.button9 -> Solver.setNumberPos(9)
        }
        gameBoard?.invalidate()
    }

    fun solveBtnClick(view: View) {
        val btn = view as Button
        if (btn.text.equals(getString(R.string.Solve))){

            Solver.getEmptyBoxIndexes()
//            val solveBoardThread: SolveBoardThread = SolveBoardThread()
//            Thread(solveBoardThread).run()

            Solver.solve(gameBoard)
            gameBoard?.invalidate()

            btn.text = getString(R.string.Clear)

        }
        else{
            Solver.resetBoard()
            gameBoard?.invalidate()
            btn.text = getString(R.string.Solve)
        }
    }



//    inner class SolveBoardThread : Runnable{
//
//        override fun run() {
//            Solver.solve(gameBoard)
//        }
//    }
}
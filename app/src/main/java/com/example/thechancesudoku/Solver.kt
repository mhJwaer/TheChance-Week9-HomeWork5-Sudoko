package com.example.thechancesudoku

object Solver {

        private val board = Array(9) { Array(9) { 0 } }
//    val emptyBoxIndex = ArrayList<ArrayList<Objects>>()
    private var selectedRow: Int? = null
    private var selectedColumn: Int? = null
//    private val board = Array(9) { IntArray(9) }
    private var emptyBoxIndex: ArrayList<ArrayList<Any>>? = null

    init {
        selectedRow = -1
        selectedColumn = -1
//        for (r in 0 until 9) {
//            for (c in 0 until 9) {
//                board[r][c] = 0
//            }
//        }

        emptyBoxIndex = ArrayList()


    }


    fun setSelectedRow(r: Int) {
        this.selectedRow = r
    }

    fun setSelectedColumn(c: Int) {
        this.selectedColumn = c
    }

    fun getSelectedRow(): Int? = selectedRow
    fun getSelectedColumn(): Int? = selectedColumn

    fun getEmptyBoxIndexes() {
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (this.board[r][c] == 0) {
                    this.emptyBoxIndex?.add(ArrayList())
                    this.emptyBoxIndex?.get(this.emptyBoxIndex!!.size - 1)?.add(r)
                    this.emptyBoxIndex?.get(this.emptyBoxIndex!!.size - 1)?.add(c)
                }
            }
        }
    }

    private fun check(raw: Int, col: Int): Boolean {

        if (this.board[raw][col] > 0) {
            for (i in 0 until 9) {
                if (this.board[i][col] == this.board[raw][col] && raw != i) {
                    return false
                }
                if (this.board[raw][i] == this.board[raw][col] && col != i) {
                    return false
                }
            }

            val rawBox: Int = raw / 3
            val colBox: Int = col / 3

            for (r in rawBox * 3 until rawBox * 3 + 3) {
                for (c in colBox * 3 until colBox * 3 + 3) {
                    if (this.board[r][c] == this.board[raw][col] && raw != r && col != c) {
                        return false
                    }
                }
            }
        }

        return true
    }

    fun solve(display: SudokuBoard?): Boolean {
        var raw: Int = -1
        var col: Int = -1

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (this.board[r][c] == 0) {
                    raw = r
                    col = c
                    break
                }
            }
        }

        if (raw == -1 || col == -1) return true

        for (i in 1 until 10){
            this.board[raw][col] = i
            display?.invalidate()

            //recursive call solve method
            if (check(raw, col)){
                if (solve(display)){
                    return true
                }
            }
            //backtracking
            this.board[raw][col] = 0
        }

            return false
    }

    fun resetBoard(){
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                board[r][c] = 0
            }
        }

        emptyBoxIndex = ArrayList()
    }

    fun setNumberPos(num: Int) {
        if (this.selectedRow != -1 && this.selectedColumn != -1) {
            //if user clicks tow times same number -> remove the num
            if (this.board[this.selectedRow!! - 1][this.selectedColumn!! - 1] == num) {
                this.board[this.selectedRow!! - 1][this.selectedColumn!! - 1] = 0
            } else {
                this.board[this.selectedRow!! - 1][this.selectedColumn!! - 1] = num
            }
        }
    }


    fun getBoardArray() = this.board
    fun getEmptyBoxIndexArray() = this.emptyBoxIndex!!
//    fun getSolver(): Solver = this.solver
}
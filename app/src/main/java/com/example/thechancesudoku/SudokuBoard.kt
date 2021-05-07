package com.example.thechancesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.ceil
import kotlin.math.min

class SudokuBoard(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var boardColor: Int = 0
    private var cellFillColor: Int = 0
    private var cellsHighlightColor: Int = 0
    private var letterColor: Int = 0
    private var letterColorSolve: Int = 0
    private val boardColorPaint = Paint()
    private val cellFillColorPaint = Paint()
    private val cellsHighlightColorPaint = Paint()
    private val letterPaint = Paint()
    private val letterPaintBounds: Rect = Rect()
    private var cellSize: Int = 0
//    private val solver: Solver = Solver()

    init {
        val a: TypedArray =
            context!!.theme.obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0)
        try {
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor, 0)
            cellFillColor = a.getInt(R.styleable.SudokuBoard_cellFillColor, 0)
            cellsHighlightColor = a.getInt(R.styleable.SudokuBoard_cellsHighlightColor, 0)
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor,0)
            letterColorSolve = a.getInteger(R.styleable.SudokuBoard_letterColorSolve,0)

        } finally {
            a.recycle()
        }
    }

    @Override
    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, height)

        val dimensions = min(this.measuredWidth, this.measuredHeight)
        cellSize = dimensions / 9

        //set dimensions of the view
        setMeasuredDimension(dimensions, dimensions)
    }

    @Override
    override fun onDraw(canvas: Canvas?) {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 16F
        boardColorPaint.color = boardColor
        boardColorPaint.isAntiAlias = true

        cellFillColorPaint.style = Paint.Style.FILL
        boardColorPaint.isAntiAlias = true
        cellFillColorPaint.color = cellFillColor

        cellsHighlightColorPaint.style = Paint.Style.FILL
        boardColorPaint.isAntiAlias = true
        cellsHighlightColorPaint.color = cellsHighlightColor

        letterPaint.style = Paint.Style.FILL
        letterPaint.isAntiAlias = true
        letterPaint.color = letterColor

        colorCell(canvas, Solver.getSelectedRow()!!, Solver.getSelectedColumn()!!)
        canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat(), boardColorPaint)
        drawBoard(canvas)
        drawNumbers(canvas)

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val isValid: Boolean
        val x: Float = event!!.x
        val y: Float = event.y

        isValid = if (event.action == MotionEvent.ACTION_DOWN) {
            Solver.setSelectedRow((ceil((y / cellSize).toDouble())).toInt())
            Solver.setSelectedColumn ((ceil((x / cellSize).toDouble())).toInt())
            true
        } else {
            false
        }

        return isValid
    }


    private fun drawNumbers(canvas: Canvas?){
        letterPaint.textSize = cellSize.toFloat()
        for (r in 0 until 9){
            for (c in 0 until 9){
                if (Solver.getBoardArray()[r][c] != 0){
                    val text = Solver.getBoardArray()[r][c].toString()
                    letterPaint.getTextBounds(text, 0, text.length, letterPaintBounds)
                    val width = letterPaint.measureText(text)
                    val height = letterPaintBounds.height()

                    canvas?.drawText(text,((c*cellSize) + ((cellSize - width) /2)),
                        ((r*cellSize + cellSize) - ((cellSize - height)/2)).toFloat(), letterPaint )
                }
            }
        }

        letterPaint.color = letterColorSolve
        for (letter in Solver.getEmptyBoxIndexArray()){
            val r: Int = letter[0] as Int
            val c: Int = letter[1] as Int

            val text = Solver.getBoardArray()[r][c].toString()

            letterPaint.getTextBounds(text, 0, text.length, letterPaintBounds)
            val width = letterPaint.measureText(text)
            val height = letterPaintBounds.height()

            canvas!!.drawText(text,(c*cellSize) + ((cellSize - width) /2),
                ((r*cellSize + cellSize) - ((cellSize - height)/2)).toFloat(), letterPaint )
        }
    }

    private fun colorCell(canvas: Canvas?, r: Int, c: Int) {
        if (Solver.getSelectedColumn() != -1 && Solver.getSelectedRow() != -1) {
            canvas!!.drawRect(
                ((c - 1) * cellSize).toFloat(),
                0f,
                (c * cellSize).toFloat(),
                (cellSize * 9).toFloat(),
                cellsHighlightColorPaint
            )

            canvas.drawRect(
                0f,
                ((r - 1) * cellSize).toFloat(),
                (cellSize * 9).toFloat(),
                (r * cellSize).toFloat(),
                cellsHighlightColorPaint
            )

            canvas.drawRect(
                ((c - 1) * cellSize).toFloat(),
                ((r - 1) * cellSize).toFloat(),
                (c * cellSize).toFloat(),
                (r * cellSize).toFloat(),
                cellFillColorPaint
            )
        }

        invalidate()
    }

    private fun drawThickLines() {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 10F
        boardColorPaint.color = boardColor
    }

    private fun drawThinLines() {
        boardColorPaint.style = Paint.Style.STROKE
        boardColorPaint.strokeWidth = 4F
        boardColorPaint.color = boardColor
    }

    private fun drawBoard(canvas: Canvas?) {
        for (c in 0 until 10) {
            if (c % 3 == 0) drawThickLines()
            else drawThinLines()

            canvas!!.drawLine(
                (cellSize * c).toFloat(), 0f,
                (cellSize * c).toFloat(), width.toFloat(), boardColorPaint
            )
        }

        for (r in 0 until 10) {
            if (r % 3 == 0) drawThickLines()
            else drawThinLines()

            canvas!!.drawLine(
                0f, (cellSize * r).toFloat(),
                width.toFloat(), (cellSize * r).toFloat(), boardColorPaint
            )
        }
    }
}
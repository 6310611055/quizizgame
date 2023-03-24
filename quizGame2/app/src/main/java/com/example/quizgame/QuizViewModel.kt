package com.example.quizgame

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _selectedOption = mutableStateOf<String?>(null)
    val selectedOption: State<String?> = _selectedOption

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private var gameover: Boolean = false

    private val questions = listOf(
        QuizQuestion(
            "What is the capital of France?",
            listOf("London", "Paris", "Madrid"),
            "Paris"
        ),
        QuizQuestion(
            "What is the largest country in the world by area?",
            listOf("China", "Russia", "USA"),
            "Russia"
        ),
        QuizQuestion(
            "What is the currency of Japan?",
            listOf("Yen", "Dollar", "Euro"),
            "Yen"
        )
    )

    val currentQuestion: QuizQuestion
        get() = questions[currentQuestionIndex.value]

    val isLastQuestion: Boolean
        get() = currentQuestionIndex.value == questions.lastIndex

    fun selectOption(option: String) {
        _selectedOption.value = option
    }

    fun nextQuestion() {
        if (_selectedOption.value == currentQuestion.correctAnswer) {
            _score.value += 1
        }
        _selectedOption.value = null
        if (!isLastQuestion) {
            _currentQuestionIndex.value += 1
        }
    }

    fun restart() {
        _currentQuestionIndex.value = 0
        _selectedOption.value = null
        _score.value = 0
    }
}

class QuizQuestion(s: String, listOf: List<String>, s1: String) {

    val correctAnswer: Any?
}

package com.example.quizgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

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


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: QuizViewModel = viewModel()
            val currentQuestion = viewModel.currentQuestion
            QuizScreen(
                question = currentQuestion.question,
                options = currentQuestion.options,
                selectedOption = viewModel.selectedOption.value,
                onOptionSelected = { viewModel.selectOption(it) }
            )
        }
    }
}

@Composable
fun QuizScreen(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .background(
                        if (option == selectedOption) Color.LightGray else Color.White
                    )
                    .padding(16.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = option,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Button(
            onClick = {
                onOptionSelected(selectedOption ?: "")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (selectedOption == null) "Next" else "Submit")
        }
    }
}



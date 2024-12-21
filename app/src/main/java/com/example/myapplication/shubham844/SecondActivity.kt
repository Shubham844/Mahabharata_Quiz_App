package com.example.myapplication.shubham844

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class SecondActivity : AppCompatActivity() {

    private var currentQuestion = 0
    private val questions = 50
    private lateinit var progressBar: ProgressBar
    private var rewardedAd: RewardedAd? = null

    private var questionList: ArrayList<QuestionData>? = null

    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button

    private lateinit var allQuestions: List<QuestionData>
    private lateinit var adActivity: View
    private lateinit var btnStartOver: Button
    private lateinit var btnWatchAd: Button

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        MobileAds.initialize(this) {}

        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        adActivity = findViewById(R.id.ad_activity)
        adActivity.visibility = View.GONE

        btnStartOver = adActivity.findViewById(R.id.btn_start_over)
        btnWatchAd = adActivity.findViewById(R.id.btn_watch_ad)

        progressBar = findViewById(R.id.progressBar)
        progressBar.max = questions

        val data = setData

        allQuestions = data.getQuestion()

        questionList = data.getQuestion()
        var que = questionList!![0]
        textView3.text = que.question

        btn2.setOnClickListener { checkAnswer(0) }
        btn3.setOnClickListener { checkAnswer(1) }
        btn4.setOnClickListener { checkAnswer(2) }
        btn5.setOnClickListener { checkAnswer(3) }

        btn7.setOnClickListener {
            if (currentQuestion > 0) {
                currentQuestion--
                updateOptions(currentQuestion)
                showQuestion(currentQuestion)
            } else {
                Toast.makeText(this, "First question", Toast.LENGTH_LONG).show()
            }
        }

        btn6.setOnClickListener {
            if (currentQuestion < allQuestions.size - 1) {
                currentQuestion++
                updateOptions(currentQuestion)
                showQuestion(currentQuestion)
            } else {
                Toast.makeText(this, "Last question", Toast.LENGTH_LONG).show()
            }
        }

        btnStartOver.setOnClickListener {
            currentQuestion = 0
            showQuestion(currentQuestion)
            adActivity.visibility = View.GONE
            enableNavigationButtons()
        }

        btnWatchAd.setOnClickListener {
            if (rewardedAd != null) {
                rewardedAd?.show(this, OnUserEarnedRewardListener {
                    adActivity.visibility = View.GONE
                    Toast.makeText(this, "You have earned the reward!", Toast.LENGTH_SHORT).show()
                    loadRewardedAd()
                    enableNavigationButtons()
                })
            } else {
                Toast.makeText(this, "Ad is not ready yet, please try again later.", Toast.LENGTH_SHORT).show()
                loadRewardedAd()
            }
        }

        showQuestion(currentQuestion)
        updateOptions(currentQuestion)
        loadRewardedAd()
    }

    private fun checkAnswer(selectedOption: Int) {
        val answer = allQuestions[currentQuestion].answer

        btn2.isEnabled = false
        btn3.isEnabled = false
        btn4.isEnabled = false
        btn5.isEnabled = false

        btn6.isEnabled = false
        btn7.isEnabled = false

        val duration = 80
        if (selectedOption == answer) {
            highlightCorrectOption(selectedOption)

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                Toast.makeText(this, "Correct\uD83D\uDE0E", Toast.LENGTH_SHORT).show()
                enableNavigationButtons()
            }, duration.toLong())
        } else {
            highlightIncorrectOption(selectedOption)
            highlightCorrectOption(answer)

            adActivity.visibility = View.VISIBLE
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            btn2.isEnabled = true
            btn3.isEnabled = true
            btn4.isEnabled = true
            btn5.isEnabled = true
        }, duration.toLong())
    }

    private fun highlightCorrectOption(option: Int) {
        when (option) {
            0 -> btn2.setBackgroundColor(Color.GREEN)
            1 -> btn3.setBackgroundColor(Color.GREEN)
            2 -> btn4.setBackgroundColor(Color.GREEN)
            3 -> btn5.setBackgroundColor(Color.GREEN)
        }
    }

    private fun highlightIncorrectOption(option: Int) {
        when (option) {
            0 -> btn2.setBackgroundColor(Color.RED)
            1 -> btn3.setBackgroundColor(Color.RED)
            2 -> btn4.setBackgroundColor(Color.RED)
            3 -> btn5.setBackgroundColor(Color.RED)
        }
    }

    private fun resetButtonColors() {
        btn2.setBackgroundColor(Color.parseColor("#008577"))
        btn3.setBackgroundColor(Color.parseColor("#008577"))
        btn4.setBackgroundColor(Color.parseColor("#008577"))
        btn5.setBackgroundColor(Color.parseColor("#008577"))
    }

    private fun showQuestion(currentQuestion: Int) {
        resetButtonColors()

        textView3.text = allQuestions[currentQuestion].question
        progressBar.progress = currentQuestion + 1
        updateOptions(currentQuestion)

        val currentOptions = allQuestions[currentQuestion].options
        btn2.text = currentOptions[0]
        btn3.text = currentOptions[1]
        btn4.text = currentOptions[2]
        btn5.text = currentOptions[3]

        btn7.isEnabled = currentQuestion > 0
        btn7.setBackgroundColor(if (currentQuestion == 0) Color.parseColor("#FFD580") else Color.parseColor("#F19F27"))

        btn6.isEnabled = currentQuestion < allQuestions.size - 1
        btn6.setBackgroundColor(if (currentQuestion == allQuestions.size - 1) Color.parseColor("#FFD580") else Color.parseColor("#F19F27"))

        btn2.isEnabled = true
        btn3.isEnabled = true
        btn4.isEnabled = true
        btn5.isEnabled = true
    }

    private fun updateOptions(questionNumber: Int) {
        val questions = setData.getQuestion()
        if (questionNumber >= 0 && questionNumber < questions.size) {
            val currentOptions = questions[questionNumber].options
            btn2.text = currentOptions[0]
            btn3.text = currentOptions[1]
            btn4.text = currentOptions[2]
            btn5.text = currentOptions[3]
        }
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    val errorMsg = adError.message
                    val errorCode = adError.code
                    val responseInfo = adError.responseInfo
                    Toast.makeText(
                        this@SecondActivity,
                        "Ad failed to load: $errorMsg (Code: $errorCode)",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("AdLoadError", "Ad failed to load: $errorMsg (Code: $errorCode)")
                    Log.e("AdLoadError", "Response Info: $responseInfo")
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("AdLoadSuccess", "Ad loaded successfully")
                }
            }
        )
    }

    private fun enableNavigationButtons() {
        btn6.isEnabled = currentQuestion < allQuestions.size - 1
        btn7.isEnabled = currentQuestion > 0
    }
}


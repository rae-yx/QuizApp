package com.example.geoquiz

object Constants {

    const val USER_NAME: String = "username"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    const val CHEAT_ATTEMPTS: String = "cheat_attempts"

    fun getQuestions(): ArrayList<Questions>{
        val questionList = ArrayList<Questions>()
        val q1 = Questions(
            1,
            "What is the largest continent by area?",
            "Africa",
            "Asia",
            "North America",
            "Europe",
            2
        )
        questionList.add(q1)

        val q2 = Questions(
            2,
            "Which country has the largest population in the world?",
            "India",
            "United States",
            "China",
            "Russia",
            3
        )
        questionList.add(q2)

        val q3 = Questions(
            3,
            "Which is the longest river in the world?",
            "Amazon River",
            "Nile River",
            "Yangtze River",
            "Mississippi River",
            2
        )
        questionList.add(q3)

        val q4 = Questions(
            4,
            "Mount Everest is located in which mountain range?",
            "Andes",
            "Rockies",
            "Himalayas",
            "Alps",
            3
        )
        questionList.add(q4)

        val q5 = Questions(
            5,
            "Which desert is the largest hot desert in the world?",
            "Gobi Desert",
            "Arabian Desert",
            "Kalahari Desert",
            "Sahara Desert",
            4
        )
        questionList.add(q5)

        val q6 = Questions(
            6,
            "Which ocean is the largest by surface area?",
            "Atlantic Ocean",
            "Indian Ocean",
            "Arctic Ocean",
            "Pacific Ocean",
            4
        )
        questionList.add(q6)

        val q7 = Questions(
            7,
            "Which country has the most natural lakes?",
            "United States",
            "Canada",
            "Russia",
            "Brazil",
            2
        )
        questionList.add(q7)

        val q8 = Questions(
            8,
            "What is the smallest country in the world by area?",
            "Monaco",
            "Nauru",
            "Vatican City",
            "San Marino",
            3
        )
        questionList.add(q8)

        val q9 = Questions(
            9,
            "Which city is known as the 'City of Light'?",
            "New York",
            "Paris",
            "Tokyo",
            "London",
            2
        )
        questionList.add(q9)

        val q10 = Questions(
            10,
            "Which country is also known as the 'Land Down Under'?",
            "New Zealand",
            "South Africa",
            "Australia",
            "Argentina",
            3
        )
        questionList.add(q10)

        return questionList
    }
}
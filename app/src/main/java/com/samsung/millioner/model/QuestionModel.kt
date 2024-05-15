package com.samsung.millioner.model

import com.google.firebase.firestore.DocumentId

class QuestionModel {
    @DocumentId
    var questionId: String? = null
    var question: String? = null
    var answer: String? = null
    var option_a: String? = null
    var option_b: String? = null
    var option_c: String? = null
    var option_d: String? = null

    constructor()

    constructor(question: String, questionId: String, answer: String, option_a: String, option_b: String, option_c: String, option_d: String) {
        this.questionId = questionId
        this.question = question
        this.answer = answer
        this.option_a = option_a
        this.option_b = option_b
        this.option_c = option_c
        this.option_d = option_d
    }
    override fun toString(): String {
        return "QuestionModel(questionId=$questionId, questionId=$question, answer=$answer, option_a=$option_a, option_b=$option_b, option_c=$option_c, option_d=$option_d)"
    }
}
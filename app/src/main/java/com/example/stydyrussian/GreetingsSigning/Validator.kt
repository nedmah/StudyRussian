package com.example.stydyrussian.GreetingsSigning

import java.util.regex.Pattern

class Validator {
    companion object {
        private const val PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%^&*()-_=+]+\$"
    }

    fun isLoginValid(login: String): Boolean {
        // Проверка наличия символов в логине
        return login.isNotEmpty()
    }

    fun isPasswordValid(password: String): Boolean {
        // Проверка наличия символов в пароле и его длины
        return password.length >= 6
    }

    fun arePasswordsMatching(password1: String, password2: String): Boolean {
        // Проверка на совпадение паролей
        return password1 == password2
    }

    fun containsInvalidCharacters(input: String): Boolean {
        // Проверка на наличие недопустимых символов в пароле
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(input)
        return !matcher.matches()
    }
}
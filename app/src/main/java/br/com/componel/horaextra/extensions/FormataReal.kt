package br.com.componel.horaextra.extensions

fun Double.toBr(): String {
    return  "R$ $this"
}
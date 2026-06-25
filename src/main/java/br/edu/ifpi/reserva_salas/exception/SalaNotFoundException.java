package br.edu.ifpi.reserva_salas.exception;


public class SalaNotFoundException extends RuntimeException {

    public SalaNotFoundException(String mensagem) {
        super(mensagem);
    }
}
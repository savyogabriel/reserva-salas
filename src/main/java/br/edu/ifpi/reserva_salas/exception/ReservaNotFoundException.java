package br.edu.ifpi.reserva_salas.exception;


public class ReservaNotFoundException extends RuntimeException {

    public ReservaNotFoundException(String mensagem) {
        super(mensagem);
    }
}
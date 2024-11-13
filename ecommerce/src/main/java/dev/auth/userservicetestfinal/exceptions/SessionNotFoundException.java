package dev.auth.userservicetestfinal.exceptions;

public class SessionNotFoundException extends RuntimeException{
      public SessionNotFoundException(){
            super(" Session not found! ");
      }
}

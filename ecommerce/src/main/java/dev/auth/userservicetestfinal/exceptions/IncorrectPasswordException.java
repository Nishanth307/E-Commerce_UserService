package dev.auth.userservicetestfinal.exceptions;

public class IncorrectPasswordException extends RuntimeException{
      public IncorrectPasswordException(){
            super("Password is incorrect!");
      }
}

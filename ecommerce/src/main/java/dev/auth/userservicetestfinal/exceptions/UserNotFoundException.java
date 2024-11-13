package dev.auth.userservicetestfinal.exceptions;

public class UserNotFoundException extends RuntimeException{
      public  UserNotFoundException(){
            super("User details not Found!");
      }
}

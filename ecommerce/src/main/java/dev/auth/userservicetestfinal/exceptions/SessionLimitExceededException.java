package dev.auth.userservicetestfinal.exceptions;

public class SessionLimitExceededException extends RuntimeException{
      public SessionLimitExceededException(){
            super("Number of sessions exeeded the limit(2) please logout from one of the sessions and try again!" );
      }
}

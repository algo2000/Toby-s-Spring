package springbook.user.service;

public class DuplicateUserIdException extends  RuntimeException
{
    public DuplicateUserIdException(Throwable cause)
    {
        super(cause);
    }
}

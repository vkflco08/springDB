package hello.jdbc.connection;

import hello.jdbc.repository.ex.MyDbException;

public class MyDuplicateKeyException extends MyDbException {
    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
/**
 * 데이터 중복에 대한 에러를 활용하기 위해 변환한 예외
 */

package online.devwiki.common.user.infrastructure.aop;

import org.springframework.http.HttpStatusCode;

public class ApiErrorResponse {

    private HttpStatusCode status;
    private String message;

    public ApiErrorResponse(HttpStatusCode status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public void setStatus(HttpStatusCode status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package utils;

public class RequestLog {

    private Request request;
    private Response response;

    class Request{

        private String method;
        private String path;
        private String contentType;
        private String length;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }
    }

    class Response{

        private Integer status;
        private String contentType;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(String method, String path, String contentType, String length) {
        this.request = new Request();
        this.request.setMethod(method);
        this.request.setPath(path);
        this.request.setContentType(contentType);
        this.request.setLength(length);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Integer status, String contentType) {
        this.response = new Response();
        this.response.setStatus(status);
        this.response.setContentType(contentType);
    }
}


package com.gymapp.utils;

public final class PathsConstants {
    private PathsConstants() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static class API {
        private API(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final String V1 = "/api/v1";

        public static class Auth {
            private Auth(){
                throw new IllegalStateException("Utility class cannot be instantiated");
            }
            public static final String BASE = V1 + "/auth";
            public static final String LOGIN = BASE + "/login";
            public static final String REFRESH = BASE + "/refresh";
            public static final String LOGOUT = BASE + "/logout";
        }

        public static final String TRAINEES = V1 + "/trainees";
        public static final String TRAINERS = V1 + "/trainers";
    }

    public static class Docs {
        private Docs(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final String SWAGGER_UI = "/swagger-ui/**";
        public static final String API_DOCS = "/v3/api-docs/**";
        public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    }

    public static class Frontend {
        private Frontend(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final String LOCAL = "http://localhost:5173";
    }

    public static class Methods {
        private Methods(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String PATCH = "PATCH";
        public static final String OPTIONS = "OPTIONS";
        public static final String HEAD = "HEAD";

        public static String[] getAllMethodsArray() {
            return new String[]{GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD};
        }
    }

    public static class Headers {
        private Headers(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";

        public static String[] getCorsAllowedHeadersArray() {
            return new String[]{AUTHORIZATION, CONTENT_TYPE};
        }
    }

    public static class Cors {
        private Cors(){
            throw new IllegalStateException("Utility class cannot be instantiated");
        }
        public static final boolean ALLOW_CREDENTIALS = true;
        public static final String ALL_PATHS = "/**";
    }

}
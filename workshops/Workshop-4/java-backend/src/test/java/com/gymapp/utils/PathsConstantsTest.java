package com.gymapp.utils;

import com.gymapp.utils.PathsConstants.API;
import com.gymapp.utils.PathsConstants.Docs;
import com.gymapp.utils.PathsConstants.Frontend;
import com.gymapp.utils.PathsConstants.Methods;
import com.gymapp.utils.PathsConstants.Headers;
import com.gymapp.utils.PathsConstants.Cors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PathsConstants Utility Class Tests")
class PathsConstantsTest {

    @Test
    void pathsConstants_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<PathsConstants> constructor = PathsConstants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }

    // ============================================================
    // API Paths Tests
    // ============================================================

    @Test
    void apiPaths_shouldBeCorrect() {
        assertEquals("/api/v1", API.V1);
        assertEquals("/api/v1/trainees", API.TRAINEES);
        assertEquals("/api/v1/trainers", API.TRAINERS);
    }

    @Test
    void authPaths_shouldBeCorrect() {
        assertEquals("/api/v1/auth", API.Auth.BASE);
        assertEquals("/api/v1/auth/login", API.Auth.LOGIN);
        assertEquals("/api/v1/auth/refresh", API.Auth.REFRESH);
        assertEquals("/api/v1/auth/logout", API.Auth.LOGOUT);
    }

    @Test
    void apiClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<API.Auth> authConstructor = API.Auth.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(authConstructor.getModifiers()));
        authConstructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) authConstructor::newInstance);

        Constructor<API> apiConstructor = API.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(apiConstructor.getModifiers()));
        apiConstructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) apiConstructor::newInstance);
    }

    // ============================================================
    // Docs Paths Tests
    // ============================================================

    @Test
    void docsPaths_shouldBeCorrect() {
        assertEquals("/swagger-ui/**", Docs.SWAGGER_UI);
        assertEquals("/v3/api-docs/**", Docs.API_DOCS);
        assertEquals("/swagger-ui.html", Docs.SWAGGER_UI_HTML);
    }

    @Test
    void docsClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<Docs> constructor = Docs.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }

    // ============================================================
    // Frontend Paths Tests
    // ============================================================

    @Test
    void frontendPaths_shouldBeCorrect() {
        assertEquals("http://localhost:5173", Frontend.LOCAL);
    }

    @Test
    void frontendClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<Frontend> constructor = Frontend.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }

    // ============================================================
    // Methods Tests
    // ============================================================

    @Test
    void methodsConstants_shouldBeCorrect() {
        assertEquals("GET", Methods.GET);
        assertEquals("POST", Methods.POST);
        assertEquals("PUT", Methods.PUT);
        assertEquals("DELETE", Methods.DELETE);
        assertEquals("PATCH", Methods.PATCH);
        assertEquals("OPTIONS", Methods.OPTIONS);
        assertEquals("HEAD", Methods.HEAD);
    }

    @Test
    void methods_getAllMethodsArray_shouldContainAllMethods() {
        String[] methodsArray = Methods.getAllMethodsArray();
        Set<String> expectedMethods = new HashSet<>(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        Set<String> actualMethods = new HashSet<>(Arrays.asList(methodsArray));

        assertEquals(expectedMethods.size(), methodsArray.length);
        assertEquals(expectedMethods, actualMethods);
    }

    @Test
    void methodsClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<Methods> constructor = Methods.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }

    // ============================================================
    // Headers Tests
    // ============================================================

    @Test
    void headersConstants_shouldBeCorrect() {
        assertEquals("Authorization", Headers.AUTHORIZATION);
        assertEquals("Content-Type", Headers.CONTENT_TYPE);
    }

    @Test
    void headers_getCorsAllowedHeadersArray_shouldContainCorrectHeaders() {
        String[] headersArray = Headers.getCorsAllowedHeadersArray();
        String[] expectedHeaders = new String[]{"Authorization", "Content-Type"};

        assertArrayEquals(expectedHeaders, headersArray);
    }

    @Test
    void headersClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<Headers> constructor = Headers.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }

    // ============================================================
    // Cors Tests
    // ============================================================

    @Test
    void corsConstants_shouldBeCorrect() {
        assertTrue(Cors.ALLOW_CREDENTIALS);
        assertEquals("/**", Cors.ALL_PATHS);
    }

    @Test
    void corsClasses_shouldBeNonInstantiable() throws NoSuchMethodException {
        Constructor<Cors> constructor = Cors.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, (Executable) constructor::newInstance);
    }
}
package com.github.dericksm.clients.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ApiErrorResponse {

    private String message;
}

package com.venhaserjava.orderms.controller.dto;

import java.util.List;

public record ApiResponse<T>(List<T> data,PaginationResponse pagination ) {

}

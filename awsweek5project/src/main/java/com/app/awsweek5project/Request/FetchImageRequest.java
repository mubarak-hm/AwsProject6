package com.app.awsweek5project.Request;

import org.springframework.ui.Model;

public record FetchImageRequest (int page, int size , Model model) {
}

package com.lab4.database_demo.controller;

import com.lab4.database_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/users")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Download the user's profile photo by ID",
            parameters = @Parameter(
                    name = "id",
                    description = "User ID",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer")
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully retrieved",
                    content = @Content(mediaType = "image/png")),
            @ApiResponse(responseCode = "400", description = "Error in request")
    })
    @GetMapping("/downloadPhoto/{id}")
    @PreAuthorize("hasAnyRole('CLIENT','VENDOR')")
    public ResponseEntity<Object> downloadUserPhoto(@PathVariable("id") Long id) {
        try {
            byte[] imageBytes = userService.getUserImage(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Add a profile photo for the user by ID",
            parameters = @Parameter(
                    name = "id",
                    description = "User ID",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer")
            ),
            requestBody = @RequestBody(
                    description = "Image file to upload (.png or .jpg)",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully saved"),
            @ApiResponse(responseCode = "400", description = "Invalid file type or request error")
    })
    @PostMapping("/userImage/{id}/add")
    @PreAuthorize("hasAnyRole('CLIENT','VENDOR')")
    public ResponseEntity<Object> addImage(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (imageFile.getOriginalFilename().contains(".png") || imageFile.getOriginalFilename().contains(".jpg")) {
                userService.addUserImage(id, imageFile);
                return ResponseEntity.ok("Image successfully saved");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

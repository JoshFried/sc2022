package com.jf.sc2022.controllers;

import com.jf.sc2022.dal.service.UserService;
import com.jf.sc2022.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping(path = "{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable final String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody final UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(), HttpStatus.ACCEPTED);
    }
}

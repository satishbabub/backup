package com.veracode.storypointer.controller;

import com.sun.istack.NotNull;
import com.veracode.storypointer.service.StoryPointingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/veracode/storypoint/")
public class AdminController {

    private StoryPointingService service;

    @Autowired
    public AdminController(StoryPointingService service) {
        this.service = service;
    }

    @PostMapping(value = "/session", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createSession(@NotNull @RequestParam("name") String name,
                                                @NotNull @RequestParam("team") String team) {
        return new ResponseEntity<Object>(service.createSession(team, name), null, HttpStatus.CREATED);
    }

    @PostMapping(value = "/session/user", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> joinSession(@NotNull @RequestParam("name") String name,
                                              @NotNull @RequestParam("team") String team) {
        return new ResponseEntity<>(service.addUserToSession(team, name), null, HttpStatus.OK);
    }

    @GetMapping(value = "/project", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getProjects() {
        return new ResponseEntity<>(service.getProjects(), null, HttpStatus.OK);
    }

    @GetMapping(value = "/project/{storyId}", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getStoryDetail(@NotNull @PathVariable("storyId") String storyId) {
        return new ResponseEntity<>(service.getStoryDtl(storyId), null, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getUserInfo(@NotNull @RequestParam("email") String email) {
        return new ResponseEntity<>(service.getUserInfo(email), null, HttpStatus.OK);
    }

    @GetMapping(value = "/session/user", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getUsersInSession(@NotNull @RequestParam("team") String team) {
        return new ResponseEntity<>(service.getUsersFromSession(team), null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/session/user", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> leaveSession(@NotNull @RequestParam("name") String name,
                                               @NotNull @RequestParam("team") String team) {
        return new ResponseEntity<>(service.removeUserFromSession(team, name), null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/session")
    public ResponseEntity<Object> closeSession(@NotNull @RequestParam("team") String team,
                                               @NotNull @RequestParam("name") String name) {
        service.closeSession(team, name);
        return new ResponseEntity<>(String.format(" %s team story pointing session closed...", team), null, HttpStatus.OK);
    }

    @PostMapping(value = "/user/{storyId}")
    public ResponseEntity<Object> addUserEstimate(@NotNull @PathVariable("storyId") String storyId,
                                                  @NotNull @RequestParam("estimate") Integer estimate,
                                                  @NotNull @RequestParam("name") String name,
                                                  @NotNull @RequestParam("team") String team) {
        return new ResponseEntity<>(service.addUserEstimate(storyId, estimate, name, team), null, HttpStatus.OK);
    }

    @PostMapping(value = "{storyId}")
    public ResponseEntity<Object> initiateStoryPointing(@NotNull @PathVariable("storyId") String storyId,
                                                        @NotNull @RequestParam("name") String name,
                                                        @NotNull @RequestParam("team") String team) {
        return new ResponseEntity<>(service.initiateStoryPointing(storyId, name, team), null, HttpStatus.OK);
    }

    @GetMapping(value = "{storyId}")
    public ResponseEntity<Object> getUserEstimates(@NotNull @PathVariable("storyId") String storyId) {
        return new ResponseEntity<>(service.getUserEstimatesForStory(storyId), null, HttpStatus.OK);
    }

    @PutMapping(value = "{storyId}")
    public ResponseEntity<Object> updateFinalEstimate(@NotNull @PathVariable("storyId") String storyId,
                                                      @NotNull @RequestParam("name") String name,
                                                      @NotNull @RequestParam("team") String team,
                                                      @NotNull @RequestParam("estimate") Integer estimate) {
        return new ResponseEntity<>(service.updateFinalEstimate(storyId, name, team, estimate), null, HttpStatus.OK);
    }

   /* private long generateSessionId() {
        int max = 50000;
        int min = 1;
        SecureRandom rand = new SecureRandom();
        rand.setSeed(new Date().getTime());
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }*/
}


package com.veracode.storypointer.service;

import com.veracode.storypointer.entity.Session;
import com.veracode.storypointer.entity.SessionUser;
import com.veracode.storypointer.entity.StoryPoint;
import com.veracode.storypointer.entity.UserStoryPoint;
import com.veracode.storypointer.exception.ValidationException;
import com.veracode.storypointer.model.Comment;
import com.veracode.storypointer.model.JiraIssue;
import com.veracode.storypointer.model.Team;
import com.veracode.storypointer.model.UserInfo;
import com.veracode.storypointer.repository.SessionRepository;
import com.veracode.storypointer.repository.SessionUserRepository;
import com.veracode.storypointer.repository.StoryPointsRepository;
import com.veracode.storypointer.repository.UserStoryPointsRepository;
import com.veracode.storypointer.util.Role;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;

@Service
public class StoryPointingService {

    private SessionRepository sessionRepo;
    private SessionUserRepository sessionUserRepo;
    private UserStoryPointsRepository userStoryPointsRepo;
    private StoryPointsRepository storyPointsRepo;
    private RestTemplate restTemplate = new RestTemplate();
    private String jiraUpdateURL = "https://veracode.atlassian.net/rest/api/3/issue/%s";
    private String jiraLoadIssues = "https://veracode.atlassian.net/rest/agile/1.0/board/%s/backlog?jql=issuetype=Story&fields=reporter&fields=description&fields=summary&fields=status&fields=priority&fields=comment&fields=issuelinks";//"https://veracode.atlassian.net/rest/agile/1.0/board/%s/backlog?jql=issuetype=Story";
    private String jiraLoadProjects = "https://veracode.atlassian.net/rest/api/3/project/search";
    private String jiraUserInfo = "https://veracode.atlassian.net/rest/api/3/user/search?query=%s";
    private String jiraStoryDtl = "https://veracode.atlassian.net/rest/api/3/issue/%s";

    private Set<Integer> validateStoryPoints = Set.of(1, 2, 3, 5, 8, 13);

    @Autowired
    public StoryPointingService(SessionRepository sessionRepo,
                                SessionUserRepository sessionUserRepo,
                                UserStoryPointsRepository userStoryPointsRepo,
                                StoryPointsRepository storyPointsRepo) {
        this.sessionRepo = sessionRepo;
        this.sessionUserRepo = sessionUserRepo;
        this.userStoryPointsRepo = userStoryPointsRepo;
        this.storyPointsRepo = storyPointsRepo;
    }

    //public List<SessionUser> createSession(String team, String name) {
    @Transactional
    public List<JiraIssue> createSession(String team, String name) {
        Session activeSession = sessionRepo.findByTeam(team);
        if (activeSession != null) {
            throw new ValidationException(String.format("%s session exists created by %s", team, activeSession.getCreatedBy()));
        }
        Session session = new Session();
        session.setCreatedOn(new Date());
        session.setActive(Boolean.TRUE);
        session.setCreatedBy(name);
        session.setTeam(team);
        session = sessionRepo.save(session);
        addUserToSession(name, session.getTeam(), Role.ADMIN);
        return loadJIRAIssues("244");
        // return addUserToSession(name, session.getTeam(), Role.ADMIN);
    }

    public List<SessionUser> addUserToSession(String team, String name) {
        if (sessionRepo.findByTeam(team) == null) {
            throw new ValidationException(String.format("%s team session not found...", team));
        }
        if (sessionUserRepo.findBySessionIdAndUserName(team, name) != null) {
            throw new ValidationException(String.format("%s already joined the %s team session...", name, team));
        }
        return addUserToSession(name, team, Role.PARTICIPANT);
    }

    public List<SessionUser> getUsersFromSession(String team) {
        if (sessionRepo.findByTeam(team) == null) {
            throw new ValidationException(String.format("%s team session not found...", team));
        }
        return sessionUserRepo.findAllBySessionId(team);
    }

    @Transactional
    public List<SessionUser> removeUserFromSession(String team, String name) {
        if (sessionUserRepo.findBySessionIdAndUserName(team, name) == null) {
            throw new ValidationException(String.format("%s is not in %s team session or team session closed.", name, team));
        }
        sessionUserRepo.deleteByUserNameAndSessionId(name, team);
        return getUsersFromSession(team);
    }

    @Transactional
    public void closeSession(String team, String name) {
        SessionUser user = sessionUserRepo.findBySessionIdAndUserName(team, name);
        if (user == null || user.getUserRole() != Role.ADMIN) {
            throw new ValidationException(String.format("%s team session not found OR only Admin can close the session", team));
        }
        sessionRepo.deleteByTeam(team);
        sessionUserRepo.deleteAllBySessionId(team);
        userStoryPointsRepo.deleteAllByTeam(team);
        storyPointsRepo.deleteAllByTeam(team);
    }

    public List<UserStoryPoint> addUserEstimate(String storyId, Integer estimate, String name, String team) {
        checkAndFailIfNotValidEstimate(estimate);
        validateStoryNumber(storyId);
        UserStoryPoint userEstimate = userStoryPointsRepo.findByUserNameAndStoryId(name, storyId);
        if (userEstimate == null) {
            userEstimate = new UserStoryPoint();
            userEstimate.setUserName(name);
            userEstimate.setStoryId(storyId);
            userEstimate.setTeam(team);
        }
        userEstimate.setEstimate(estimate);
        userStoryPointsRepo.save(userEstimate);
        return getUserEstimatesForStory(storyId);
    }

    public List<UserStoryPoint> getUserEstimatesForStory(String storyId) {
        return userStoryPointsRepo.findAllByStoryId(storyId);
    }

    @Transactional
    public StoryPoint updateFinalEstimate(String storyId, String name, String team, Integer estimate) {
        checkAndFailIfNotAdminUser(name, team);
        checkAndFailIfNotValidEstimate(estimate);
        StoryPoint sp = storyPointsRepo.findByStoryId(storyId);
        if (sp == null)
            throw new ValidationException(String.format("Invalid story Id"));
        sp.setEstimate(estimate);
        sp.setActive(Boolean.FALSE);
        updateJIRAStory(storyId, estimate);
        return storyPointsRepo.save(sp);
    }

    public StoryPoint initiateStoryPointing(String storyId, String name, String team) {
        checkAndFailIfNotAdminUser(name, team);
        StoryPoint sp = storyPointsRepo.findByStoryId(storyId);
        if (sp != null) {
            sp.setActive(Boolean.TRUE);
            return storyPointsRepo.save(sp);
        }
        StoryPoint storyPoint = new StoryPoint();
        storyPoint.setStoryId(storyId);
        storyPoint.setActive(Boolean.TRUE);
        storyPoint.setTeam(team);
        return storyPointsRepo.save(storyPoint);
    }

    public UserInfo getUserInfo(String emailId) {
        String userDtl = restTemplate.exchange(String.format(jiraUserInfo, emailId),
                HttpMethod.GET,
                new HttpEntity<>(getJIRAHeaders()),
                String.class).getBody();
        UserInfo userInfo = new UserInfo();
        try {
            JSONArray jsonArray = new JSONArray(userDtl);
            JSONObject jsonObject = null;
            if (jsonArray.length() > 0)
                jsonObject = jsonArray.getJSONObject(0);
            userInfo.setName(jsonObject.getString("displayName"));
            userInfo.setEmailId(jsonObject.getString("emailAddress"));
            JSONObject avatars = jsonObject.optJSONObject("avatarUrls");
            if (avatars != null) {
                userInfo.setAvatarUrl(avatars.getString("48x48"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public List<Team> getProjects() {
        String projects = restTemplate.exchange(jiraLoadProjects,
                HttpMethod.GET,
                new HttpEntity<>(getJIRAHeaders()),
                String.class).getBody();
        JSONArray jsonArray = null;
        List<Team> teamList = new ArrayList<>();
        try {
            JSONObject resultsObj = new JSONObject(projects);
            jsonArray = resultsObj.getJSONArray("values");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Team team = new Team();
                team.setId(obj.getString("id"));
                team.setKey(obj.getString("key"));
                team.setName(obj.getString("name"));
                JSONObject avatars = obj.optJSONObject("avatarUrls");
                if (avatars != null) {
                    team.setAvatarUrl(avatars.getString("48x48"));
                }
                teamList.add(team);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teamList;
    }

    public JiraIssue getStoryDtl(String jiraId) {
        String jiraDtl = restTemplate.exchange(String.format(jiraStoryDtl, jiraId),
                HttpMethod.GET,
                new HttpEntity<>(getJIRAHeaders()),
                String.class).getBody();
        try {
            return extractStoryDtl(new JSONObject(jiraDtl));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JiraIssue> loadJIRAIssues(String jiraBoardId) {
        String jiraIssues = restTemplate.exchange(String.format(jiraLoadIssues, jiraBoardId),
                HttpMethod.GET,
                new HttpEntity<>(getJIRAHeaders()),
                String.class).getBody();
        JSONObject jsonObject = null;
        List<JiraIssue> issueList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(jiraIssues);
            JSONArray issues = jsonObject.getJSONArray("issues");
            for (int i = 0; i < issues.length(); i++) {
                issueList.add(extractStoryDtl(issues.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return issueList;
    }

    private JiraIssue extractStoryDtl(JSONObject obj) {
        JiraIssue jiraIssue = new JiraIssue();
        try {
            JSONObject fields = obj.getJSONObject("fields");
            jiraIssue.setJiraId(obj.getString("key"));
            jiraIssue.setSummary(fields.getString("summary"));
            jiraIssue.setDescription(fields.getString("description"));
            jiraIssue.setReportedBy(fields.getJSONObject("reporter").getString("displayName"));
            jiraIssue.setPriority(fields.getJSONObject("priority").getString("name"));
            jiraIssue.setStatus(fields.getJSONObject("status").getString("name"));
            JSONObject commentsObj = fields.optJSONObject("comment");
            if (commentsObj != null) {
                List<Comment> comments = new ArrayList<>();
                JSONArray commentsArray = commentsObj.getJSONArray("comments");
                for (int j = 0; j < commentsArray.length(); j++) {
                    Comment comment = new Comment();
                    comment.setComment(commentsArray.getJSONObject(j).getString("body"));
                    comment.setCommentedBy(commentsArray.getJSONObject(j).getJSONObject("updateAuthor").getString("displayName"));
                    comments.add(comment);
                }
                jiraIssue.setComments(comments);
            }
            JSONArray issueLinks = fields.optJSONArray("issuelinks");
            if (issueLinks.length() > 0) {
                List<String> blocks = new ArrayList<>();
                List<String> blockedBy = new ArrayList<>();
                for (int j = 0; j < issueLinks.length(); j++) {
                    JSONObject blockedByObj = issueLinks.getJSONObject(j).optJSONObject("inwardIssue");
                    JSONObject blocksObj = issueLinks.getJSONObject(j).optJSONObject("outwardIssue");
                    if (blockedByObj != null) {
                        blockedBy.add(blockedByObj.getString("key"));
                    }
                    if (blocksObj != null) {
                        blocks.add(blocksObj.getString("key"));
                    }
                }
                jiraIssue.setBlocks(blocks);
                jiraIssue.setBlockedBy(blockedBy);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jiraIssue;
    }

    private List<SessionUser> addUserToSession(String userName, String team, Role role) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setSessionId(team);
        sessionUser.setUserName(userName);
        sessionUser.setUserRole(role);
        sessionUserRepo.save(sessionUser);

        return sessionUserRepo.findAllBySessionId(team);
    }

    private void checkAndFailIfNotAdminUser(String userName, String team) {
        SessionUser user = sessionUserRepo.findBySessionIdAndUserName(team, userName);
        if (user == null || user.getUserRole() != Role.ADMIN) {
            throw new ValidationException(String.format("Only admin can start and finalize story points for a story"));
        }
    }

    private void checkAndFailIfNotValidEstimate(Integer estimate) {
        if (!validateStoryPoints.contains(estimate)) {
            throw new ValidationException(String.format("Invalid estimate, only values 1, 2, 3, 5, 8 and 13 are allowed"));
        }
    }

    private void validateStoryNumber(String storyId) {
        StoryPoint sp = storyPointsRepo.findByStoryId(storyId);
        if (sp == null || !sp.isActive()) {
            throw new ValidationException(String.format(" %s - Either not ready for pointing or pointing closed", storyId));
        }
    }

    private void updateJIRAStory(String storyId, Integer estimate) {
        {
            restTemplate.exchange(String.format(jiraUpdateURL, storyId),
                    HttpMethod.PUT,
                    new HttpEntity<>(updateStoryPointsJson(estimate), getJIRAHeaders()),
                    String.class);

        }
    }

    private String updateStoryPointsJson(Integer storyPoints) {
        String updateStoryPointsJson = "{\"fields\": { \"customfield_10026\": %s }}";
        return String.format(updateStoryPointsJson, storyPoints);
    }

    private HttpHeaders getJIRAHeaders() {
        String plainCreds = "sbalasu@veracode.com" + ":" + "GB0z8VOK85n5BGTVMuBXE01B";
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCreds.getBytes());
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }
}

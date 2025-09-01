package org.springframework.backend2exercise3.service;

import org.springframework.backend2exercise3.config.TrelloConfig;
import org.springframework.backend2exercise3.model.Task;
import org.springframework.backend2exercise3.model.TaskForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrelloService {

    private static final Logger logger = LoggerFactory.getLogger(TrelloService.class);

    private final TrelloConfig trelloConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public TrelloService(TrelloConfig trelloConfig) {
        this.trelloConfig = trelloConfig;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Hämtar kort från standard-listan (om konfigurerad)
     * @return Lista av kort som Task-objekt
     */
    public List<Task> getCardsFromDefaultList() {
        if (!trelloConfig.hasDefaultListId()) {
            logger.warn("Ingen standard-lista konfigurerad");
            return new ArrayList<>();
        }
        return getCards(trelloConfig.getDefaultListId());
    }

    /**
     * Skapar ett nytt kort i standard-listan (om konfigurerad)
     * @param task Task-objekt med kortets data
     * @return true om kortet skapades framgångsrikt, annars false
     */
    public boolean createCardInDefaultList(Task task) {
        if (!trelloConfig.hasDefaultListId()) {
            logger.warn("Ingen standard-lista konfigurerad för att skapa kort");
            return false;
        }
        return createCard(trelloConfig.getDefaultListId(), task);
    }

    /**
     * Hämtar listor från den konfigurerade boarden
     * @return Lista av listor som Map-objekt
     */
    public List<Map<String, Object>> getListsFromConfiguredBoard() {
        if (trelloConfig.getBoardId() == null || trelloConfig.getBoardId().isEmpty()) {
            logger.warn("Ingen board-ID konfigurerad");
            return new ArrayList<>();
        }
        return getLists(trelloConfig.getBoardId());
    }

    /**
     * Skapar en uppgift baserat på TaskForm (används av controllern)
     * Denna metod fungerar som en bro mellan webbformuläret och Trello API:et
     * @param taskForm Formulärdata från webbsidan
     * @return true om uppgiften skapades framgångsrikt, annars false
     * @throws Exception om något går fel under skapandet
     */
    public boolean createTask(TaskForm taskForm) throws Exception {
        logger.info("Skapar uppgift från formulär: {}", taskForm.getTitle());

        // Validera att vi har nödvändiga konfigurationer
        if (!trelloConfig.hasDefaultListId()) {
            throw new Exception("Ingen standard-lista är konfigurerad. Kontrollera application.yml");
        }

        if (taskForm.getTitle() == null || taskForm.getTitle().trim().isEmpty()) {
            throw new Exception("Uppgiftstitel får inte vara tom");
        }

        try {
            // Konvertera TaskForm till Task-objekt
            Task task = convertFormToTask(taskForm);

            // Skapa kortet i Trello
            boolean success = createCard(trelloConfig.getDefaultListId(), task);

            if (success) {
                logger.info("Uppgift '{}' skapad framgångsrikt", taskForm.getTitle());
                return true;
            } else {
                throw new Exception("Kunde inte skapa uppgift i Trello. Kontrollera API-konfiguration.");
            }

        } catch (RestClientException e) {
            logger.error("API-fel vid skapande av uppgift: {}", e.getMessage());
            throw new Exception("Anslutningsfel till Trello: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Oväntat fel vid skapande av uppgift: {}", e.getMessage());
            throw new Exception("Ett oväntat fel inträffade: " + e.getMessage());
        }
    }

    /**
     * Hjälpmetod för att konvertera TaskForm till Task
     * @param taskForm Formulärdata
     * @return Task-objekt redo för Trello API
     */
    private Task convertFormToTask(TaskForm taskForm) {
        Task task = new Task();
        task.setName(taskForm.getTitle());
        task.setDescription(taskForm.getDescription() != null ? taskForm.getDescription() : "");

        // Om TaskForm har fler fält, lägg till dem här
        if (taskForm.getPriority() != null) {
            // Lägg till prioritet i beskrivningen om det inte finns stöd i Task-klassen
            String description = task.getDescription();
            if (!description.isEmpty()) {
                description += "\n\n";
            }
            description += "Prioritet: " + taskForm.getPriority();
            task.setDescription(description);
        }

        return task;
    }

    /**
     * Hämtar alla uppgifter från standard-listan för visning på webbsidan
     * @return Lista med uppgifter som kan visas i Thymeleaf-mall
     */
    public List<Task> getAllTasksForDisplay() {
        logger.info("Hämtar alla uppgifter för visning");

        if (!trelloConfig.hasDefaultListId()) {
            logger.warn("Ingen standard-lista konfigurerad");
            return new ArrayList<>();
        }

        return getCardsFromDefaultList();
    }

    /**
     * Kontrollerar om Trello-tjänsten är redo att användas
     * @return true om allt är konfigurerat korrekt, annars false
     */
    public boolean isServiceReady() {
        return trelloConfig.getKey() != null && !trelloConfig.getKey().isEmpty() &&
                trelloConfig.getToken() != null && !trelloConfig.getToken().isEmpty() &&
                trelloConfig.getBaseUrl() != null && !trelloConfig.getBaseUrl().isEmpty();
    }

    /**
     * Hämtar alla boards för den autentiserade användaren
     * @return Lista av boards som Map-objekt
     */
    public List<Map<String, Object>> getBoards() {
        logger.info("Hämtar boards från Trello API");

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(trelloConfig.getBaseUrl() + "/members/me/boards")
                    .queryParam("key", trelloConfig.getKey())
                    .queryParam("token", trelloConfig.getToken())
                    .queryParam("filter", "open")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Lyckades hämta {} boards", response.getBody().size());
                return response.getBody();
            } else {
                logger.warn("Oväntat svar från Trello API: {}", response.getStatusCode());
                return new ArrayList<>();
            }

        } catch (RestClientException e) {
            logger.error("Fel vid hämtning av boards: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Hämtar listor från en specifik board
     * @param boardId Board ID att hämta listor från
     * @return Lista av listor som Map-objekt
     */
    public List<Map<String, Object>> getLists(String boardId) {
        logger.info("Hämtar listor för board: {}", boardId);

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(trelloConfig.getBaseUrl() + "/boards/" + boardId + "/lists")
                    .queryParam("key", trelloConfig.getKey())
                    .queryParam("token", trelloConfig.getToken())
                    .queryParam("filter", "open")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Lyckades hämta {} listor", response.getBody().size());
                return response.getBody();
            } else {
                logger.warn("Oväntat svar från Trello API: {}", response.getStatusCode());
                return new ArrayList<>();
            }

        } catch (RestClientException e) {
            logger.error("Fel vid hämtning av listor: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Hämtar kort från en specifik lista
     * @param listId Lista ID att hämta kort från
     * @return Lista av kort som Task-objekt
     */
    public List<Task> getCards(String listId) {
        logger.info("Hämtar kort för lista: {}", listId);

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(trelloConfig.getBaseUrl() + "/lists/" + listId + "/cards")
                    .queryParam("key", trelloConfig.getKey())
                    .queryParam("token", trelloConfig.getToken())
                    .queryParam("filter", "open")
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    List.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> cards = response.getBody();
                List<Task> tasks = new ArrayList<>();

                // Konvertera Trello-kort till Task-objekt
                for (Map<String, Object> card : cards) {
                    Task task = new Task();
                    task.setId((String) card.get("id"));
                    task.setName((String) card.get("name"));
                    task.setDescription((String) card.get("desc"));
                    task.setUrl((String) card.get("url"));
                    tasks.add(task);
                }

                logger.info("Lyckades hämta och konvertera {} kort", tasks.size());
                return tasks;
            } else {
                logger.warn("Oväntat svar från Trello API: {}", response.getStatusCode());
                return new ArrayList<>();
            }

        } catch (RestClientException e) {
            logger.error("Fel vid hämtning av kort: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Skapar ett nytt kort i Trello
     * @param listId Lista ID där kortet ska skapas
     * @param task Task-objekt med kortets data
     * @return true om kortet skapades framgångsrikt, annars false
     */
    public boolean createCard(String listId, Task task) {
        logger.info("Skapar nytt kort i lista: {}", listId);

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(trelloConfig.getBaseUrl() + "/cards")
                    .queryParam("key", trelloConfig.getKey())
                    .queryParam("token", trelloConfig.getToken())
                    .queryParam("idList", listId)
                    .queryParam("name", task.getName())
                    .queryParam("desc", task.getDescription())
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Kort skapat framgångsrikt");
                return true;
            } else {
                logger.warn("Kunde inte skapa kort: {}", response.getStatusCode());
                return false;
            }

        } catch (RestClientException e) {
            logger.error("Fel vid skapande av kort: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Kontrollerar om API-nyckeln och token fungerar
     * @return true om autentiseringen är giltig, annars false
     */
    public boolean isValidAuthentication() {
        logger.info("Kontrollerar Trello API-autentisering");

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(trelloConfig.getBaseUrl() + "/members/me")
                    .queryParam("key", trelloConfig.getKey())
                    .queryParam("token", trelloConfig.getToken())
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            boolean isValid = response.getStatusCode() == HttpStatus.OK;
            logger.info("Autentisering {}", isValid ? "giltig" : "ogiltig");
            return isValid;

        } catch (RestClientException e) {
            logger.error("Fel vid kontroll av autentisering: {}", e.getMessage());
            return false;
        }
    }
}
package galadriel.backEnd.Controllers;

import galadriel.backEnd.Models.Topic;
import galadriel.backEnd.Repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class BucketController {


    @Autowired
    TopicRepository topicRepository;

    @GetMapping("/topic/{meta}")
    public List<Topic> getMatching(@PathVariable("meta") String searchStr) {

        // first array holds entire repo
        // second holds only matching topics
        ArrayList<Topic> repo = new ArrayList<Topic>(topicRepository.findAll());
        ArrayList<Topic> matches = new ArrayList<Topic>();

        // for every topic in the repo, if the name property contains the search string,
        // add it to the second array
        for (int i = 0; i < repo.size(); i++) {
            if (((Topic) (repo.get(i))).getName().contains(searchStr)) {
                matches.add(((Topic) (repo.get(i))));

            }
        }

        // return second array
        return matches;
    }

    @PostMapping("/topic")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        try {
            return new ResponseEntity<>(topicRepository.save(topic), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

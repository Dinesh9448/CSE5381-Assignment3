package edu.uta.cse5381.assignment3.controller;

import edu.uta.cse5381.assignment3.repository.PeopleRepository;
import edu.uta.cse5381.assignment3.model.People;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@BasePathAwareController
@RequestMapping("people")*/
//@RepositoryRestController
//@RequestMapping("people")
@CrossOrigin
public class PeopleController {

    @Autowired @Setter private PeopleRepository peopleRepository;

    @PostMapping("/batch")
    public  @ResponseBody ResponseEntity<?> savePeoples(@RequestBody List<People> peopleList){
        return ResponseEntity.ok(""/*peopleRepository.saveAll(peopleList)*/);
    }
}

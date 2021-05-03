package edu.uta.cse5381.assignment3.controller;

import edu.uta.cse5381.assignment3.repository.DataRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/*@BasePathAwareController
@RequestMapping("people")*/
//@RepositoryRestController
//@RequestMapping("data")
@CrossOrigin
public class DataController {

    @Autowired @Setter private DataRepository dataRepository;

    @GetMapping("/betweenDistance")
    public  @ResponseBody ResponseEntity<?> savePeoples(@RequestParam String start, @RequestParam String end){
        return ResponseEntity.ok(dataRepository.findAllByDistanceBetween(new BigInteger(start), new BigInteger(end)));
    }
}

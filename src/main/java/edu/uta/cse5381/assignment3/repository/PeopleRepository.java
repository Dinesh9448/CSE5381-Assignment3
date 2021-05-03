package edu.uta.cse5381.assignment3.repository;

import edu.uta.cse5381.assignment3.model.People;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "peoples", path = "people")
@CrossOrigin
public interface PeopleRepository /*extends JpaRepository<People, BigInteger>*/ {

    public List<People> findBySalaryLessThan(BigInteger salary);
    public List<People> findByName(String name);
}

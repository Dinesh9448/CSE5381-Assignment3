package edu.uta.cse5381.assignment3.repository;

import edu.uta.cse5381.assignment3.model.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigInteger;
import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "datas", path = "data")
@CrossOrigin
public interface DataRepository /*extends JpaRepository<Data, String>*/ {


    public List<Data> findAllByDistanceBetween(BigInteger start, BigInteger end);

    public List<Data> findAllByAuthorEqualsAndNsizeBetween(String author, BigInteger start, BigInteger end);

}

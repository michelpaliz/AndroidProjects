package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Person;
import com.user_manager_v1.repository.PersonRepository;
import com.user_manager_v1.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PersonApiController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/add")
    public boolean addPerson(@RequestBody Person person) {
        try {
            Log.i("Nuevo Person: ", person.toString());
            personRepository.save(person);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("/update")
    public boolean updatePerson(@RequestBody Person person){
        try {
            Log.i("Update Person: ", person.toString());
            personRepository.save(person);
            return true;
        } catch (Exception e) {
            Log.e("Update autor", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}

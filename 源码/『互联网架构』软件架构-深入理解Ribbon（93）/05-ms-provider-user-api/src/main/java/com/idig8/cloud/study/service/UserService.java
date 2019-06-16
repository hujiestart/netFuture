package com.idig8.cloud.study.service;

import com.idig8.cloud.study.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable(value = "id") Long id);
  
}

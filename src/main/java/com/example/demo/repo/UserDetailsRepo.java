package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.UserDetailsDomain;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetailsDomain, String>{

}

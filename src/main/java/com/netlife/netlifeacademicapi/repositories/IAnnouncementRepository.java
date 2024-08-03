package com.netlife.netlifeacademicapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.netlife.netlifeacademicapi.models.Announcement;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnnouncementRepository extends JpaRepository<Announcement, String> {

}
package org.example.mapper;

import org.example.dto.*;
import org.example.model.Publisher;

import java.util.ArrayList;
import java.util.List;

public class PublisherMapper {
    public List<PublisherGetAllRs> toPublisherGetAllRs(List<Publisher> publishers){
        List<PublisherGetAllRs> getAllRs = new ArrayList<>();
        for (Publisher publisher : publishers){
            PublisherGetAllRs publisherGetAll = new PublisherGetAllRs();
            publisherGetAll.setId(publisher.getId());
            publisherGetAll.setName(publisher.getName());
            publisherGetAll.setBooks(publisher.getBooks());
            getAllRs.add(publisherGetAll);
        }
        return getAllRs;
    }

    public PublisherGetByIdRs getByIdRs(Publisher publisher){
        return PublisherGetByIdRs.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .books(publisher.getBooks())
                .build();
    }

    public Publisher toPublisherSave(PublisherSaveRq publisher){
        return Publisher.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }
    public PublisherSaveRs toPublisherSaveRs(PublisherSaveRq publisher){
        return PublisherSaveRs.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

    public Publisher toPublisherUpdate(PublisherUpdateRq publisherUpdateRq){
        return Publisher.builder()
                .name(publisherUpdateRq.getName())
                .build();
    }

    public PublisherUpdateRs toPublisherUpdateRs(PublisherUpdateRq publisherUpdateRq){
        return PublisherUpdateRs.builder()
                .name(publisherUpdateRq.getName())
                .build();
    }
}

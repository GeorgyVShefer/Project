package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dao.PublisherDAO;
import org.example.dto.*;
import org.example.mapper.PublisherMapper;
import org.example.model.Publisher;

import java.util.List;


@AllArgsConstructor
public class PublisherService {
    private PublisherDAO publisherDAO;
    private PublisherMapper mapper;

    public List<PublisherGetAllRs> getAll(){
        List<Publisher> all = publisherDAO.getAll();
        return mapper.toPublisherGetAllRs(all);
    }

    public PublisherGetByIdRs getById(int id){
        Publisher publisherId = publisherDAO.getById(id);
        return mapper.getByIdRs(publisherId);
    }

    public PublisherSaveRs save(PublisherSaveRq publisherSaveRq){
        publisherDAO.save(mapper.toPublisherSave(publisherSaveRq));
        return mapper.toPublisherSaveRs(publisherSaveRq);
    }

    public PublisherUpdateRs update(int id, PublisherUpdateRq publisherUpdateRq){
        publisherDAO.update(id, mapper.toPublisherUpdate(publisherUpdateRq));
        return mapper.toPublisherUpdateRs(publisherUpdateRq);
    }

    public String delete(int id){
        if(publisherDAO.deleteById(id)){
            return "Издатель с id " + id + " удален";
        }
        return "Издателя под таким id нет!";
    }
}

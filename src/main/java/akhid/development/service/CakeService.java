package akhid.development.service;


import akhid.development.model.postgres.Cake;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class CakeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CakeService.class);

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    public Map<String, Object> submit(Cake cake) {
        LOGGER.info("CakeService submit: {}", cake);

        String uuid = UUID.randomUUID().toString();
        Timestamp ldt = new Timestamp(new Date().getTime());
        Boolean active = true;

        // store to database
        insertData(cake, uuid, active, ldt);

        Map<String, Object> result = new HashMap<>();
        result.put("id", uuid);
        result.put("tittle", cake.tittle);
        result.put("description", cake.description);
        result.put("rating", cake.rating);
        result.put("image", cake.image);
        result.put("created_at", sdf.format(ldt));

        return result;
    }

    public Map<String, Object> deleteById(String id) {
        LOGGER.info("CakeService delete by id");

        Boolean active = false;
        Timestamp ldt = new Timestamp(new Date().getTime());

        if (Strings.isNullOrEmpty(id)) {
            throw new ValidationException("BAD_REQUEST");
        }

        Cake cake = Cake.findById(id);
        if(cake == null) {
            throw new NotFoundException("NOT_FOUND");
        }

        // store to database
        deleteData(id, active, ldt);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("active", active);
        result.put("deleted_at", sdf.format(ldt));

        return result;
    }

    @Transactional
    public void insertData (Cake cake, String id, Boolean active, Timestamp ldt) {

        Cake newCake = new Cake();
        newCake.id = id;
        newCake.tittle = cake.tittle;
        newCake.description = cake.description;
        newCake.rating = cake.rating;
        newCake.image = cake.image;
        newCake.active = active;
        newCake.createdAt = ldt;
        newCake.persist();

    }

    @Transactional
    public void deleteData (String id, Boolean active, Timestamp ldt) {

        Cake cake = Cake.findById(id);
        cake.active = active;
        cake.deletedAt =  ldt;
        cake.persist();

    }
}

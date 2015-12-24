package ro.devwfw.mfw.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.service.BaseService;

import java.util.Collection;

/**
 * @author laurentiumiu
 * @createdOn 12/20/15
 */
@RestController
public class BaseController extends ExceptionHandlerController {
    /**
     * The BaseService business service.
     */
    @Autowired
    private BaseService baseService;

    /**
     * Web service endpoint to fetch all BaseEntitys entities. The service returns
     * the collection of BaseEntities entities as JSON.
     *
     * @return A ResponseEntity containing a Collection of BaseEntitys objects.
     */
    @RequestMapping(
            value = "/web/baseEntities",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<BaseEntity>> getBaseEntities() {
        logger.info("> getBaseEntities");

        Collection<BaseEntity> baseEntityies = baseService.findAll();

        logger.info("< getBaseEntities");
        return new ResponseEntity<Collection<BaseEntity>>(baseEntityies, HttpStatus.OK);
    }

    /**
     * Web service endpoint to fetch a single BaseEntity entity by primary key
     * identifier.
     * <p>
     * If found, the BaseEntity is returned as JSON with HTTP status 200.
     * <p>
     * If not found, the service returns an empty response body with HTTP status
     * 404.
     *
     * @param id A Long URL path variable containing the BaseEntity primary key
     *           identifier.
     * @return A ResponseEntity containing a single BaseEntity object, if found,
     * and a HTTP status code as described in the method comment.
     */
    @RequestMapping(
            value = "/web/baseEntities/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseEntity> getBaseEntity(@PathVariable("id") Long id) {
        logger.info("> getBaseEntity id:{}", id);

        BaseEntity BaseEntity = baseService.findOne(id);
        if (BaseEntity == null) {
            return new ResponseEntity<BaseEntity>(HttpStatus.NOT_FOUND);
        }

        logger.info("< getBaseEntity id:{}", id);
        return new ResponseEntity<BaseEntity>(BaseEntity, HttpStatus.OK);
    }

    /**
     * Web service endpoint to create a single BaseEntity entity. The HTTP request
     * body is expected to contain a BaseEntity object in JSON format. The
     * BaseEntity is persisted in the data repository.
     * <p>
     * If created successfully, the persisted BaseEntity is returned as JSON with
     * HTTP status 201.
     * <p>
     * If not created successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param BaseEntity The BaseEntity object to be created.
     * @return A ResponseEntity containing a single BaseEntity object, if created
     * successfully, and a HTTP status code as described in the method
     * comment.
     */
    @RequestMapping(
            value = "/web/baseEntities",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseEntity> createBaseEntity(
            @RequestBody BaseEntity BaseEntity) {
        logger.info("> createBaseEntity");

        BaseEntity savedBaseEntity = baseService.create(BaseEntity);

        logger.info("< createBaseEntity");
        return new ResponseEntity<BaseEntity>(savedBaseEntity, HttpStatus.CREATED);
    }

    /**
     * Web service endpoint to update a single BaseEntity entity. The HTTP request
     * body is expected to contain a BaseEntity object in JSON format. The
     * BaseEntity is updated in the data repository.
     * <p>
     * If updated successfully, the persisted BaseEntity is returned as JSON with
     * HTTP status 200.
     * <p>
     * If not found, the service returns an empty response body and HTTP status
     * 404.
     * <p>
     * If not updated successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param BaseEntity The BaseEntity object to be updated.
     * @return A ResponseEntity containing a single BaseEntity object, if updated
     * successfully, and a HTTP status code as described in the method
     * comment.
     */
    @RequestMapping(
            value = "/web/baseEntities/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseEntity> updateBaseEntity(
            @RequestBody BaseEntity BaseEntity) {
        logger.info("> updateBaseEntity id:{}", BaseEntity.getId());

        BaseEntity updatedBaseEntity = baseService.update(BaseEntity);
        if (updatedBaseEntity == null) {
            return new ResponseEntity<BaseEntity>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateBaseEntity id:{}", BaseEntity.getId());
        return new ResponseEntity<BaseEntity>(updatedBaseEntity, HttpStatus.OK);
    }

    /**
     * Web service endpoint to delete a single BaseEntity entity. The HTTP request
     * body is empty. The primary key identifier of the BaseEntity to be deleted
     * is supplied in the URL as a path variable.
     * <p>
     * If deleted successfully, the service returns an empty response body with
     * HTTP status 204.
     * <p>
     * If not deleted successfully, the service returns an empty response body
     * with HTTP status 500.
     *
     * @param id A Long URL path variable containing the BaseEntity primary key
     *           identifier.
     * @return A ResponseEntity with an empty response body and a HTTP status
     * code as described in the method comment.
     */
    @RequestMapping(
            value = "/web/baseEntities/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<BaseEntity> deleteBaseEntity(
            @PathVariable("id") Long id) {
        logger.info("> deleteBaseEntity id:{}", id);

        baseService.delete(id);

        logger.info("< deleteBaseEntity id:{}", id);
        return new ResponseEntity<BaseEntity>(HttpStatus.NO_CONTENT);
    }


}

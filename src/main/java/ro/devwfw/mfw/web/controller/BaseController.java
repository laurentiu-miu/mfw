package ro.devwfw.mfw.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.devwfw.mfw.model.BaseEntity;
import ro.devwfw.mfw.service.BaseService;
import ro.devwfw.mfw.utils.mappings.PathVariableToClassMapper;
import ro.devwfw.mfw.web.component.RequestBodyEntityObject;

import java.util.Collection;

/**
 * @author LaurentiuM
 * @version createdOn: 12/20/15
 */
@RestController
public class BaseController extends ExceptionHandlerController {
    /**
     * The BaseService business service.
     */
    @Autowired
    private BaseService baseService;

    /**
     * The PathVariableToClassMapper maps class by entity name in the model layer.
     */
    @Autowired
    private PathVariableToClassMapper pathVariableToClassMapper;

    /**
     * Creates an instance of type entityClass.
     *
     * @param entityClass type of entityClass
     * @param <T> type of entityClass
     * @return An instance of entityClass
     */
    @ModelAttribute("entityObject")
    public <T extends BaseEntity> BaseEntity loadObject(@PathVariable("entityClass") String entityClass) {
        Class<T> clazz = pathVariableToClassMapper.getClassByPath(entityClass);
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Web service endpoint to fetch all BaseEntitys entities. The service returns
     * the collection of BaseEntities entities as JSON.
     * @param entityClass The entity name maped in @EntityName("demo") annotation
     *
     * @return A ResponseEntity containing a Collection of BaseEntities objects.
     */
    @RequestMapping(
            value = "/web/{entityClass}/getAll",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<BaseEntity>> getBaseEntities(@PathVariable("entityClass") String entityClass) {
        logger.info("> getBaseEntities");

        Class clazz = pathVariableToClassMapper.getClassByPath(entityClass);

        Collection<BaseEntity> baseEntityies = baseService.findAll(clazz);

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
     * @param entityClass The entity name maped in @EntityName("demo") annotation
     * @param id A Long URL path variable containing the BaseEntity primary key
     *           identifier.
     * @return A ResponseEntity containing a single BaseEntity object, if found,
     * and a HTTP status code as described in the method comment.
     */
    @RequestMapping(
            value = "/web/{entityClass}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseEntity> getBaseEntity(@PathVariable("entityClass") String entityClass, @PathVariable("id") Long id) {
        logger.info("> getBaseEntity id:{}", id);

        Class clazz = pathVariableToClassMapper.getClassByPath(entityClass);

        BaseEntity BaseEntity = baseService.findOne(clazz, id);
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
     * @param baseEntity The BaseEntity object to be created.
     * @return A ResponseEntity containing a single BaseEntity object, if created
     * successfully, and a HTTP status code as described in the method
     * comment.
     */
    @RequestMapping(
            value = "/web/{entityClass}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public <T extends BaseEntity> ResponseEntity<T> createBaseEntity(
            @RequestBodyEntityObject("entityObject") T baseEntity) {
        logger.info("> createBaseEntity");

        T savedBaseEntity = baseService.create(baseEntity);

        logger.info("< createBaseEntity");
        return new ResponseEntity<T>(savedBaseEntity, HttpStatus.CREATED);
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
     * @param baseEntity The BaseEntity object to be updated.
     * @param entityClass The entity name maped in @EntityName("demo") annotation
     *
     * @return A ResponseEntity containing a single BaseEntity object, if updated
     * successfully, and a HTTP status code as described in the method
     * comment.
     */
    @RequestMapping(
            value = "/web/{entityClass}/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public <T extends BaseEntity> ResponseEntity<T> updateBaseEntity(
            @RequestBodyEntityObject("entityObject") T baseEntity,
            @PathVariable("entityClass") String entityClass) {
        logger.info("> updateBaseEntity id:{}", baseEntity.getId());

        T updatedBaseEntity = baseService.update(baseEntity);
        if (updatedBaseEntity == null) {
            return new ResponseEntity<T>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("< updateBaseEntity id:{}", baseEntity.getId());
        return new ResponseEntity<T>(updatedBaseEntity, HttpStatus.OK);
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
     * @param entityClass The entity name maped in @EntityName("demo") annotation
     * @param id A Long URL path variable containing the BaseEntity primary key
     *           identifier.
     * @return A ResponseEntity with an empty response body and a HTTP status
     * code as described in the method comment.
     */
    @RequestMapping(
            value = "/web/{entityClass}/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<BaseEntity> deleteBaseEntity(
            @PathVariable("entityClass") String entityClass,
            @PathVariable("id") Long id) {
        logger.info("> deleteBaseEntity id:{}", id);

        Class clazz = pathVariableToClassMapper.getClassByPath(entityClass);

        baseService.delete(clazz, id);

        logger.info("< deleteBaseEntity id:{}", id);
        return new ResponseEntity<BaseEntity>(HttpStatus.NO_CONTENT);
    }


}

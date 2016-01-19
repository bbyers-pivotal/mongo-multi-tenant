package com.example

import com.mongodb.Mongo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class SecureDocumentController {

    @Autowired
    Mongo mongo

    //NOT autowired, don't want to impact the existing mongoTemplate
    MongoTemplate mongoTemplate

    /**
     * Return a secure document from mongo
     * GET http://localhost:8080/secureDocument/asdbfasyasdfasdfads?clientId=alpha
     * will return a document from collection secureDocument in database client_alpha
     * @param id
     * @param clientId
     * @return
     */
    @RequestMapping(value = '/secureDocument/{id}', method = RequestMethod.GET)
    public SecureDocument getSecureDocument(@PathVariable('id') String id, @RequestParam('custId') String custId) {
        switchDatabase(custId)
        mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), SecureDocument)
    }

    /**
     * Return all secure documents from mongo
     *
     * GET http://localhost:8080/secureDocument?clientId=alpha
     * will return all documents from collection secureDocument in database client_alpha
     * @param clientId
     * @return
     */
    @RequestMapping(value = '/secureDocument', method = RequestMethod.GET)
    public List<SecureDocument> getAllSecureDocument(@RequestParam('clientId') String clientId) {
        switchDatabase(clientId)
        mongoTemplate.findAll(SecureDocument)
    }

    /**
     * Save a secure document to mongo
     *
     * POST http://localhost:8080/secureDocument?clientId=alpha (form body JSON {"filename":"somelongfilename"})
     * will save document in collection secureDocument in from database client_alpha
     * @param body
     * @param clientId
     * @return
     */
    @RequestMapping(value = '/secureDocument', method = RequestMethod.POST)
    public ResponseEntity saveDocument(@RequestBody body, @RequestParam('clientId') String clientId) {
        switchDatabase(clientId)
        SecureDocument secureDocument = new SecureDocument()
        secureDocument.filename = body.filename
        mongoTemplate.insert(secureDocument)

        new ResponseEntity(HttpStatus.CREATED)
    }

    /**
     * Switch database name for the current call
     * @param clientId
     */
    private void switchDatabase(String clientId) {
        mongoTemplate = new MongoTemplate(mongo, "client_$clientId")
    }
}

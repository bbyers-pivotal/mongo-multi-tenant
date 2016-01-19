package com.example

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by bbyers on 1/18/16.
 */
class SecureDocument {

    @Id
    String id
    String filename
}

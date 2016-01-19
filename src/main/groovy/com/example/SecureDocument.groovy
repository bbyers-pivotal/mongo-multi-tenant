package com.example

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class SecureDocument {

    @Id
    String id
    String filename
}

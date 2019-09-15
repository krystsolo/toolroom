package com.manageyourtools.toolroom.catalogue.domain

import groovy.transform.CompileStatic

@CompileStatic
trait SampleCategories {

    Category electric = new Category(1, "electric")
    Category mechanical = new Category(2, "mechanical")

}
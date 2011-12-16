package com.thoughtworks.storage;

import org.neo4j.graphdb.RelationshipType;

public enum EdgeType implements RelationshipType {
    TRAIN,
    ROOT
}
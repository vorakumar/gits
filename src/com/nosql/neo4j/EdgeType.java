package com.nosql.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum EdgeType implements RelationshipType {
    TRAIN,
    ROOT
}
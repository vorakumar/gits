package com.thoughtworks.storage;

import com.thoughtworks.properties.ApplicationProperties;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.io.File;

public class GraphDatabase {

    private static GraphDatabaseService graphDb;
    private Transaction transaction;

    private GraphDatabaseService startDatabase() {
        graphDb = new EmbeddedGraphDatabase(ApplicationProperties.databaseLocation);
        Index<Node> nodeIndex = graphDb.index().forNodes("nodes");
        registerShutdownHookForNeo();
        return graphDb;
    }

    private static void deleteFileOrDirectory(final File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFileOrDirectory(child);
            }
        } else {
            file.delete();
        }
    }

    private static void shutDown() {
        graphDb.shutdown();
    }

    public Index<Node> getIndexedNode() {
        IndexManager index = graphDb.index();
        Index<Node> nodes = index.forNodes("nodes");
        return nodes;
    }

    public void startTransaction() {
        transaction = graphDb.beginTx();
    }

    public void endTransaction() {
        transaction.success();
    }

    public GraphDatabaseService getDatabaseService(boolean cleanDatabase) {
        if (graphDb == null) {
            return createDatabase(cleanDatabase);
        }
        return graphDb;
    }

    private GraphDatabaseService createDatabase(boolean cleanDatabase) {
        if (cleanDatabase) {
            deleteFileOrDirectory(new File(ApplicationProperties.databaseLocation));
        }
        return startDatabase();
    }

    private static void registerShutdownHookForNeo() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutDown();
            }
        });
    }
}
